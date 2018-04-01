package com.copasso.cocobill.mvp.presenter;

import com.copasso.cocobill.base.BasePresenter;
import com.copasso.cocobill.model.bean.local.BBill;

public abstract  class BillPresenter extends BasePresenter {

    /**
     * 获取信息
     */
    public abstract void getNote();

    /**
     * 添加账单
     */
    public abstract void add(BBill bBill);

    /**
     * 修改账单
     */
    public abstract void update(BBill bBill);


    /**
     * 删除账单
     */
    public abstract void delete(Long id);
}
