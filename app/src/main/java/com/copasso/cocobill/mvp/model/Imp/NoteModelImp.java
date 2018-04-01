package com.copasso.cocobill.mvp.model.Imp;

import com.copasso.cocobill.api.RetrofitFactory;
import com.copasso.cocobill.base.BaseObserver;
import com.copasso.cocobill.model.bean.local.BPay;
import com.copasso.cocobill.model.bean.local.BSort;
import com.copasso.cocobill.model.bean.remote.BPayBean;
import com.copasso.cocobill.model.bean.remote.BSortBean;
import com.copasso.cocobill.model.bean.local.NoteBean;
import com.copasso.cocobill.model.local.LocalRepository;
import com.copasso.cocobill.mvp.model.NoteModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class NoteModelImp implements NoteModel{

    private NoteOnListener listener;

    public NoteModelImp(NoteOnListener listener) {
        this.listener = listener;
    }


    @Override
    public void getNote() {
        final NoteBean note=new NoteBean();
        LocalRepository.getInstance()
                .getBPay()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BPay>>() {
                    @Override
                    protected void onSuccees(List<BPay> bPays) throws Exception {
                        note.setPayinfo(bPays);
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
        LocalRepository.getInstance().getBSort(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BSort>>(){
                    @Override
                    protected void onSuccees(List<BSort> sorts) throws Exception {
                        note.setOutSortlis(sorts);
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
        LocalRepository.getInstance().getBSort(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BSort>>(){
                    @Override
                    protected void onSuccees(List<BSort> sorts) throws Exception {
                        note.setInSortlis(sorts);
                        listener.onSuccess(note);
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void addSort(BSort bSort) {

    }

    @Override
    public void addPay(BPay bPay) {

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
