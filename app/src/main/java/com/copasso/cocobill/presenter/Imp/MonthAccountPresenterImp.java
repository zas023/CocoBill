package com.copasso.cocobill.presenter.Imp;

import com.copasso.cocobill.bean.MonthAccountBean;
import com.copasso.cocobill.bean.MonthChartBean;
import com.copasso.cocobill.model.Imp.MonthAccountModelImp;
import com.copasso.cocobill.model.Imp.MonthChartModelImp;
import com.copasso.cocobill.model.MonthAccountModel;
import com.copasso.cocobill.model.MonthChartModel;
import com.copasso.cocobill.presenter.MonthAccountPresenter;
import com.copasso.cocobill.presenter.MonthChartPresenter;
import com.copasso.cocobill.view.MonthAccountView;
import com.copasso.cocobill.view.MonthChartView;

public class MonthAccountPresenterImp extends MonthAccountPresenter implements MonthAccountModelImp.MonthAccountOnListener{

    private MonthAccountModel model;
    private MonthAccountView view;

    public MonthAccountPresenterImp(MonthAccountView view) {
        this.model=new MonthAccountModelImp(this);
        this.view = view;
    }

    @Override
    public void onSuccess(MonthAccountBean bean) {
        view.loadDataSuccess(bean);
    }

    @Override
    public void onFailure(Throwable e) {
        view.loadDataError(e);
    }


    @Override
    public void getMonthAccountBills(String id, String year, String month) {
        model.getMonthAccountBills(id,year,month);
    }
}
