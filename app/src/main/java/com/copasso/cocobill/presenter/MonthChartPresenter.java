package com.copasso.cocobill.presenter;

import com.copasso.cocobill.base.BasePresenter;

public abstract  class MonthChartPresenter extends BasePresenter {

    public abstract void getMonthChartBills(String id,String year,String month);
}
