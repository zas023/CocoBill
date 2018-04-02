package com.copasso.cocobill.mvp.model;

import com.copasso.cocobill.model.bean.remote.MyUser;

public interface UserInfoModel {

    void update(MyUser user);

    void onUnsubscribe();
}
