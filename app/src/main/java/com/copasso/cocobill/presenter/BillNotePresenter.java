package com.copasso.cocobill.presenter;

import com.copasso.cocobill.base.RxPresenter;
import com.copasso.cocobill.model.bean.local.BSort;
import com.copasso.cocobill.model.repository.LocalRepository;
import com.copasso.cocobill.presenter.contract.BillNoteContract;

import java.util.List;


/**
 * Created by Zhouas666 on 2019-01-08
 * Github: https://github.com/zas023
 */
public class BillNotePresenter extends RxPresenter<BillNoteContract.View> implements BillNoteContract.Presenter {

    private String TAG = "BillNotePresenter";

    @Override
    public void getBillNote() {
        //此处采用同步的方式，防止账单分类出现白块
        mView.loadDataSuccess(LocalRepository.getInstance().getBillNote());
    }

    @Override
    public void updateBBsorts(List<BSort> items) {
        LocalRepository.getInstance().updateBSoers(items);
        mView.onSuccess();
    }

    @Override
    public void addBSort(BSort bSort) {
        LocalRepository.getInstance().saveBSort(bSort);
        mView.onSuccess();
    }

    @Override
    public void deleteBSortByID(Long id) {
        LocalRepository.getInstance().deleteBSortById(id);
        mView.onSuccess();
    }
}
