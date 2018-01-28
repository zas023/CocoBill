package com.copasso.cocobill.view;

import com.copasso.cocobill.base.BaseView;
import com.copasso.cocobill.bean.BPay2;
import com.copasso.cocobill.bean.BSort2;
import com.copasso.cocobill.bean.NoteBean;
import com.copasso.cocobill.bean.UserBean;

public interface NoteView extends BaseView<NoteBean>{

    /**
     * 请求数据成功
     * @param tData
     */
    void loadDataSuccess(BSort2 tData);
    void loadDataSuccess(BPay2 tData);
}
