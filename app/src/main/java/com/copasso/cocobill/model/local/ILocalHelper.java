package com.copasso.cocobill.model.local;

import com.copasso.cocobill.model.bean.BBill;
import com.copasso.cocobill.model.bean.BPay;
import com.copasso.cocobill.model.bean.BSort;
import io.reactivex.Observable;

import java.util.List;

/**
 * 本地数据操作接口
 */
public interface ILocalHelper {

    /**************************Sava****************************/
    void saveBBill(BBill bill);
    void saveBPay(BPay pay);
    void saveBSort(BSort sort);

    /*************************Get*****************************/
    BBill getBBillById(int id);
    Observable<List<BBill>> getBBillByUserId(int id);
    Observable<List<BBill>> getBBillByUserIdWithYM(int id,String year,String month);
    Observable<List<BBill>> getBBillByUserIdWithYMD(int id,String ymd);

    /*************************Update**************************/
    Observable<BBill> updateBBill(BBill bill);
    /*************************Delete**************************/


}
