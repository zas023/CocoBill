package com.copasso.cocobill.presenter;

import com.copasso.cocobill.base.BaseObserver;
import com.copasso.cocobill.base.RxPresenter;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.repository.LocalRepository;
import com.copasso.cocobill.presenter.contract.MonthChartContract;
import com.copasso.cocobill.utils.BillUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Zhouas666 on 2019-01-08
 * Github: https://github.com/zas023
 */
public class MonthChartPresenter extends RxPresenter<MonthChartContract.View> implements MonthChartContract.Presenter{

    private String TAG="MonthChartPresenter";

    @Override
    public void getMonthChart(String id, String year, String month) {
        LocalRepository.getInstance().getBBillByUserIdWithYM(id, year, month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BBill>>() {
                    @Override
                    protected void onSuccees(List<BBill> bBills) throws Exception {
                        mView.loadDataSuccess(BillUtils.packageChartList(bBills));
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        mView.onFailure(e);
                    }
                });
    }
}
