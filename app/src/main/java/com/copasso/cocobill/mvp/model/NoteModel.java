package com.copasso.cocobill.mvp.model;

import com.copasso.cocobill.model.bean.local.BPay;
import com.copasso.cocobill.model.bean.local.BSort;

public interface NoteModel {

    void getNote();

    void addSort(BSort bSort);

    void addPay(BPay bPay);

    void onUnsubscribe();
}
