package com.copasso.cocobill.mvp.model.Imp;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import com.copasso.cocobill.model.bean.MyUser;
import com.copasso.cocobill.mvp.model.UserInfoModel;

public class UserInfoModelImp implements UserInfoModel {

    private UserInfoOnListener listener;

    public UserInfoModelImp(UserInfoOnListener listener) {
        this.listener = listener;
    }

    @Override
    public void update(MyUser user) {
        user.update(user.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null)
                    listener.onSuccess(new MyUser());
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
