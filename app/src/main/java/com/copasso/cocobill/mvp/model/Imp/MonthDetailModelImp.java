package com.copasso.cocobill.mvp.model.Imp;

import com.copasso.cocobill.api.RetrofitFactory;
import com.copasso.cocobill.base.BaseObserver;
import com.copasso.cocobill.model.bean.BBill;
import com.copasso.cocobill.model.bean.BaseBean;
import com.copasso.cocobill.model.bean.packages.MonthDetailBean;
import com.copasso.cocobill.model.local.LocalRepository;
import com.copasso.cocobill.mvp.model.MonthDetailModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class MonthDetailModelImp implements MonthDetailModel {

    private MonthDetailOnListener listener;

    public MonthDetailModelImp(MonthDetailOnListener listener) {
        this.listener = listener;
    }


    @Override
    public void getMonthDetailBills(String id, String year, String month) {
        RetrofitFactory.getInstence().API()
                .getMonthDetial(id, year, month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<MonthDetailBean>() {
                    @Override
                    protected void onSuccees(MonthDetailBean monthDetailBean) throws Exception {
                        listener.onSuccess(monthDetailBean);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        listener.onFailure(e);
                    }
                });
    }

    @Override
    public void getLocalMonthDetailBills(int id, String year, String month) {
        LocalRepository.getInstance().getBBillByUserIdWithYM(id, year, month)
                .subscribe(new BaseObserver<List<BBill>>() {
                    @Override
                    protected void onSuccees(List<BBill> bBills) throws Exception {
                        listener.onSuccess(bBills);
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
    public interface MonthDetailOnListener {

        void onSuccess(MonthDetailBean bean);

        void onSuccess(List<BBill> list);

        void onSuccess(BaseBean bean);

        void onFailure(Throwable e);
    }
}
