package com.copasso.cocobill.mvp.model.Imp;

import com.copasso.cocobill.api.RetrofitFactory;
import com.copasso.cocobill.base.BaseObserver;
import com.copasso.cocobill.model.bean.packages.BPayBean;
import com.copasso.cocobill.model.bean.packages.BSortBean;
import com.copasso.cocobill.model.bean.packages.NoteBean;
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
                .subscribe(new BaseObserver<BSortBean>() {
                    @Override
                    protected void onSuccees(BSortBean bSortBean) throws Exception {
                        listener.onSuccess(bSortBean);
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
                .subscribe(new BaseObserver<BPayBean>() {
                    @Override
                    protected void onSuccees(BPayBean bPayBean) throws Exception {
                        listener.onSuccess(bPayBean);
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
        void onSuccess(BSortBean bean);
        void onSuccess(BPayBean bean);

        void onFailure(Throwable e);
    }
}
