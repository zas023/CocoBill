package com.copasso.cocobill.presenter;


import com.copasso.cocobill.base.BaseObserver;
import com.copasso.cocobill.base.RxPresenter;
import com.copasso.cocobill.model.bean.BaseBean;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.remote.MyUser;
import com.copasso.cocobill.model.repository.LocalRepository;
import com.copasso.cocobill.presenter.contract.LandContract;
import com.copasso.cocobill.presenter.contract.MonthListContract;
import com.copasso.cocobill.utils.BillUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Zhouas666 on 2019-01-08
 * Github: https://github.com/zas023
 */
public class MonthListPresenter extends RxPresenter<MonthListContract.View> implements MonthListContract.Presenter{

    private String TAG="MonthListPresenter";

    @Override
    public void getMonthList(String id, String year, String month) {
        LocalRepository.getInstance().getBBillByUserIdWithYM(id, year, month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BBill>>() {
                    @Override
                    protected void onSuccees(List<BBill> bBills) throws Exception {
                        mView.loadDataSuccess(BillUtils.packageDetailList(bBills));
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        mView.onFailure(e);
                    }
                });
    }

    @Override
    public void deleteBill(Long id) {
        LocalRepository.getInstance().deleteBBillById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Long>() {
                    @Override
                    protected void onSuccees(Long l) throws Exception {
                        mView.onSuccess();
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        mView.onFailure(e);
                    }
                });
    }

    @Override
    public void updateBill(BBill bBill) {
        LocalRepository.getInstance()
                .updateBBill(bBill)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BBill>() {
                    @Override
                    protected void onSuccees(BBill bBill) throws Exception {
                        mView.onSuccess();
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        mView.onFailure(e);
                    }
                });
    }
}
