package com.copasso.cocobill.presenter.contract;

import com.copasso.cocobill.base.BaseContract;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.local.MonthListBean;

/**
 * Created by Zhouas666 on 2019-01-08
 * Github: https://github.com/zas023
 */
public interface MonthListContract extends BaseContract {

    interface View extends BaseContract.BaseView {

        void loadDataSuccess(MonthListBean list);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void getMonthList(String id, String year, String month);

        void deleteBill(Long id);

        void updateBill(BBill bBill);
    }
}
