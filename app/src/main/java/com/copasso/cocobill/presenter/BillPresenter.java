package com.copasso.cocobill.presenter;

import com.copasso.cocobill.base.BasePresenter;

public abstract  class BillPresenter extends BasePresenter {

    /**
     * 获取信息
     * @param id
     */
    public abstract void getNote(int id);

    /**
     * 添加账单
     */
    public abstract void add(int userid, int sortid, int payid,String cost, String content,
             String crdate, boolean income);

    /**
     * 修改账单
     */
    public abstract void update(int id, int userid, int sortid, int payid,String cost, String content,
                String crdate, boolean income);

    /**
     * 删除账单
     */
    public abstract void delete(int id);
}
