package com.copasso.cocobill.model.bean.local;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 支付方式bean
 */
@Entity
public class BPay {

    @Id
    private Long id;
    private String payName;
    private String payImg;

    private float income;
    private float outcome;

    @Generated(hash = 48271616)
    public BPay() {
    }

    @Generated(hash = 572465971)
    public BPay(Long id, String payName, String payImg, float income,
                float outcome) {
        this.id = id;
        this.payName = payName;
        this.payImg = payImg;
        this.income = income;
        this.outcome = outcome;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayImg() {
        return payImg;
    }

    public void setPayImg(String payImg) {
        this.payImg = payImg;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public float getOutcome() {
        return outcome;
    }

    public void setOutcome(float outcome) {
        this.outcome = outcome;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}