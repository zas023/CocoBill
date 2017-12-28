package com.copasso.cocobill.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.copasso.cocobill.R;
import com.copasso.cocobill.utils.ActivityManagerUtil;
import com.copasso.cocobill.utils.ThemeManager;


public abstract class BaseActivity extends AppCompatActivity {

    protected Activity mContext;
    private Unbinder mUnBinder;

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ActivityManagerUtil.mActivities.add(this);
        mUnBinder = ButterKnife.bind(this);
        mContext = this;
        initEventAndData();

        // 设置主题色
        ThemeManager.getInstance().init(this);
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
