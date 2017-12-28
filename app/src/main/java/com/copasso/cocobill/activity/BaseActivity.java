package com.copasso.cocobill.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.copasso.cocobill.R;
import com.copasso.cocobill.utils.ActivityManagerUtil;
import com.copasso.cocobill.utils.ThemeManager;


public abstract class BaseActivity extends AppCompatActivity {

    protected Activity mContext;
    private Unbinder mUnBinder;

    private Toolbar mToolbar;

    protected static String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 设置 Activity 屏幕方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 隐藏 ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置 TAG
        TAG = this.getClass().getSimpleName();
        //
        super.onCreate(savedInstanceState);
        mContext = this;
        // 设置主题色，，，一定要在setView之前
        ThemeManager.getInstance().init(this);
        setContentView(getLayoutInflater().inflate(getLayout(), null, true));
        mUnBinder = ButterKnife.bind(this);
        initEventAndData();
        ActivityManagerUtil.mActivities.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
        ActivityManagerUtil.mActivities.remove(this);
    }

    protected abstract int getLayout();
    protected abstract void initEventAndData();
}
