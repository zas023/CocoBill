package com.copasso.cocobill.presenter.Imp;

import com.copasso.cocobill.bean.MonthChartBean;
import com.copasso.cocobill.model.Imp.MonthChartModelImp;
import com.copasso.cocobill.model.MonthChartModel;
import com.copasso.cocobill.presenter.MonthChartPresenter;
import com.copasso.cocobill.view.MonthChartView;

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
    public void getMonthChartBills(String id, String year, String month) {
        model.getMonthChartBills(id,year,month);
    }

}
