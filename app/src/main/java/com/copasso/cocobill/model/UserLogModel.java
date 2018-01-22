package com.copasso.cocobill.model;

public interface UserLogModel {

    /**
     * 用户登陆
     */
    void login(String username,String password);

    /**
     * 用户注册
     */
    void signup(String username,String password,String mail);

    void onUnsubscribe();
}
