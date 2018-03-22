package com.copasso.cocobill.model.bean;

public class test {

    /**
     * status : 100
     * message : 处理成功！
     * data : {"bill":{"id":82,"cost":555,"content":"test","userid":1,"payid":3,"sortid":2,"crdate":1514379507000,"income":false,"sort":null}}
     */

    private int status;
    private String message;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bill : {"id":82,"cost":555,"content":"test","userid":1,"payid":3,"sortid":2,"crdate":1514379507000,"income":false,"sort":null}
         */

        private BillBean bill;

        public BillBean getBill() {
            return bill;
        }

        public void setBill(BillBean bill) {
            this.bill = bill;
        }

        public static class BillBean {
            /**
             * id : 82
             * cost : 555.0
             * content : test
             * userid : 1
             * payid : 3
             * sortid : 2
             * crdate : 1514379507000
             * income : false
             * sort : null
             */

            private int id;
            private double cost;
            private String content;
            private int userid;
            private int payid;
            private int sortid;
            private long crdate;
            private boolean income;
            private Object sort;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getCost() {
                return cost;
            }

            public void setCost(double cost) {
                this.cost = cost;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public int getPayid() {
                return payid;
            }

            public void setPayid(int payid) {
                this.payid = payid;
            }

            public int getSortid() {
                return sortid;
            }

            public void setSortid(int sortid) {
                this.sortid = sortid;
            }

            public long getCrdate() {
                return crdate;
            }

            public void setCrdate(long crdate) {
                this.crdate = crdate;
            }

            public boolean isIncome() {
                return income;
            }

            public void setIncome(boolean income) {
                this.income = income;
            }

            public Object getSort() {
                return sort;
            }

            public void setSort(Object sort) {
                this.sort = sort;
            }
        }
    }
}
