package com.copasso.cocobill.base;

/**
 * 视图基类
 * @param <T>
 */
public interface BaseView<T> {

    /**
     * 请求数据成功
     * @param tData
     */
    void loadDataSuccess(T tData);

    /**
     * 请求数据错误
     * @param throwable
     */
    void loadDataError(Throwable throwable);
}
