package com.copasso.cocobill.presenter.Imp;

import com.copasso.cocobill.bean.MonthDetailBean;
import com.copasso.cocobill.model.Imp.MonthDetailModelImp;
import com.copasso.cocobill.model.MonthDetailModel;
import com.copasso.cocobill.presenter.MonthDetailPresenter;
import com.copasso.cocobill.view.MonthDetailView;

public class MonthDetailPresenterImp extends MonthDetailPresenter implements MonthDetailModelImp.MonthDetailOnListener{

    private MonthDetailModel monthDetailModel;
    private MonthDetailView monthDetailView;

    public MonthDetailPresenterImp(MonthDetailView monthDetailView) {
        this.monthDetailModel=new MonthDetailModelImp(this);
        this.monthDetailView = monthDetailView;
    }

    @Override
    public void onSuccess(MonthDetailBean bean) {
        monthDetailView.loadDataSuccess(bean);
    }

    @Override
    public void onFailure(Throwable e) {
        monthDetailView.loadDataError(e);
    }

    @Override
    public void getMonthDetailBills(String id, String year, String mail) {
        monthDetailModel.getMonthDetailBills(id,year,mail);
    }

}
