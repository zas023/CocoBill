package com.copasso.cocobill.model.bean.remote;

import java.util.List;

public class MonthDetailBean {

    private String t_income;
    private String t_outcome;
    private String l_outcome;
    private List<DaylistBean> daylist;

    public String getT_income() {
        return t_income;
    }

    public void setT_income(String t_income) {
        this.t_income = t_income;
    }

    public String getT_outcome() {
        return t_outcome;
    }

    public void setT_outcome(String t_outcome) {
        this.t_outcome = t_outcome;
    }

    public String getL_outcome() {
        return l_outcome;
    }

    public void setL_outcome(String l_outcome) {
        this.l_outcome = l_outcome;
    }

    public List<DaylistBean> getDaylist() {
        return daylist;
    }

    public void setDaylist(List<DaylistBean> daylist) {
        this.daylist = daylist;
    }

    public static class DaylistBean {

        private String time;
        private String money;
        private List<BBillBean> list;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public List<BBillBean> getList() {
            return list;
        }

        public void setList(List<BBillBean> list) {
            this.list = list;
        }

    }
}
