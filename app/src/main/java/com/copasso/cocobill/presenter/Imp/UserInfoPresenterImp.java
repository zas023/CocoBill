package com.copasso.cocobill.presenter.Imp;

import com.copasso.cocobill.bean.UserBean;
import com.copasso.cocobill.model.Imp.UserInfoModelImp;
import com.copasso.cocobill.model.UserInfoModel;
import com.copasso.cocobill.presenter.UserInfoPresenter;
import com.copasso.cocobill.view.UserInfoView;

public class UserInfoPresenterImp extends UserInfoPresenter implements UserInfoModelImp.UserInfoOnListener {

    private UserInfoModel model;
    private UserInfoView view;

    public UserInfoPresenterImp(UserInfoView view) {
        this.model=new UserInfoModelImp(this);
        this.view = view;
    }

    @Override
    public void onSuccess(UserBean user) {
        view.loadDataSuccess(user);
    }

    @Override
    public void onFailure(Throwable e) {
        view.loadDataError(e);
    }

    @Override
    public void update(int id, String username, String gengder, String phone, String mail) {
        model.update(id,username,gengder,phone,mail);
    }
}
