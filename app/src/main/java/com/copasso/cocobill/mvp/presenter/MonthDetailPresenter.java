package com.copasso.cocobill.mvp.presenter;

import com.copasso.cocobill.base.BasePresenter;

public abstract  class MonthDetailPresenter extends BasePresenter {

    public abstract void getMonthDetailBills(int id,String year,String month);

    public abstract void deleteBill(Long id);
}
