package com.copasso.cocobill.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter {


    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /**
     * RXjava取消注册，以避免内存泄露
     */
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }

    /**
     * RXjava注册
     * @param disposable
     */
    public void register(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

}
