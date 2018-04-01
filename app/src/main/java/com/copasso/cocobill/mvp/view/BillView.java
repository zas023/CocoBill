package com.copasso.cocobill.mvp.view;

import com.copasso.cocobill.base.BaseView;
import com.copasso.cocobill.model.bean.BaseBean;
import com.copasso.cocobill.model.bean.local.NoteBean;

public interface BillView extends BaseView<BaseBean>{

    void loadDataSuccess(NoteBean tData);
}
