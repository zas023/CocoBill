package com.copasso.cocobill.model.bean.local;

import com.copasso.cocobill.model.bean.BaseBean;

import java.util.List;

public class NoteBean extends BaseBean {

    private List<BSort> outSortlis;
    private List<BSort> inSortlis;
    private List<BPay> payinfo;

    public List<BSort> getOutSortlis() {
        return outSortlis;
    }

    public void setOutSortlis(List<BSort> outSortlis) {
        this.outSortlis = outSortlis;
    }

    public List<BSort> getInSortlis() {
        return inSortlis;
    }

    public void setInSortlis(List<BSort> inSortlis) {
        this.inSortlis = inSortlis;
    }

    public List<BPay> getPayinfo() {
        return payinfo;
    }

    public void setPayinfo(List<BPay> payinfo) {
        this.payinfo = payinfo;
    }
}
