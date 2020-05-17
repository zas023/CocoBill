package com.copasso.cocobill.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.copasso.cocobill.R;
import com.copasso.cocobill.utils.ThemeManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Zhouas666 on AndroidStudio
 * Date: 2019-01-08
 * Github: https://github.com/zas023
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static String TAG;

    protected Activity mActivity;
    protected Context mContext;

    protected CompositeDisposable mDisposable;

    protected Toolbar mToolbar;

    /****************************abstract*************************************/

    @LayoutRes
    protected abstract int getLayoutId();


    /************************初始化************************************/
    protected void addDisposable(Disposable d) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(d);
    }

    /**
     * 配置Toolbar
     */
    protected void setUpToolbar(Toolbar toolbar) {
    }

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected void initData(Bundle savedInstanceState) {
    }

    /**
     * 初始化零件
     */
    protected void initWidget() {
    }

    /**
     * 初始化事件
     */
    protected void initEvent() {
    }

    /**
     * 初始化点击事件
     */
    protected void initClick() {
    }

    /**
     * 执行逻辑
     */
    protected void processLogic() {
    }

    protected void beforeDestroy() {
    }

    /*************************lifecycle*****************************************************/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置 Activity 屏幕方向
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 隐藏 ActionBar
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        // 设置主题色，，，一定要在setView之前
        ThemeManager.getInstance().init(this);

        setContentView(getLayoutId());
        mActivity = this;
        mContext = this;
        // 设置 TAG
        TAG = this.getClass().getSimpleName();
        //init
        initData(savedInstanceState);
        initToolbar();
        initWidget();
        initEvent();
        initClick();
        processLogic();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            supportActionBar(mToolbar);
            setUpToolbar(mToolbar);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        beforeDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    /**************************used method*******************************************/

    protected ActionBar supportActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(
                (v) -> finish()
        );
        return actionBar;
    }
}
