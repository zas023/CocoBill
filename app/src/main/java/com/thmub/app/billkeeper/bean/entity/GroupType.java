package com.thmub.app.billkeeper.bean.entity;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

/**
 * Created by Enosh on 2020-03-29
 * Github: https://github.com/zas023
 * <p>
 * 账单金额统计
 */
public class GroupType {

    // 类型
    @ColumnInfo(name = "group_count")
    private int count;

    // 类型
    @ColumnInfo(name = "record_type")
    private int type;

    // 总量
    @ColumnInfo(name = "group_amount")
    private float amount;

    // 时间
    @ColumnInfo(name = "record_date")
    private long date;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Ignore
    public GroupType() {
    }

    public GroupType(int count, int type, float amount, long date) {
        this.count = count;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }
}
