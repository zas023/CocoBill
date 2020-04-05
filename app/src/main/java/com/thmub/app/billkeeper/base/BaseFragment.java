package com.thmub.app.billkeeper.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Enosh on 2020-03-11
 * Github: https://github.com/zas023
 */
public abstract class BaseFragment extends Fragment {

    protected String TAG;

    protected Activity mActivity;
    protected Context mContext;

    private View root = null;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    /*******************************init area*********************************/
    protected void initData(Bundle savedInstanceState){
    }

    /**
     * 初始化点击事件
     */
    protected void initClick(){
    }

    /**
     * 逻辑使用区
     */
    protected void processLogic(){
    }

    /**
     * 初始化零件
     */
    protected void initWidget(Bundle savedInstanceState){
    }

    /******************************lifecycle area*****************************************/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resId = getLayoutId();
        root = inflater.inflate(resId,container,false);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(savedInstanceState);
        unbinder = ButterKnife.bind(this,root);
        TAG=getName();
        initWidget(savedInstanceState);
        initClick();
        processLogic();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    /**************************公共类*******************************************/
    public String getName(){
        return getClass().getName();
    }

    protected <VT> VT getViewById(int id){
        if (root == null){
            return  null;
        }
        return (VT) root.findViewById(id);
    }
}
