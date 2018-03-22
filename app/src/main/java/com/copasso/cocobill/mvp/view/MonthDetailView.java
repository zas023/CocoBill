package com.copasso.cocobill.mvp.view;

import com.copasso.cocobill.base.BaseView;
import com.copasso.cocobill.bean.BaseBean;
import com.copasso.cocobill.bean.MonthDetailBean;

public interface MonthDetailView extends BaseView<MonthDetailBean>{

    /**
     * 删除成功
     * @param tData
     */
    void loadDataSuccess(BaseBean tData);
}
