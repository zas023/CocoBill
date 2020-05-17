package com.copasso.cocobill;

import android.app.Application;
import android.content.Context;

import com.copasso.cocobill.model.bean.remote.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by Zhouas666 on AndroidStudio
 * Date: 2019-01-08
 * Github: https://github.com/zas023
 */
public class MyApplication extends Application {

    public static MyApplication application;
    private static Context context;
    private static MyUser currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = getApplicationContext();
        //初始化Bmob后端云
        Bmob.initialize(this, "941f4add6503358048b02b83fcb605f6");
        currentUser = BmobUser.getCurrentUser(MyUser.class);
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 获取用户id
     * @return
     */
    public static String getCurrentUserId() {
        currentUser = BmobUser.getCurrentUser(MyUser.class);
        if (currentUser == null)
            return null;
        return BmobUser.getCurrentUser(MyUser.class).getObjectId();
    }
}
