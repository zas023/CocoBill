package com.copasso.cocobill.mvp.model.Imp;

import com.copasso.cocobill.api.RetrofitFactory;
import com.copasso.cocobill.base.BaseObserver;
import com.copasso.cocobill.model.bean.remote.UserBean;
import com.copasso.cocobill.mvp.model.UserInfoModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserInfoModelImp implements UserInfoModel {

    private UserInfoOnListener listener;

    public UserInfoModelImp(UserInfoOnListener listener) {
        this.listener = listener;
    }

    @Override
    public void update(int id, String username, String gengder, String phone, String mail) {
        RetrofitFactory.getInstence().API()
                .updateUser(id, username, gengder, phone, mail)
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
    public interface UserInfoOnListener {

        void onSuccess(UserBean user);

        void onFailure(Throwable e);
    }
}
