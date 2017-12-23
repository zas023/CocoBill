package com.copasso.cocobill.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/16 0016.
 */

public class TallyAccountBean extends BaseBean{


    /**
     * total_in : 0.00
     * total_out : .00
     * list : [{"type":"1","img":"/UF/Uploads/Noteimg/cash@2x.png","name":"现金","num":"","income":"+0.00","outcome":"-0.00"},{"type":"3_17","img":"/UF/Uploads/Noteimg/bank@2x.png","name":"银行卡","num":"尾号3333","income":"+0.00","outcome":"-0.00"},{"type":"4","img":"/UF/Uploads/Noteimg/other@2x.png","name":"未分类","num":"","income":"+0.00","outcome":"-0.00"}]
     */

    private String total_in;
    private String total_out;
    private List<ListBean> list;

    public String getTotal_in() {
        return total_in;
    }

    public void setTotal_in(String total_in) {
        this.total_in = total_in;
    }

    public String getTotal_out() {
        return total_out;
    }

    public void setTotal_out(String total_out) {
        this.total_out = total_out;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * type : 1
         * img : /UF/Uploads/Noteimg/cash@2x.png
         * name : 现金
         * num :
         * income : +0.00
         * outcome : -0.00
         */

        private String type;
        private String img;
        private String name;
        private String num;
        private String income;
        private String outcome;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getOutcome() {
            return outcome;
        }

        public void setOutcome(String outcome) {
            this.outcome = outcome;
        }
    }
}
