package com.thmub.app.billkeeper.bean.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.thmub.app.billkeeper.constant.Constant;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Enosh on 2020-03-18
 * Github: https://github.com/zas023
 * <p>
 * 账单记录
 */
@Entity
public class Record implements Serializable, MultiItemEntity {

    @Ignore
    private static final long serialVersionUID = -6009969719444427000L;

    /**
     * 数据表自增 ID
     */
    @PrimaryKey
    @ColumnInfo(name = "record_id")
    private Long id;

    // 账户 ID
    @Ignore
    @ColumnInfo(name = "record_uid")
    private long accountId;

    // 记录时间
    // 各个数据库提供时间函数不同，使用时间戳便于兼容
    @ColumnInfo(name = "record_date")
    private long recordDate;

    // 金额
    @ColumnInfo(name = "record_amount")
    private String amount;

    // 分类名称
    @ColumnInfo(name = "record_category_name")
    private String categoryName;

    // 分类图标资源
    @ColumnInfo(name = "record_category_res")
    private String categoryRes;

    // 备注
    @ColumnInfo(name = "record_comment")
    private String comment = "";

    // 同步 ID
    @Ignore
    private String rId = "";

    // 状态
    @ColumnInfo(name = "record_status")
    private int status;

    // 记录类型
    @ColumnInfo(name = "record_type")
    private int type = Constant.TYPE_OUTCOME;

    // item类型，多布局使用
    @Ignore
    private int itemType;

    @Ignore
    public Record(Long id, long recordDate, String amount, String categoryName, String categoryRes,
                  String comment, int status, int type) {
        this.id = id;
        this.recordDate = recordDate;
        this.amount = amount;
        this.categoryName = categoryName;
        this.categoryRes = categoryRes;
        this.comment = comment;
        this.status = status;
        this.type = type;
    }

    public Record() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(long recordDate) {
        this.recordDate = recordDate;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryRes() {
        return this.categoryRes;
    }

    public void setCategoryRes(String categoryRes) {
        this.categoryRes = categoryRes;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
