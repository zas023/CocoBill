package com.copasso.cocobill.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import com.copasso.cocobill.model.bean.remote.MyUser;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhouas666 on 16/8/11.
 * 无MVP的Fragment基类
 */

public abstract class BaseFragment extends Fragment {
    protected String TAG;
    protected View mView;
    protected Activity mActivity;
    protected Context mContext;

    protected MyUser currentUser;
    private Unbinder mUnBinder;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        // 设置 TAG
        TAG = this.getClass().getSimpleName();
        //当前用户
        currentUser= BmobUser.getCurrentUser(MyUser.class);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        initEventAndData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
        beforeDestroy();
    }

    protected abstract int getLayoutId();
    protected abstract void initEventAndData();
    protected abstract void beforeDestroy();
}
