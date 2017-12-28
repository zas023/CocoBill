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
            return gson.fromJson(sp.getString("jsonStr", null),UserBean.class);
        }
        return null;
    }

    /**
     * 设置当前用户
     */
    public static void setCurrentUser(Context context,String jsonStr) {
        Gson gson = new Gson();
        UserBean userBean = gson.fromJson(jsonStr, UserBean.class);
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", userBean.getUsername());
        editor.putString("email", userBean.getMail());
        editor.putInt("id", userBean.getId());
        editor.putString("jsonStr", jsonStr);
        editor.commit();
    }

    /**
     * 设置当前用户
     */
    public static void setCurrentUser(Context context,UserBean userBean) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(userBean);
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", userBean.getUsername());
        editor.putString("email", userBean.getMail());
        editor.putInt("id", userBean.getId());
        editor.putString("jsonStr", jsonStr);
        editor.commit();
    }

    /**
     * 获取当前用户头像
     */
    public static String getCurrentUserImagePath(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (sp != null)
            return sp.getString("imgPath", "null");
        return null;
    }

    /**
     * 设置当前用户头像
     */
    public static void setCurrentUserImagePath(Context context,String imgPath) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (editor != null) {
            editor.putString("imgPath", imgPath);
            editor.commit();
        }
    }
}
