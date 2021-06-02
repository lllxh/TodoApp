package com.jason.mdtodo;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

public class MdTodoApplication extends Application {
    public static int mode;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
        mode = 0;
    }

    public static int getMode() {
        return mode;
    }

    public static void setMode(int mode) {
        MdTodoApplication.mode = mode;
    }

    public static Context getContext() {
        return context;
    }
}
