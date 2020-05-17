package com.copasso.cocobill.presenter.contract;

import com.copasso.cocobill.base.BaseContract;
import com.copasso.cocobill.model.bean.local.MonthChartBean;

/**
 * Created by Zhouas666 on 2019-01-08
 * Github: https://github.com/zas023
 */
public interface MonthChartContract extends BaseContract {

    interface View extends BaseView {

        void loadDataSuccess(MonthChartBean bean);

    }

    interface Presenter extends BasePresenter<View> {

        void getMonthChart(String id, String year, String month);
    }
}
