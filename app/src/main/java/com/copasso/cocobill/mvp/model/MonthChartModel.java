package com.copasso.cocobill.mvp.model;

public interface MonthChartModel {

    /**
     * 每月账单图表数据
     */
    void getMonthChartBills(int id, String year, String month);

    void onUnsubscribe();
}
