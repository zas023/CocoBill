package com.thmub.app.billkeeper.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Enosh on 2020-03-07
 * Github: https://github.com/zas023
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static String TAG;
    protected Activity mContext;
    // ButterKnife
    protected Unbinder unbinder;
    //RxJava
    protected CompositeDisposable mDisposable;

    /****************************Abstract area****************************/
    @LayoutRes
    protected abstract int getLayoutId();

    /****************************Initialization****************************/
    protected void addDisposable(Disposable d) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(d);
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
     * 逻辑使用区
     */
    protected void processLogic() {
    }

    /****************************Lifecycle****************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        //设置 TAG
        TAG = this.getClass().getSimpleName();
        //注册ButterKnife注解
        unbinder = ButterKnife.bind(this);
        //init
        initData(savedInstanceState);
        initWidget();
        initEvent();
        initClick();
        processLogic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        unbinder.unbind();
    }
}
