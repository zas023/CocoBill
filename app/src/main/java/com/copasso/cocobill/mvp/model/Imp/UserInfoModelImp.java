package com.copasso.cocobill.mvp.model.Imp;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import com.copasso.cocobill.api.RetrofitFactory;
import com.copasso.cocobill.base.BaseObserver;
import com.copasso.cocobill.model.bean.MyUser;
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
    public void update(final MyUser user) {
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null)
                    listener.onSuccess(user);
                else
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

        void onSuccess(MyUser user);

        void onFailure(Throwable e);
    }
}
