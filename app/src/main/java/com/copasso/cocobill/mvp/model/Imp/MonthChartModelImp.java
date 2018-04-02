package com.copasso.cocobill.mvp.model.Imp;

import com.copasso.cocobill.base.BaseObserver;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.local.MonthChartBean;
import com.copasso.cocobill.model.repository.LocalRepository;
import com.copasso.cocobill.mvp.model.MonthChartModel;
import com.copasso.cocobill.utils.BillUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class MonthChartModelImp implements MonthChartModel {

    private MonthChartOnListener listener;

    public MonthChartModelImp(MonthChartOnListener listener) {
        this.listener = listener;
    }

    @Override
    public void getMonthChartBills(int id, String year, String month) {
        LocalRepository.getInstance().getBBillByUserIdWithYM(id, year, month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BBill>>() {
                    @Override
                    protected void onSuccees(List<BBill> bBills) throws Exception {
                        listener.onSuccess(BillUtils.packageChartList(bBills));
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
    public interface MonthChartOnListener {

        void onSuccess(MonthChartBean bean);

        void onFailure(Throwable e);
    }
}
