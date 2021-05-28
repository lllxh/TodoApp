package com.jason.mdtally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jason.mdtally.R;
import com.jason.mdtally.entity.Todo;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private Context mContext;
    private List<Todo> mTodoList;

    public TodoAdapter(List<Todo> mTodoList) {
        this.mTodoList = mTodoList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            checkBox = itemView.findViewById(R.id.todo_checkbox);
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
        holder.cardView.setOnClickListener(v -> {
            //TODO 点击card后弹出编辑
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Todo todo = new Todo();
                todo.setChecked(isChecked);
                todo.save();
                notifyDataSetChanged();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo todo = mTodoList.get(position);
        holder.checkBox.setChecked(todo.isChecked());
        holder.checkBox.setText(todo.getContent());
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }


}
