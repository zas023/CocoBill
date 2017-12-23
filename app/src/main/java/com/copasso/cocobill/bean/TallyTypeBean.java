package com.copasso.cocobill.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public class TallyTypeBean extends BaseBean {


    /**
     * total : 4.31
     * surplus : +2.31
     * scale : -99.90%
     * t_money : [{"affect_money":4.31,"type":"78","typename":"vip额外收益","back_color":"#6783fd"}]
     */

    private float total;
    private String surplus;
    private String scale;
    private List<TMoneyBean> t_money;

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public List<TMoneyBean> getT_money() {
        return t_money;
    }

    public void setT_money(List<TMoneyBean> t_money) {
        this.t_money = t_money;
    }

    public static class TMoneyBean {
        /**
         * affect_money : 4.31
         * type : 78
         * typename : vip额外收益
         * back_color : #6783fd
         */

        private float affect_money;
        private int type;
        private String typename;
        private String back_color;

        public float getAffect_money() {
            return affect_money;
        }

        public void setAffect_money(float affect_money) {
            this.affect_money = affect_money;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getBack_color() {
            return back_color;
        }

        public void setBack_color(String back_color) {
            this.back_color = back_color;
        }
    }
}
