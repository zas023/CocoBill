package com.copasso.cocobill.model.bean.local;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 账单分类bean
 */
@Entity
public class BSort{

    @Id
    private Long id;
    private String sortName;
    private String sortImg;
    private float cost;
    private Boolean income;

    @Generated(hash = 2092983496)
    public BSort() {
    }

    @Generated(hash = 1031921844)
    public BSort(Long id, String sortName, String sortImg, float cost,
            Boolean income) {
        this.id = id;
        this.sortName = sortName;
        this.sortImg = sortImg;
        this.cost = cost;
        this.income = income;
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

    public Boolean getIncome() {
        return income;
    }

    public void setIncome(Boolean income) {
        this.income = income;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}