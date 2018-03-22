package com.copasso.cocobill.mvp.presenter;

import com.copasso.cocobill.base.BasePresenter;

public abstract  class UserInfoPresenter extends BasePresenter {

    public abstract void update(int id, String username, String gengder, String phone, String mail);
}
