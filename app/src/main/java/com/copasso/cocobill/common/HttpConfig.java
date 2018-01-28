package com.copasso.cocobill.common;

/**
 * Created by zhouas666 on 2018/1/19.\
 * 关于网络的配置
 */
public class HttpConfig {
    public static int HTTP_TIME=30000;
    //网络请求
    public static final String BASE_URL = "http://139.199.176.173:8080/ssmBillBook/";
    public static final String USER_LOGIN = "user/login";
    public static final String USER_SIGN = "user/sign";
    public static final String USER_UPDATE = "user/update";
    public static final String USER_CHANGEPW = "user/changePw";
    public static final String USER_FORGETPW = "user/forgetPw";
    public static final String BILL_MONTH_DETIAL = "bill/user/{id}/{year}/{month}";
    public static final String BILL_MONTH_CHART = "bill/chart/{id}/{year}/{month}";
    public static final String BILL_MONTH_CARD = "bill/pay/{id}/{year}/{month}";
    public static final String BILL_DELETE = "bill/delete/{id}";
    public static final String BILL_UPDATE = "bill/update";
    public static final String BILL_ADD = "bill/add";
    public static final String NOTE_USER = "note/user/{id}";
    public static final String NOTE_SORT_ADD = "note/sort/add";
    public static final String NOTE_SORT_UPDATE = "note/sort/update";
    public static final String NOTE_PAY_ADD = "note/pay/add";
    public static final String NOTE_PAY_UPDATE = "note/pay/update";
    public static final String IMAGE_USER ="upload/";
    public static final String IMAGE_SORT ="upload/noteImg/sort/";
    public static final String IMAGE_PAY ="upload/noteImg/pay/";
}
