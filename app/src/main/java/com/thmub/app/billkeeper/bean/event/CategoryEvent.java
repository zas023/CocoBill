package com.thmub.app.billkeeper.bean.event;

/**
 * Created by Enosh on 2020-03-21
 * Github: https://github.com/zas023
 * <p>
 * 账单选择事件
 */
public class CategoryEvent {

    private String categoryName;
    private int categoryRes;

    public CategoryEvent() {

    }

    public CategoryEvent(String categoryName, int categoryRes) {
        this.categoryName = categoryName;
        this.categoryRes = categoryRes;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryRes() {
        return categoryRes;
    }

    public void setCategoryRes(int categoryRes) {
        this.categoryRes = categoryRes;
    }
}
