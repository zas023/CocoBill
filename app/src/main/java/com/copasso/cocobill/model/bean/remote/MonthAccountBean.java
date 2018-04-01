package com.copasso.cocobill.model.bean.remote;

import com.copasso.cocobill.model.bean.local.BPay;

import java.util.List;

/**
 * 月账单报表信息
 */
public class MonthAccountBean {

    String totalOut;    //总支出
    String totalIn;    //总收入
    String l_totalOut;  //上月总支出
    String l_totalIn;  //上月总收入
    List<PayTypeListBean> list;    //账单分类统计支出

    public String getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(String totalOut) {
        this.totalOut = totalOut;
    }

    public String getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(String totalIn) {
        this.totalIn = totalIn;
    }

    public String getL_totalOut() {
        return l_totalOut;
    }

    public void setL_totalOut(String l_totalOut) {
        this.l_totalOut = l_totalOut;
    }

    public String getL_totalIn() {
        return l_totalIn;
    }

    public void setL_totalIn(String l_totalIn) {
        this.l_totalIn = l_totalIn;
    }

    public List<PayTypeListBean> getList() {
        return list;
    }

    public void setList(List<PayTypeListBean> list) {
        this.list = list;
    }

    public static class PayTypeListBean {
        BPay bPay;
        String outcome;
        String income;

        public BPay getbPay() {
            return bPay;
        }

        public void setbPay(BPay bPay) {
            this.bPay = bPay;
        }

        public String getOutcome() {
            return outcome;
        }

        public void setOutcome(String outcome) {
            this.outcome = outcome;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }
    }
}
