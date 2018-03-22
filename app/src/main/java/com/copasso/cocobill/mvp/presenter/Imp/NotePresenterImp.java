package com.copasso.cocobill.mvp.presenter.Imp;

import com.copasso.cocobill.model.bean.packages.BPayBean;
import com.copasso.cocobill.model.bean.packages.BSortBean;
import com.copasso.cocobill.model.bean.packages.NoteBean;
import com.copasso.cocobill.mvp.model.Imp.NoteModelImp;
import com.copasso.cocobill.mvp.model.NoteModel;
import com.copasso.cocobill.mvp.presenter.NotePresenter;
import com.copasso.cocobill.mvp.view.NoteView;

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
    public void onSuccess(BSortBean bean) {
        view.loadDataSuccess(bean);
    }

    @Override
    public void onSuccess(BPayBean bean) {
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
