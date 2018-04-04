package com.copasso.cocobill.mvp.presenter;

import com.copasso.cocobill.base.BasePresenter;
import com.copasso.cocobill.model.bean.local.BBill;

public abstract  class MonthDetailPresenter extends BasePresenter {

    public abstract void getMonthDetailBills(int id,String year,String month);

    public abstract void deleteBill(Long id);

    public abstract void updateBill(BBill bBill);
}
