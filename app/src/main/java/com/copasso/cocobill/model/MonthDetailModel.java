package com.copasso.cocobill.model;

public interface MonthDetailModel {

    /**
     * 每月账单详情
     */
    void getMonthDetailBills(String id, String year, String mail);

    void onUnsubscribe();
}
