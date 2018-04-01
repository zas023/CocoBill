package com.copasso.cocobill.mvp.model.Imp;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import com.copasso.cocobill.api.RetrofitFactory;
import com.copasso.cocobill.base.BaseObserver;
import com.copasso.cocobill.model.bean.BmobUser;
import com.copasso.cocobill.model.bean.remote.UserBean;
import com.copasso.cocobill.mvp.model.UserLogModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserLogModelImp implements UserLogModel {

    private UserLogOnListener listener;

    public UserLogModelImp(UserLogOnListener listener) {
        this.listener = listener;
    }

    @Override
    public void login(String username, String password) {
//        BmobUser.loginByAccount(username, password, new LogInListener<BmobUser>() {
//            @Override
//            public void done(BmobUser bmobUser, BmobException e) {
//
//            }
//        });
        RetrofitFactory.getInstence().API()
                .login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<UserBean>() {
                    @Override
                    protected void onSuccees(UserBean userBean) throws Exception {
                        listener.onSuccess(userBean);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void signup(String username, String password, String mail) {
        RetrofitFactory.getInstence().API()
                .signup(username, password, mail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<UserBean>() {
                    @Override
                    protected void onSuccees(UserBean userBean) throws Exception {
                        listener.onSuccess(userBean);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void onUnsubscribe() {

    }

    /**
     * 回调接口
     */
    public interface UserLogOnListener {

        void onSuccess(UserBean user);

        void onFailure(Throwable e);
    }
}
