package com.copasso.cocobill.mvp.view;

import com.copasso.cocobill.base.BaseView;
import com.copasso.cocobill.model.bean.local.BPay;
import com.copasso.cocobill.model.bean.local.BSort;
import com.copasso.cocobill.model.bean.local.NoteBean;

public interface NoteView extends BaseView<NoteBean>{

    /**
     * 请求数据成功
     * @param tData
     */
    void loadDataSuccess(BSort tData);
    void loadDataSuccess(BPay tData);
}
