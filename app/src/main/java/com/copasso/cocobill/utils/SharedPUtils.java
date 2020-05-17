package com.copasso.cocobill.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.copasso.cocobill.model.bean.local.NoteBean;
import com.google.gson.Gson;

/**
 * Created by zhouas666 on 2017/12/25.
 */
public class SharedPUtils {

    public final static String USER_INFOR = "userInfo";
    public final static String USER_SETTING = "userSetting";


    /**
     * 获取当前用户账单分类信息
     */
    public static NoteBean getUserNoteBean(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_INFOR, Context.MODE_PRIVATE);
        if (sp != null) {
            String jsonStr = sp.getString("noteBean", null);
            if (jsonStr != null) {
                Gson gson = new Gson();
                return gson.fromJson(jsonStr, NoteBean.class);
            }
        }
        return null;
    }

    /**
     * 设置当前用户账单分类信息
     */
    public static void setUserNoteBean(Context context, String jsonStr) {
        SharedPreferences sp = context.getSharedPreferences(USER_INFOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("noteBean", jsonStr);
        editor.commit();
    }

    /**
     * 设置当前用户账单分类信息
     */
    public static void setUserNoteBean(Context context, NoteBean noteBean) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(noteBean);
        SharedPreferences sp = context.getSharedPreferences(USER_INFOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("noteBean", jsonStr);
        editor.commit();
    }

    /**
     * 获取当前用户主题
     */
    public static String getCurrentTheme(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_SETTING, Context.MODE_PRIVATE);
        if (sp != null)
            return sp.getString("theme", "原谅绿");
        return null;
    }

    /**
     * 获取当前用户主题
     */
    public static void setCurrentTheme(Context context, String theme) {
        SharedPreferences sp = context.getSharedPreferences(USER_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (editor != null) {
            editor.putString("theme", theme);
            editor.commit();
        }
    }

    /**
     * 判断是否第一次进入APP
     * @param context
     * @return
     */
    public static boolean isFirstStart(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_SETTING, Context.MODE_PRIVATE);
        boolean isFirst= sp.getBoolean("first", true);
        //第一次则修改记录
        if(isFirst)
            sp.edit().putBoolean("first", false).commit();
        return isFirst;
    }
}
