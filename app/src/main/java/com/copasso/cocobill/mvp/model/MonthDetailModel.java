package com.copasso.cocobill.mvp.model;

public interface MonthDetailModel {

    /**
     * 每月账单详情
     */
    void getMonthDetailBills(int id, String year, String month);

    /**
     * 删除账单
     */
    void delete(Long id);

    void onUnsubscribe();
}
