package com.copasso.cocobill.mvp.view;

import com.copasso.cocobill.base.BaseView;
import com.copasso.cocobill.model.bean.BBill;
import com.copasso.cocobill.model.bean.BaseBean;
import com.copasso.cocobill.model.bean.packages.MonthDetailBean;

import java.util.List;

public interface MonthDetailView extends BaseView<MonthDetailBean>{

    /**
     * 本地当月账单
     * @param list
     */
    void loadDataSuccess(List<BBill> list);
    /**
     * 删除成功
     * @param tData
     */
    void loadDataSuccess(BaseBean tData);
}
