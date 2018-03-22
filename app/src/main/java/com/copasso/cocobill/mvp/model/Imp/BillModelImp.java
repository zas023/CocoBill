package com.copasso.cocobill.mvp.model.Imp;

import com.copasso.cocobill.api.RetrofitFactory;
import com.copasso.cocobill.base.BaseObserver;
import com.copasso.cocobill.model.bean.BaseBean;
import com.copasso.cocobill.model.bean.packages.NoteBean;
import com.copasso.cocobill.mvp.model.BillModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BillModelImp implements BillModel{

    private BillOnListener listener;

    public BillModelImp(BillOnListener listener) {
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
    public void add(int userid, int sortid, int payid, String cost, String content, String crdate, boolean income) {
        RetrofitFactory.getInstence().API()
                .addBill(userid,sortid,payid,cost,content,crdate,income)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseBean>() {
                    @Override
                    protected void onSuccees(BaseBean baseBean) throws Exception {
                        listener.onSuccess(baseBean);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void update(int id, int userid, int sortid, int payid, String cost, String content, String crdate, boolean income) {
        RetrofitFactory.getInstence().API()
                .updateBill(id,userid,sortid,payid,cost,content,crdate,income)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseBean>() {
                    @Override
                    protected void onSuccees(BaseBean baseBean) throws Exception {
                        listener.onSuccess(baseBean);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void delete(int id) {
        RetrofitFactory.getInstence().API()
                .deleteBill(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseBean>() {
                    @Override
                    protected void onSuccees(BaseBean baseBean) throws Exception {
                        listener.onSuccess(baseBean);
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
    public interface BillOnListener {

        void onSuccess(BaseBean bean);
        void onSuccess(NoteBean bean);

        void onFailure(Throwable e);
    }
}
