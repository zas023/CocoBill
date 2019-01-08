package com.copasso.cocobill.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Zhouas666 on AndroidStudio
 * Date: 2019-01-08
 * Github: https://github.com/zas023
 */
public class RxPresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected T mView;
    protected CompositeDisposable mDisposable;

    protected void unSubscribe() {
        if (mDisposable != null) mDisposable.dispose();
    }

    protected void addDisposable(Disposable subscription) {
        if (mDisposable == null) mDisposable = new CompositeDisposable();
        mDisposable.add(subscription);
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
