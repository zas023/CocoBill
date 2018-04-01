package com.copasso.cocobill.mvp.presenter;

import com.copasso.cocobill.base.BasePresenter;

public abstract  class MonthAccountPresenter extends BasePresenter {

    public abstract void getMonthAccountBills(int id,String year,String month);
}
