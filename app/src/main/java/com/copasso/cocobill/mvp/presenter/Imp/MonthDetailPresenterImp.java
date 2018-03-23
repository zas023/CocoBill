package com.copasso.cocobill.mvp.presenter.Imp;

import com.copasso.cocobill.model.bean.BBill;
import com.copasso.cocobill.model.bean.BaseBean;
import com.copasso.cocobill.model.bean.packages.MonthDetailBean;
import com.copasso.cocobill.mvp.model.Imp.MonthDetailModelImp;
import com.copasso.cocobill.mvp.model.MonthDetailModel;
import com.copasso.cocobill.mvp.presenter.MonthDetailPresenter;
import com.copasso.cocobill.mvp.view.MonthDetailView;

import java.util.List;

public class MonthDetailPresenterImp extends MonthDetailPresenter implements MonthDetailModelImp.MonthDetailOnListener{

    private MonthDetailModel monthDetailModel;
    private MonthDetailView monthDetailView;

    public MonthDetailPresenterImp(MonthDetailView monthDetailView) {
        this.monthDetailModel=new MonthDetailModelImp(this);
        this.monthDetailView = monthDetailView;
    }

    /**
     * 服务器当月账单
     * @param bean
     */
    @Override
    public void onSuccess(MonthDetailBean bean) {
        monthDetailView.loadDataSuccess(bean);
    }

    /**
     * 本地本月账单
     * @param list
     */
    @Override
    public void onSuccess(List<BBill> list) {
        monthDetailView.loadDataSuccess(list);
    }

    @Override
    public void onSuccess(BaseBean bean) {
        monthDetailView.loadDataSuccess(bean);
    }

    @Override
    public void onFailure(Throwable e) {
        monthDetailView.loadDataError(e);
    }

    @Override
    public void getMonthDetailBills(String id, String year, String month) {
        monthDetailModel.getMonthDetailBills(id,year,month);
    }

    @Override
    public void getLocalMonthDetailBills(int id, String year, String month) {
        monthDetailModel.getLocalMonthDetailBills(id, year, month);
    }

    @Override
    public void deleteBill(int id) {
        monthDetailModel.delete(id);
    }

}
