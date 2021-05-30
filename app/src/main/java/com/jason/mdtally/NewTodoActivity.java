package com.jason.mdtally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.jason.mdtally.entity.Todo;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.Objects;

public class NewTodoActivity extends AppCompatActivity {
    private static final String TAG = "MdTally-NewTodoActivity";
    private Toolbar toolbar;
    private FloatingActionButton fab_ok;
    String todo_title;
    TextInputLayout textField_title;
    Button todo_date_btn, todo_remindTime_btn;
    private int mYear,mMonth,mDay;
    private int mHour,mMin;
    private String todoDate,remindTime;

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

        /*
          提交按钮事件监听
         */
        fab_ok.setOnClickListener(v -> {
            if (todoDate == null) {
                Toast.makeText(NewTodoActivity.this,"没有设置提醒日期",Toast.LENGTH_SHORT).show();
            }else if (remindTime == null){
                Toast.makeText(NewTodoActivity.this,"没有设置提醒事件",Toast.LENGTH_SHORT).show();

            }else if (TextUtils.isEmpty(textField_title.getEditText().getText().toString())){
                Toast.makeText(NewTodoActivity.this,"待办事项可不能不写啊~",Toast.LENGTH_SHORT).show();
            }else {
                todo_title = textField_title.getEditText().getText().toString();
                Todo todo = new Todo();
                todo.setContent(todo_title);
                todo.setDate(todoDate);
                todo.setRemindTime(remindTime);
                if (todo.save()){
                    Log.i(TAG,"添加待办："+todo.toString());
                    Toast.makeText(NewTodoActivity.this,"保存成功！",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(NewTodoActivity.this,"保存失败，请稍后再试",Toast.LENGTH_SHORT).show();
                }

            }
        });

        todo_date_btn.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(NewTodoActivity.this, android.R.style.Theme_Material_Dialog, onDateSetListener, mYear, mMonth, mDay);
            datePickerDialog.setCancelable(true);
            datePickerDialog.setCanceledOnTouchOutside(true);
            datePickerDialog.show();
        });

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
}