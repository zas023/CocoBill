package com.copasso.cocobill.mvp.presenter.Imp;

import com.copasso.cocobill.model.bean.remote.MyUser;
import com.copasso.cocobill.mvp.model.Imp.UserInfoModelImp;
import com.copasso.cocobill.mvp.model.UserInfoModel;
import com.copasso.cocobill.mvp.presenter.UserInfoPresenter;
import com.copasso.cocobill.mvp.view.UserInfoView;

public class UserInfoPresenterImp extends UserInfoPresenter implements UserInfoModelImp.UserInfoOnListener {

    private UserInfoModel model;
    private UserInfoView view;

    public UserInfoPresenterImp(UserInfoView view) {
        this.model=new UserInfoModelImp(this);
        this.view = view;
    }

    @Override
    public void onSuccess(MyUser user) {
        view.loadDataSuccess(user);
    }

    @Override
    public void onFailure(Throwable e) {
        view.loadDataError(e);
    }

    @Override
    public void update(MyUser user) {
        model.update(user);
    }
}
