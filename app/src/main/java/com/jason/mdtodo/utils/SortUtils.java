package com.jason.mdtodo.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import com.jason.mdtodo.adapter.TodoAdapter;
import com.jason.mdtodo.entity.Todo;

import org.litepal.crud.DataSupport;

import java.util.List;

public class SortUtils {
    private final static String TAG = "MdTodo-SortUtils";

    public static void sortByMode(List<Todo> todoList, TodoAdapter todoAdapter, int mode){
        todoList.clear();
        switch (mode) {
            case 0:
                todoList.addAll(DataSupport.findAll(Todo.class));
                Log.i(TAG,"按创建时间排序");
                break;
            case 1:
                todoList.addAll(DataSupport.order("date desc").find(Todo.class));
                Log.i(TAG,"按日期排序");
                break;
            case 2:
                todoList.addAll(DataSupport.order("checked").find(Todo.class));
                Log.i(TAG,"按完成情况排序");
                break;
        }
        todoAdapter.notifyDataSetChanged();
    }

}
