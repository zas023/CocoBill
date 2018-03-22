package com.copasso.cocobill.mvp.model;

public interface MonthAccountModel {

    /**
     * 每月账单图表数据
     */
    void getMonthAccountBills(String id, String year, String month);

    void onUnsubscribe();
}
