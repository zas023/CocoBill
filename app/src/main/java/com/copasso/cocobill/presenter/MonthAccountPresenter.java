package com.copasso.cocobill.presenter;

import com.copasso.cocobill.base.BasePresenter;

public abstract  class MonthAccountPresenter extends BasePresenter {

    public abstract void getMonthAccountBills(String id,String year,String month);
}
