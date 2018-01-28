package com.copasso.cocobill.view;

import com.copasso.cocobill.base.BaseView;
import com.copasso.cocobill.bean.BaseBean;
import com.copasso.cocobill.bean.NoteBean;

public interface BillView extends BaseView<BaseBean>{

    void loadDataSuccess(NoteBean tData);
}
