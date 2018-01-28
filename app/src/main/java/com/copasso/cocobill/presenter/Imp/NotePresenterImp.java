package com.copasso.cocobill.presenter.Imp;

import com.copasso.cocobill.bean.BPay2;
import com.copasso.cocobill.bean.BSort2;
import com.copasso.cocobill.bean.BaseBean;
import com.copasso.cocobill.bean.NoteBean;
import com.copasso.cocobill.model.BillModel;
import com.copasso.cocobill.model.Imp.BillModelImp;
import com.copasso.cocobill.model.Imp.NoteModelImp;
import com.copasso.cocobill.model.NoteModel;
import com.copasso.cocobill.presenter.BillPresenter;
import com.copasso.cocobill.presenter.NotePresenter;
import com.copasso.cocobill.view.BillView;
import com.copasso.cocobill.view.NoteView;

public class NotePresenterImp extends NotePresenter implements NoteModelImp.NoteOnListener{

    private NoteModel model;
    private NoteView view;

    public NotePresenterImp(NoteView view) {
        this.model=new NoteModelImp(this);
        this.view = view;
    }

    @Override
    public void onSuccess(NoteBean bean) {
        view.loadDataSuccess(bean);
    }

    @Override
    public void onSuccess(BSort2 bean) {
        view.loadDataSuccess(bean);
    }

    @Override
    public void onSuccess(BPay2 bean) {
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
    public void addSort(int userid, String sortName, String sortImg, boolean income) {
        model.addSort(userid,sortName,sortImg,income);
    }

    @Override
    public void addPay(int userid, String payName, String payImg, String payNum) {
        model.addPay(userid,payName,payImg,payNum);
    }

    @Override
    public void delete(int id) {

    }
}
