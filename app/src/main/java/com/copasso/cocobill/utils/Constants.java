package com.copasso.cocobill.utils;

public class Constants {

    //网络请求
    public static final String BASE_URL = "http://139.199.176.173:8080/ssmBillBook";
    public static final String USER_LOGIN = "/user/login";
    public static final String USER_SIGN = "/user/sign";
    public static final String USER_UPDATE = "/user/update";
    public static final String USER_CHANGEPW = "/user/changePw";
    public static final String USER_FORGETPW = "/user/forgetPw";
    public static final String BILL_MONTH_DETIAL = "/bill/user";
    public static final String BILL_MONTH_CHART = "/bill/chart";
    public static final String BILL_MONTH_CARD = "/bill/pay";
    public static final String BILL_DELETE = "/bill/delete";
    public static final String BILL_UPDATE = "/bill/update";
    public static final String BILL_ADD = "/bill/add";
    public static final String NOTE_USER = "/note/user";
    public static final String NOTE_SORT_ADD = "/note/sort/add";
    public static final String NOTE_SORT_UPDATE = "/note/sort/update";
    public static final String NOTE_PAY_ADD = "/note/pay/add";
    public static final String NOTE_PAY_UPDATE = "/note/pay/update";
    public static final String IMAGE_USER ="/upload/";
    public static final String IMAGE_SORT ="/upload/noteImg/sort/";
    public static final String IMAGE_PAY ="/upload/noteImg/pay/";


    public static final String CACHE = "cache";
    public static final int LATEST_COLUMN = Integer.MAX_VALUE;
    public static final int BASE_COLUMN = 100000000;

    //当前用户
    public static int currentUserId=0;

    public static String EXTRA_IS_UPDATE_THEME  = "com.copasso.cocobill.IS_UPDATE_THEME";
}
