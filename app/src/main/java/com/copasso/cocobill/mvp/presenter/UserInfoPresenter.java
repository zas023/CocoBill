package com.copasso.cocobill.mvp.presenter;

import com.copasso.cocobill.base.BasePresenter;
import com.copasso.cocobill.model.bean.remote.MyUser;

public abstract  class UserInfoPresenter extends BasePresenter {

    public abstract void update(MyUser user);
}
