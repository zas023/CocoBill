package com.copasso.cocobill.api;

import com.copasso.cocobill.model.bean.*;
import com.copasso.cocobill.common.HttpConfig;
import com.copasso.cocobill.model.bean.local.NoteBean;
import com.copasso.cocobill.model.bean.remote.*;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zhouas666 on 2018/1/19.
 */

public interface APIService {

    /**
     * 用户登陆
     *
     * @param username
     * @param password
     * @return
     */
    @GET(HttpConfig.USER_LOGIN)
    Observable<UserBean> login(@Query("username") String username, @Query("password") String password);

    /**
     * 用户注册
     *
     * @param username
     * @param password
     * @param mail
     * @return
     */
    @GET(HttpConfig.USER_SIGN)
    Observable<UserBean> signup(@Query("username") String username, @Query("password") String password
            , @Query("mail") String mail);

    /**
     * 修改账号信息
     *
     * @param id
     * @param username
     * @param gender
     * @param phone
     * @param mail
     * @return
     */
    @GET(HttpConfig.USER_UPDATE)
    Observable<UserBean> updateUser(@Query("id") int id, @Query("username") String username
            , @Query("gender") String gender, @Query("phone") String phone, @Query("mail") String mail);

    /**
     * 每月账单详情
     *
     * @param id
     * @param year
     * @param month
     * @return
     */
    @GET(HttpConfig.BILL_MONTH_DETIAL)
    Observable<MonthDetailBean> getMonthDetial(@Path("id") String id, @Path("year") String year
            , @Path("month") String month);

    /**
     * 每月账单分类
     *
     * @param id
     * @param year
     * @param month
     * @return
     */
    @GET(HttpConfig.BILL_MONTH_CHART)
    Observable<MonthChartBean> getMonthChart(@Path("id") String id, @Path("year") String year
            , @Path("month") String month);

    /**
     * 每月账户统计
     *
     * @param id
     * @param year
     * @param month
     * @return
     */
    @GET(HttpConfig.BILL_MONTH_CARD)
    Observable<MonthAccountBean> getMonthAccount(@Path("id") String id, @Path("year") String year
            , @Path("month") String month);

    /**
     * 添加账单
     *
     * @param userid
     * @param sortid
     * @param payid
     * @param cost
     * @param crdate
     * @param content
     * @param income
     * @return
     */
    @GET(HttpConfig.BILL_ADD)
    Observable<BaseBean> addBill(@Query("userid") int userid, @Query("sortid") int sortid
            , @Query("payid") int payid, @Query("cost") String cost
            , @Query("content") String content, @Query("crdate") String crdate, @Query("income") boolean income);

    /**
     * 修改账单
     *
     * @param id
     * @param userid
     * @param sortid
     * @param payid
     * @param cost
     * @param content
     * @param crdate
     * @param income
     * @return
     */
    @GET(HttpConfig.BILL_UPDATE)
    Observable<BaseBean> updateBill(@Query("id") int id, @Query("userid") int userid, @Query("sortid") int sortid
            , @Query("payid") int payid, @Query("cost") String cost
            , @Query("content") String content, @Query("crdate") String crdate, @Query("income") boolean income);

    /**
     * 删除账单
     *
     * @param id
     * @return
     */
    @GET(HttpConfig.BILL_DELETE)
    Observable<BaseBean> deleteBill(@Path("id") int id);


    /**
     * 获取用户分类和支付方式
     *
     * @param id
     * @return
     */
    @GET(HttpConfig.NOTE_USER)
    Observable<NoteBean> getNote(@Path("id") int id);

    /**
     * 添加账单分类
     * @param uid
     * @param sortName
     * @param sortImg
     * @param income
     * @return
     */
    @GET(HttpConfig.NOTE_SORT_ADD)
    Observable<BSortBean> addSort(@Query("uid") int uid, @Query("sortName") String sortName
            , @Query("sortImg") String sortImg, @Query("income") Boolean income);

    /**
     * 添加账单支付方式
     * @param uid
     * @param sortName
     * @param sortImg
     * @param payNum
     * @return
     */
    @GET(HttpConfig.NOTE_PAY_ADD)
    Observable<BPayBean> addPay(@Query("uid") int uid, @Query("payName") String sortName
            , @Query("payImg") String sortImg, @Query("payNum") String payNum);


}
