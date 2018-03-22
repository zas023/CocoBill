package com.copasso.cocobill.mvp.view;

import com.copasso.cocobill.base.BaseView;
import com.copasso.cocobill.model.bean.BaseBean;
import com.copasso.cocobill.model.bean.packages.MonthDetailBean;

public interface MonthDetailView extends BaseView<MonthDetailBean>{

    /**
     * 删除成功
     * @param tData
     */
    void loadDataSuccess(BaseBean tData);
}
