package com.jason.mdtodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.jason.mdtodo.entity.Todo;

import java.util.Calendar;
import java.util.UUID;

public class NewTodoActivity extends AppCompatActivity {
    private static final String TAG = "MdTodo-NewTodoActivity";
    private Toolbar toolbar;
    private FloatingActionButton fab_ok;
    String todo_title;
    TextInputLayout textField_title;
    Button todo_date_btn, todo_remindTime_btn;
    private int mYear,mMonth,mDay;
    private int mHour,mMin;
    private String todoDate,remindTime;
    private CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);
        Log.i(TAG,"进入NewTodoActivity");
        initDateAndTime();
        initView();
    }

    private void initDateAndTime() {
        Calendar instance = Calendar.getInstance();
        mYear = instance.get(Calendar.YEAR);
        mMonth = instance.get(Calendar.MONTH);
        mDay = instance.get(Calendar.DAY_OF_MONTH);
        mHour = instance.get(Calendar.HOUR_OF_DAY);
        mMin = instance.get(Calendar.MINUTE);
    }

    private void initView() {
        fab_ok = findViewById(R.id.todo_fab_ok);
        textField_title = findViewById(R.id.textField_title);
        todo_date_btn = findViewById(R.id.new_todo_date_btn);
        todo_remindTime_btn = findViewById(R.id.new_todo_remindTime_btn);
        toolbar = findViewById(R.id.new_toolbar);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        Intent intent = getIntent();
        boolean isEdit = intent.getBooleanExtra("isEdit",false);
        Todo editTodo = (Todo)intent.getSerializableExtra("todo");

        //调出返回按钮
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (isEdit) {
            Log.i(TAG,"进入编辑待办模式");
            toolbarLayout.setTitle("编辑待办");
            textField_title.getEditText().setText(editTodo.getContent());
            todo_date_btn.setText(editTodo.getDate());
            todo_remindTime_btn.setText(editTodo.getRemindTime());
        }

        /*
          提交按钮事件监听
         */
        fab_ok.setOnClickListener(v -> {
            if (TextUtils.isEmpty(textField_title.getEditText().getText().toString())) {
                Toast.makeText(NewTodoActivity.this,"待办事项可不能不写啊~",Toast.LENGTH_SHORT).show();
            }else if (todo_date_btn.getText().toString().equals("")){
                Toast.makeText(NewTodoActivity.this,"没有设置提醒日期",Toast.LENGTH_SHORT).show();
            }else if (todo_remindTime_btn.getText().toString().equals("")){
                Toast.makeText(NewTodoActivity.this,"没有设置提醒时间",Toast.LENGTH_SHORT).show();
            }else {
                todo_title = textField_title.getEditText().getText().toString();
                Todo todo = new Todo();
                todo.setContent(todo_title);
                todo.setDate(todo_date_btn.getText().toString());
                todo.setChecked(false);
                todo.setRemindTime(todo_remindTime_btn.getText().toString());
                if (!isEdit){
                    todo.setmId(UUID.randomUUID().toString());
                    if (todo.save()){
                        Log.i(TAG,"添加待办："+todo.toString());
                        Toast.makeText(NewTodoActivity.this,"添加成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(NewTodoActivity.this,"保存失败，请稍后再试",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    todo.setmId(editTodo.getmId());
                    int res = todo.updateAll("mid=?", editTodo.getmId());
                    if (res > 0){
                        Log.i(TAG,"修改待办："+todo.toString());
                        Toast.makeText(NewTodoActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(NewTodoActivity.this,"修改失败，请稍后再试",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        /*
            日期选择事件监听
         */
        todo_date_btn.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(NewTodoActivity.this, android.R.style.Theme_Material_Dialog, onDateSetListener, mYear, mMonth, mDay);
            datePickerDialog.setCancelable(true);
            datePickerDialog.setCanceledOnTouchOutside(true);
            datePickerDialog.show();
        });

        /*
            提醒时间选择事件监听
         */
        todo_remindTime_btn.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog =new TimePickerDialog(NewTodoActivity.this, android.R.style.Theme_Material_Dialog,onTimeSetListener,mHour,mMin,true  );
            timePickerDialog.setCancelable(true);
            timePickerDialog.setCanceledOnTouchOutside(true);
            timePickerDialog.show();
        });


    }


    /**
     *  日期选择器对话框监听
     */
    public DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;
            todoDate = year+ "年"+(month + 1) + "月" + dayOfMonth + "日";
            todo_date_btn.setText(todoDate);
        }
    };

    /**
     *  提醒时间选择器对话框监听
     */
    public TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMin = minute;
            if (minute < 10){
                remindTime = hourOfDay + ":0" + minute;
            }else {
                remindTime = hourOfDay + ":" +minute;
            }
            todo_remindTime_btn.setText(remindTime);
        }
    };

    /*
        返回按钮
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}