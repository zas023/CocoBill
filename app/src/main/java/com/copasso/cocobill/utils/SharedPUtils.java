package com.copasso.cocobill.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.copasso.cocobill.bean.UserBean;
import com.google.gson.Gson;

/**
 * Created by zhouas666 on 2017/12/25.
 */
public class SharedPUtils {

    /**
     * 获取当前用户
     */
    public static UserBean getCurrentUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (sp != null) {
            Gson gson=new Gson();
            return gson.fromJson(sp.getString("json", "null"),UserBean.class);
        }
        return null;
    }
}
