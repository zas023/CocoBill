package com.copasso.cocobill.mvp.view;

import com.copasso.cocobill.base.BaseView;
import com.copasso.cocobill.model.bean.packages.BPayBean;
import com.copasso.cocobill.model.bean.packages.BSortBean;
import com.copasso.cocobill.model.bean.packages.NoteBean;

public interface NoteView extends BaseView<NoteBean>{

    /**
     * 请求数据成功
     * @param tData
     */
    void loadDataSuccess(BSortBean tData);
    void loadDataSuccess(BPayBean tData);
}
