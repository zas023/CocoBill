package com.copasso.cocobill.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.copasso.cocobill.MyApplication;

import androidx.core.content.ContextCompat;

/**
 * Created by zhouas666 on 2017/12/24.
 * Class desc: ui 操作相关封装
 */
public class UiUtils {

    /**
     * 获取上下文
     */
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /**
     * 获取资源操作类
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取字符串资源
     *
     * @param id 资源id
     * @return 字符串
     */
    public static String getString(int id) {
        return getResources().getString(id);
    }

    /**
     * 获取字符串数组资源
     *
     * @param id 资源id
     * @return 字符串数组
     */
    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    /**
     * 获取颜色资源
     */
    public static int getColor(int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    /**
     * 获取颜色资源
     *
     * @param id    资源id
     * @param theme 样式
     * @return
     */
    public static int getColor(int id, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(id, theme);
        }
        return getResources().getColor(id);
    }

    /**
     * 获取drawable资源
     *
     * @param id 资源id
     * @return
     */
    public static Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    /**
     * 加载布局（使用View方式）
     *
     * @param resource 布局资源id
     * @return View
     */
    public static View inflate(int resource) {
        return View.inflate(getContext(), resource, null);
    }

    /** 显示不带 null 的字符 */
    public static String show(String text){
        return text != null ? text : "";
    }

}

