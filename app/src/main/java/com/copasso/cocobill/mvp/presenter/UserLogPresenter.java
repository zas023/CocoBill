package com.copasso.cocobill.mvp.presenter;

import com.copasso.cocobill.base.BasePresenter;

public abstract  class UserLogPresenter extends BasePresenter {

    public abstract void login(String username,String password);

    public abstract void signup(String username,String password,String mail);
}
