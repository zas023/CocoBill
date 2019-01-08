package com.copasso.cocobill.presenter;


import android.util.Log;

import com.copasso.cocobill.base.RxPresenter;
import com.copasso.cocobill.model.bean.remote.MyUser;
import com.copasso.cocobill.presenter.contract.LandContract;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Zhouas666 on 2019-01-08
 * Github: https://github.com/zas023
 */
public class LandPresenter extends RxPresenter<LandContract.View> implements LandContract.Presenter{

    private String TAG="LandPresenter";

    @Override
    public void login(String username, String password) {
        MyUser.loginByAccount(username, password, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null)
                    mView.landSuccess(myUser);
                else
                    mView.onFailure(e);
            }
        });
    }

    @Override
    public void signup(String username, String password, String mail) {
        MyUser myUser =new MyUser();
        myUser.setUsername(username);
        myUser.setPassword(password);
        myUser.setEmail(mail);

        myUser.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null)
                    mView.landSuccess(myUser);
                else
                    mView.onFailure(e);
            }
        });
    }
}
