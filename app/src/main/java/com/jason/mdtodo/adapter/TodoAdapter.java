package com.jason.mdtodo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jason.mdtodo.MdTodoApplication;
import com.jason.mdtodo.NewTodoActivity;
import com.jason.mdtodo.R;
import com.jason.mdtodo.entity.Todo;
import com.jason.mdtodo.utils.SortUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private static final String TAG = "MdTodo-TodoAdapter";
    private Context mContext;
    private List<Todo> mTodoList;

    public TodoAdapter(List<Todo> mTodoList) {
        this.mTodoList = mTodoList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        CheckBox checkBox;
        TextView textView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            checkBox = itemView.findViewById(R.id.todo_checkbox);
            textView = itemView.findViewById(R.id.todo_tv);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        //绑定Layout布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.todo_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Todo todo = mTodoList.get(holder.getAdapterPosition());
                LongClickListenerHandler(todo);
                return true;
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Todo todo = mTodoList.get(position);
                todo.setChecked(holder.checkBox.isChecked());
                todo.save();
                if (todo.isChecked()){
                    Toast.makeText(mContext,todo.getContent()+"->已完成",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext,todo.getContent()+"->未完成",Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG,"更新完成状态："+todo.getContent()+"->"+holder.checkBox.isChecked());
            }
        });
        holder.checkBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Todo todo = mTodoList.get(holder.getAdapterPosition());
                LongClickListenerHandler(todo);
                return true;
            }
        });
        return holder;
    }

    /*
        长按卡片，弹出Dialog
     */
    public void LongClickListenerHandler(Todo todo){
        new MaterialAlertDialogBuilder(mContext)
                .setTitle("待办 : "+todo.getContent())
                .setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, NewTodoActivity.class);
                        intent.putExtra("isEdit",true);
                        intent.putExtra("todo",todo);
                        mContext.startActivity(intent);
                        Log.i(TAG,"编辑");
                    }
                })
                .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        todo.delete();
                        SortUtils.sortByMode(mTodoList,TodoAdapter.this, MdTodoApplication.getMode());
                        Toast.makeText(mContext,"删除："+todo.getContent(),Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"删除待办："+todo.toString());
                    }
                })
                .show();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo todo = mTodoList.get(position);
        holder.checkBox.setChecked(todo.isChecked());
        holder.checkBox.setText(todo.getContent());
        holder.textView.setText(todo.getDate()+" "+todo.getRemindTime());
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }


}
