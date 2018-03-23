package com.copasso.cocobill.mvp.model;

public interface MonthDetailModel {

    /**
     * 每月账单详情
     */
    void getMonthDetailBills(String id, String year, String month);

    /**
     * 每月本地账单详情
     */
    void getLocalMonthDetailBills(int id, String year, String month);

    /**
     * 删除账单
     */
    void delete(int id);

    void onUnsubscribe();
}
