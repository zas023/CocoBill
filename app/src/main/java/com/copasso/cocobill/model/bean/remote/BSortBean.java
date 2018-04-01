package com.copasso.cocobill.model.bean.remote;

import com.copasso.cocobill.model.bean.local.BSort;
import com.copasso.cocobill.model.bean.BaseBean;

public class BSortBean extends BaseBean {
    private Integer id;

    private Integer uid;

    private String sortName;

    private String sortImg;

    private Integer priority;

    private Boolean income;

    public BSortBean(){
        super();
    }

    public BSortBean(BSort sort) {
        this.sortName = sort.getSortName();
        this.sortImg = sort.getSortImg();
        this.income = sort.getIncome();
    }

    public BSortBean(Integer uid, String sortName, String sortImg, Boolean income) {
        this.uid = uid;
        this.sortName = sortName;
        this.sortImg = sortImg;
        this.income = income;
    }
    public BSortBean(Integer id, Integer uid, String sortName, String sortImg, Integer priority, Boolean income) {
        this.id = id;
        this.uid = uid;
        this.sortName = sortName;
        this.sortImg = sortImg;
        this.priority = priority;
        this.income = income;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName == null ? null : sortName.trim();
    }

    public String getSortImg() {
        return sortImg;
    }

    public void setSortImg(String sortImg) {
        this.sortImg = sortImg == null ? null : sortImg.trim();
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getIncome() {
        return income;
    }

    public void setIncome(Boolean income) {
        this.income = income;
    }
}