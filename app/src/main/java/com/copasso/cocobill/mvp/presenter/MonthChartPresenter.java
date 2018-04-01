package com.copasso.cocobill.mvp.presenter;

import com.copasso.cocobill.base.BasePresenter;

public abstract  class MonthChartPresenter extends BasePresenter {

    public abstract void getMonthChartBills(int id,String year,String month);
}
