package com.copasso.cocobill.presenter;


import com.copasso.cocobill.base.RxPresenter;
import com.copasso.cocobill.model.bean.remote.MyUser;
import com.copasso.cocobill.presenter.contract.UserInfoContract;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Zhouas666 on 2019-01-08
 * Github: https://github.com/zas023
 */
public class UserInfoPresenter extends RxPresenter<UserInfoContract.View>
        implements UserInfoContract.Presenter {

    private String TAG = "UserInfoPresenter";

    @Override
    public void updateUser(MyUser user) {
        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null)
                    mView.onSuccess();
                else
                    mView.onFailure(e);
            }
        });
    }
}
