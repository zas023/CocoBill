package com.copasso.cocobill.mvp.model.Imp;

import com.copasso.cocobill.base.BaseObserver;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.BaseBean;
import com.copasso.cocobill.model.bean.local.BPay;
import com.copasso.cocobill.model.bean.local.BSort;
import com.copasso.cocobill.model.bean.local.NoteBean;
import com.copasso.cocobill.model.repository.LocalRepository;
import com.copasso.cocobill.mvp.model.BillModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class BillModelImp implements BillModel{

    private BillOnListener listener;

    public BillModelImp(BillOnListener listener) {
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
    public void add(BBill bBill) {
        LocalRepository.getInstance().saveBBill(bBill)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BBill>() {
                    @Override
                    protected void onSuccees(BBill bBill) throws Exception {
                        listener.onSuccess(new BaseBean());
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void update(BBill bBill) {
        LocalRepository.getInstance()
                .updateBBill(bBill)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BBill>() {
                    @Override
                    protected void onSuccees(BBill bBill) throws Exception {
                        listener.onSuccess(new BaseBean());
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void delete(Long id) {
        LocalRepository.getInstance()
                .deleteBBillById(id);
    }

    @Override
    public void onUnsubscribe() {

    }

    /**
     * 回调接口
     */
    public interface BillOnListener {
        void onSuccess(BaseBean bean);
        void onSuccess(NoteBean bean);
        void onFailure(Throwable e);
    }
}
