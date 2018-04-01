package com.copasso.cocobill.mvp.presenter.Imp;

import com.copasso.cocobill.model.bean.local.MonthChartBean;
import com.copasso.cocobill.mvp.model.Imp.MonthChartModelImp;
import com.copasso.cocobill.mvp.model.MonthChartModel;
import com.copasso.cocobill.mvp.presenter.MonthChartPresenter;
import com.copasso.cocobill.mvp.view.MonthChartView;

public class MonthChartPresenterImp extends MonthChartPresenter implements MonthChartModelImp.MonthChartOnListener{

    private MonthChartModel model;
    private MonthChartView view;

    public MonthChartPresenterImp(MonthChartView view) {
        this.model=new MonthChartModelImp(this);
        this.view = view;
    }

    @Override
    public void onSuccess(MonthChartBean bean) {
        view.loadDataSuccess(bean);
    }

    @Override
    public void onFailure(Throwable e) {
        view.loadDataError(e);
    }

    @Override
    public void getMonthChartBills(int id, String year, String month) {
        model.getMonthChartBills(id,year,month);
    }

}
