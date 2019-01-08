package com.copasso.cocobill.model.bean.local;

import com.copasso.cocobill.model.bean.BaseBean;

import java.util.List;

/**
 * 月账单报表信息
 */
public class MonthAccountBean extends BaseBean {

    float totalOut;    //总支出
    float totalIn;    //总收入
    List<PayTypeListBean> list;    //账单分类统计支出

    public float getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(float totalOut) {
        this.totalOut = totalOut;
    }

    public float getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(float totalIn) {
        this.totalIn = totalIn;
    }

    public List<PayTypeListBean> getList() {
        return list;
    }

    public void setList(List<PayTypeListBean> list) {
        this.list = list;
    }

    public static class PayTypeListBean {
        String payName;
        String payImg;
        float outcome;
        float income;
        List<BBill> Bills;

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

        public float getOutcome() {
            return outcome;
        }

        public void setOutcome(float outcome) {
            this.outcome = outcome;
        }

        public float getIncome() {
            return income;
        }

        public void setIncome(float income) {
            this.income = income;
        }

        public List<BBill> getBills() {
            return Bills;
        }

        public void setBills(List<BBill> bills) {
            Bills = bills;
        }
    }
}
