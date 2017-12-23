package com.copasso.cocobill.bean;

public class SortBean {
    /**
     * id : 72
     * uid : 0
     * sortName : 偿还费用
     * sortImg : changhuanfeiyong@2x.png
     * priority : 0
     * income : false
     */

    private int id;
    private int uid;
    private String sortName;
    private String sortImg;
    private int priority;
    private boolean income;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortImg() {
        return sortImg;
    }

    public void setSortImg(String sortImg) {
        this.sortImg = sortImg;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isIncome() {
        return income;
    }

    public void setIncome(boolean income) {
        this.income = income;
    }
}