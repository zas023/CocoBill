package com.thmub.app.billkeeper;

import android.app.Application;
import android.content.Context;

/**
 * Created by Enosh on 2020-03-15
 * Github: https://github.com/zas023
 */
public class App extends Application {

    public static App application;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = getApplicationContext();
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }
}
