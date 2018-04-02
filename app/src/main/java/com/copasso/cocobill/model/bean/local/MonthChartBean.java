package com.copasso.cocobill.model.bean.local;

import com.copasso.cocobill.model.bean.BaseBean;

import java.util.List;

public class MonthChartBean extends BaseBean {
    float totalOut;    //总支出
    float totalIn;    //总收入
    List<SortTypeList> outSortlist;    //账单分类统计支出
    List<SortTypeList> inSortlist;    //账单分类统计收入

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
        this.inSortlist = inSortlist;
    }

    public static class SortTypeList {
        private String back_color;
        private float  money;    //此分类下的当月总收支
        private String sortName;  //此分类
        private String sortImg;
        private List<BBill> list;  //此分类下的当月账单

        public String getBack_color() {
            return back_color;
        }

        public void setBack_color(String back_color) {
            this.back_color = back_color;
        }

        public float getMoney() {
            return money;
        }

        public void setMoney(float money) {
            this.money = money;
        }

        public List<BBill> getList() {
            return list;
        }

        public void setList(List<BBill> list) {
            this.list = list;
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
    }
}
