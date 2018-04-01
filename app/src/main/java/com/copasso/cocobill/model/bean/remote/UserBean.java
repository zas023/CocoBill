package com.copasso.cocobill.model.bean.remote;

import com.copasso.cocobill.model.bean.BaseBean;

public class UserBean extends BaseBean {
    private Integer id;

    private String username;

    private String password;

    private String image;

    private String gender;

    private String phone;

    private String mail;

    private Integer state;

    private String mailcode;

    private String phonecode;

    public UserBean() {
        super();
    }

    public UserBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserBean(String username, String password, String mail) {
        this.username = username;
        this.password = password;
        this.mail = mail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

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
        this.gender = gender == null ? null : gender.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail == null ? null : mail.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMailcode() {
        return mailcode;
    }

    public void setMailcode(String mailcode) {
        this.mailcode = mailcode == null ? null : mailcode.trim();
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode == null ? null : phonecode.trim();
    }
}