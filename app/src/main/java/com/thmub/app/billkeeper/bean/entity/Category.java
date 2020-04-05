package com.thmub.app.billkeeper.bean.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.thmub.app.billkeeper.constant.Constant;

import java.io.Serializable;

/**
 * Created by Enosh on 2020-03-14
 * Github: https://github.com/zas023
 * <p>
 * 账单分类Bean
 */
@Entity
public class Category implements Serializable {

    @Ignore
    private static final long serialVersionUID = 8417595442426451345L;

    @PrimaryKey
    @ColumnInfo(name = "category_id")
    private Long id;

    // 分类名称
    @ColumnInfo(name = "category_name")
    private String name;

    // 图标名称
    @ColumnInfo(name = "category_icon_res")
    private String iconRes;

    /**
     * 类型
     * 0：支出
     * 1：收入
     *
     * @see Constant#TYPE_OUTCOME
     * @see Constant#TYPE_INCOME
     */
    @ColumnInfo(name = "category_type")
    private int type;

    // 排序
    @ColumnInfo(name = "category_order")
    private int order;

    // 是否选择，用于UI
    @Ignore
    private boolean isSelected;

    public Category() {
    }

    @Ignore
    public Category(Long id, String name, String iconRes, int type, int order) {
        this.id = id;
        this.name = name;
        this.iconRes = iconRes;
        this.type = type;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconRes() {
        return iconRes;
    }

    public void setIconRes(String iconRes) {
        this.iconRes = iconRes;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
