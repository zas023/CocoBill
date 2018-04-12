package com.copasso.cocobill.mvp.presenter;

import com.copasso.cocobill.base.BasePresenter;
import com.copasso.cocobill.model.bean.local.BPay;
import com.copasso.cocobill.model.bean.local.BSort;

public abstract  class NotePresenter extends BasePresenter {

    /**
     * 获取信息
     */
    public abstract void getNote();

    /**
     * 添加账单分类
     */
    public abstract void addSort(BSort bSort);

    /**
     *添加账单支付方式
     */
    public abstract void addPay(BPay bPay);


    /**
     * 删除账单分类
     */
    public abstract void deleteSort(Long id);

    /**
     * 删除账单支出方式
     */
    public abstract void deletePay(Long id);
}
