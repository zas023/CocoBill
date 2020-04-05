package com.thmub.app.billkeeper.bean.entity;

import androidx.room.ColumnInfo;

/**
 * Created by Enosh on 2020-03-27
 * Github: https://github.com/zas023
 * <p>
 * 账单分组——每日
 */
public class GroupDaily {

    // 数量
    @ColumnInfo(name = "record_count")
    private long count;

    // 时间
    @ColumnInfo(name = "record_date")
    private long date;

    // 总金额
    @ColumnInfo(name = "record_amount")
    private float amount;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
