package com.example.sqlapplication;

import android.app.Application;

import com.example.sqlapplication.data.model.User;

public class MyApplication extends Application {
    static MyApplication instance;

    Mode mode;

    User user;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public MyApplication getInstance() {
        return instance;
    }

    public enum Mode {
        FOUND,
        LOST
    }

    public static Mode getMode() {
        return instance.mode;
    }

    public static void setMode(Mode mode) {
        instance.mode = mode;
    }

    public static void setUser(User user) {
        instance.user = user;
    }

    public static User getUser() {
        return instance.user;
    }

}
