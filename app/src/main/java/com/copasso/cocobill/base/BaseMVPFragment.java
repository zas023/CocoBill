package com.copasso.cocobill.base;

/**
 * Created by Zhouas666 on AndroidStudio
 * Date: 2019-01-08
 * Github: https://github.com/zas023
 */
public abstract class BaseMVPFragment<T extends BaseContract.BasePresenter> extends BaseFragment
        implements BaseContract.BaseView{

    protected T mPresenter;

    protected abstract T bindPresenter();

    @Override
    protected void processLogic(){
        mPresenter = bindPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
