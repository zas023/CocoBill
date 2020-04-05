package com.thmub.app.billkeeper.bean.entity;

import androidx.room.ColumnInfo;

/**
 * Created by Enosh on 2020-03-29
 * Github: https://github.com/zas023
 * <p>
 * 账单分组——类型
 */
public class GroupCategory {

    @ColumnInfo(name = "record_type")
    private int type;

    // 该分类记录数量
    @ColumnInfo(name = "group_count")
    private long count;

    // 总金额
    @ColumnInfo(name = "group_amount")
    private double amount;

    // 分类名称
    @ColumnInfo(name = "record_category_name")
    private String categoryName;

    // 分类图标资源
    @ColumnInfo(name = "record_category_res")
    private String categoryRes;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryRes() {
        return categoryRes;
    }

    public void setCategoryRes(String categoryRes) {
        this.categoryRes = categoryRes;
    }
}
