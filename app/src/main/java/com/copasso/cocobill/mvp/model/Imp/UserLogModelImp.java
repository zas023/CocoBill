package com.copasso.cocobill.mvp.model.Imp;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import com.copasso.cocobill.model.bean.remote.MyUser;
import com.copasso.cocobill.mvp.model.UserLogModel;

public class UserLogModelImp implements UserLogModel {

    private UserLogOnListener listener;

    public UserLogModelImp(UserLogOnListener listener) {
        this.listener = listener;
    }

    @Override
    public void login(String username, String password) {
        MyUser.loginByAccount(username, password, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null)
                    listener.onSuccess(myUser);
                else
                    listener.onFailure(e);
            }
        });
//        RetrofitFactory.getInstence().API()
//                .login(username, password)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<UserBean>() {
//                    @Override
//                    protected void onSuccees(UserBean userBean) throws Exception {
//                        listener.onSuccess(userBean);
//                    }
//
//                    @Override
//                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
//                        listener.onFailure(e);
//                    }
//                });
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
                    listener.onSuccess(myUser);
                else
                    listener.onFailure(e);
            }
        });
//        RetrofitFactory.getInstence().API()
//                .signup(username, password, mail)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<UserBean>() {
//                    @Override
//                    protected void onSuccees(UserBean userBean) throws Exception {
//                        listener.onSuccess(userBean);
//                    }
//
//                    @Override
//                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
//                        listener.onFailure(e);
//                    }
//                });
    }

    @Override
    public void onUnsubscribe() {

    }

    /**
     * 回调接口
     */
    public interface UserLogOnListener {

        void onSuccess(MyUser user);

        void onFailure(Throwable e);
    }
}
