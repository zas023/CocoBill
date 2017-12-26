package com.copasso.cocobill;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    public static MyApplication application;
    private static Context context;

    /**
     * 获取上下文
     * @return
     */
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = getApplicationContext();
    }
}
