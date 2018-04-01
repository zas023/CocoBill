package com.copasso.cocobill.model.bean.remote;

import com.copasso.cocobill.model.bean.local.BSort;
import com.copasso.cocobill.model.bean.BaseBean;

/**
 * 账单bean
 */
public class BBillBean extends BaseBean {

    /**
     * id : 72
     * cost : 100.0
     * content : test
     * userid : 1
     * payid : 0
     * sortid : 1
     * crdate : 1512379901000
     * income : false
     * version : 0
     * sort : {"id":72,"uid":0,"sortName":"偿还费用","sortImg":"changhuanfeiyong@2x.png","priority":0,"income":false}
     */

    private int id;
    private double cost;
    private String content;
    private int userid;
    private int payid;
    private int sortid;
    private long crdate;
    private boolean income;
    private int version;
    private BSort sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getPayid() {
        return payid;
    }

    public void setPayid(int payid) {
        this.payid = payid;
    }

    public int getSortid() {
        return sortid;
    }

    public void setSortid(int sortid) {
        this.sortid = sortid;
    }

    public long getCrdate() {
        return crdate;
    }

    public void setCrdate(long crdate) {
        this.crdate = crdate;
    }

    public boolean isIncome() {
        return income;
    }

    public void setIncome(boolean income) {
        this.income = income;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public BSort getSort() {
        return sort;
    }

    public void setSort(BSort sort) {
        this.sort = sort;
    }

}
