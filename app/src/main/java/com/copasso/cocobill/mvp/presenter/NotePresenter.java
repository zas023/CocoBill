package com.copasso.cocobill.mvp.presenter;

import com.copasso.cocobill.base.BasePresenter;

public abstract  class NotePresenter extends BasePresenter {

    /**
     * 获取信息
     * @param id
     */
    public abstract void getNote(int id);

    /**
     * 添加账单分类
     * @param userid
     * @param sortName
     * @param sortImg
     * @param income
     */
    public abstract void addSort(int userid, String sortName, String sortImg, boolean income);

    /**
     *添加账单支付方式
     * @param userid
     * @param payName
     * @param payImg
     * @param payNum
     */
    public abstract void addPay(int userid, String payName, String payImg, String payNum);


    /**
     * 删除账单分类
     */
    public abstract void delete(int id);
}
