package com.copasso.cocobill.mvp.model.Imp;

import com.copasso.cocobill.api.RetrofitFactory;
import com.copasso.cocobill.base.BaseObserver;
import com.copasso.cocobill.bean.BPay2;
import com.copasso.cocobill.bean.BSort2;
import com.copasso.cocobill.bean.NoteBean;
import com.copasso.cocobill.mvp.model.NoteModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NoteModelImp implements NoteModel{

    private NoteOnListener listener;

    public NoteModelImp(NoteOnListener listener) {
        this.listener = listener;
    }


    @Override
    public void getNote(int id) {
        RetrofitFactory.getInstence().API()
                .getNote(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<NoteBean>() {
                    @Override
                    protected void onSuccees(NoteBean noteBean) throws Exception {
                        listener.onSuccess(noteBean);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void addSort(int userid, String sortName, String sortImg, boolean income) {
        RetrofitFactory.getInstence().API()
                .addSort(userid,sortName,sortImg,income)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BSort2>() {
                    @Override
                    protected void onSuccees(BSort2 bSort2) throws Exception {
                        listener.onSuccess(bSort2);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void addPay(int userid, String payName, String payImg, String payNum) {
        RetrofitFactory.getInstence().API()
                .addPay(userid,payName,payImg,payNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BPay2>() {
                    @Override
                    protected void onSuccees(BPay2 bPay2) throws Exception {
                        listener.onSuccess(bPay2);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void onUnsubscribe() {

    }

    /**
     * 回调接口
     */
    public interface NoteOnListener {

        void onSuccess(NoteBean bean);
        void onSuccess(BSort2 bean);
        void onSuccess(BPay2 bean);

        void onFailure(Throwable e);
    }
}
