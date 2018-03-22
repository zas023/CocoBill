package com.copasso.cocobill.mvp.view;

import com.copasso.cocobill.base.BaseView;
import com.copasso.cocobill.model.bean.BPay2;
import com.copasso.cocobill.model.bean.BSort2;
import com.copasso.cocobill.model.bean.NoteBean;

public interface NoteView extends BaseView<NoteBean>{

    /**
     * 请求数据成功
     * @param tData
     */
    void loadDataSuccess(BSort2 tData);
    void loadDataSuccess(BPay2 tData);
}
