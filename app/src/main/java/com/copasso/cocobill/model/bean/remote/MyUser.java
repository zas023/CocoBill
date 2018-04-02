package com.copasso.cocobill.model.bean.remote;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {

    private String image;

    private String gender;

    private String age;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
