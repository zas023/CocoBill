package com.copasso.cocobill.mvp.model;

public interface UserInfoModel {

    void update(int id, String username, String gengder, String phone, String mail);

    void onUnsubscribe();
}
