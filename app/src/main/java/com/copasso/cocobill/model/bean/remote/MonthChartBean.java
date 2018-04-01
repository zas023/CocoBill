package com.copasso.cocobill.model.bean.remote;

import com.copasso.cocobill.model.bean.local.BSort;

import java.util.List;

public class MonthChartBean {
    String totalOut;    //总支出
    String totalIn;    //总收入
    String l_totalOut;  //上月总支出
    String l_totalIn;  //上月总收入
    List<SortTypeList> outSortlist;    //账单分类统计支出
    List<SortTypeList> inSortlist;    //账单分类统计收入

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

    public List<SortTypeList> getOutSortlist() {
        return outSortlist;
    }

    public void setOutSortlist(List<SortTypeList> outSortlist) {
        this.outSortlist = outSortlist;
    }

    public List<SortTypeList> getInSortlist() {
        return inSortlist;
    }

    public void setInSortlist(List<SortTypeList> inSortlist) {
        inSortlist = inSortlist;
    }

    public static class SortTypeList {
        private String back_color;
        private String  money;    //此分类下的当月总收支
        private BSort sort;    //此分类
        private List<BBillBean> list;  //此分类下的当月账单

        public String getBack_color() {
            return back_color;
        }

        public void setBack_color(String back_color) {
            this.back_color = back_color;
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

        public BSort getSort() {
            return sort;
        }

        public void setSort(BSort sort) {
            this.sort = sort;
        }
    }
}
