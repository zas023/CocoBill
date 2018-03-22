package com.copasso.cocobill.mvp.presenter.Imp;

import com.copasso.cocobill.model.bean.BaseBean;
import com.copasso.cocobill.model.bean.packages.NoteBean;
import com.copasso.cocobill.mvp.model.BillModel;
import com.copasso.cocobill.mvp.model.Imp.BillModelImp;
import com.copasso.cocobill.mvp.presenter.BillPresenter;
import com.copasso.cocobill.mvp.view.BillView;

public class BillPresenterImp extends BillPresenter implements BillModelImp.BillOnListener{

    private BillModel model;
    private BillView view;

    public BillPresenterImp(BillView view) {
        this.model=new BillModelImp(this);
        this.view = view;
    }

    @Override
    public void onSuccess(BaseBean bean) {
        view.loadDataSuccess(bean);
    }

    @Override
    public void onSuccess(NoteBean bean) {
        view.loadDataSuccess(bean);
    }

    @Override
    public void onFailure(Throwable e) {
        view.loadDataError(e);
    }

    @Override
    public void getNote(int id) {
        model.getNote(id);
    }

    @Override
    public void add(int userid, int sortid, int payid, String cost, String content, String crdate, boolean income) {
        model.add(userid,sortid,payid,cost,content,crdate,income);
    }

    @Override
    public void update(int id, int userid, int sortid, int payid, String cost, String content, String crdate, boolean income) {
        model.update(id,userid,sortid,payid,cost,content,crdate,income);
    }

    @Override
    public void delete(int id) {
        model.delete(id);
    }
}
