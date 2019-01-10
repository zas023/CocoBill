package com.copasso.cocobill.presenter.contract;

import com.copasso.cocobill.base.BaseContract;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.local.NoteBean;

/**
 * Created by Zhouas666 on 2019-01-08
 * Github: https://github.com/zas023
 */
public interface BillContract extends BaseContract {

    interface View extends BaseView {

        void loadDataSuccess(NoteBean bean);

    }

    interface Presenter extends BasePresenter<View>{
        /**
         * 获取信息
         */
        void getBillNote();

        /**
         * 添加账单
         */
        void addBill(BBill bBill);

        /**
         * 修改账单
         */
        void updateBill(BBill bBill);


        /**
         * 删除账单
         */
        void deleteBillById(Long id);
    }
}
