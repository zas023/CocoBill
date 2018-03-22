package com.copasso.cocobill.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 账单bean
 */
@Entity
public class BBill {

    @Id
    private int id;
    private double cost;
    private String content;
    private int userid;
    private int payid;
    private int sortid;
    private long crdate;
    private boolean income;
    private int version;

    @Generated(hash = 782767181)
    public BBill(int id, double cost, String content, int userid, int payid,
            int sortid, long crdate, boolean income, int version) {
        this.id = id;
        this.cost = cost;
        this.content = content;
        this.userid = userid;
        this.payid = payid;
        this.sortid = sortid;
        this.crdate = crdate;
        this.income = income;
        this.version = version;
    }

    @Generated(hash = 124482664)
    public BBill() {
    }

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

    public boolean getIncome() {
        return this.income;
    }
}
