package com.copasso.cocobill;

import android.app.Application;
import android.content.Context;
import cn.bmob.v3.Bmob;

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

        //第一：默认初始化
        Bmob.initialize(this, "941f4add6503358048b02b83fcb605f6");
    }
}
