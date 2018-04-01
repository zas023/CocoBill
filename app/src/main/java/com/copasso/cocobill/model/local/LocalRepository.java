package com.copasso.cocobill.model.local;

import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.local.BPay;
import com.copasso.cocobill.model.bean.local.BSort;
import com.copasso.cocobill.model.bean.local.NoteBean;
import com.copasso.cocobill.model.gen.BBillDao;
import com.copasso.cocobill.model.gen.BSortDao;
import com.copasso.cocobill.model.gen.DaoSession;
import com.copasso.cocobill.utils.DateUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

public class LocalRepository {

    private static final String TAG = "LocalRepository";
    private static final String DISTILLATE_ALL = "normal";
    private static final String DISTILLATE_BOUTIQUES = "distillate";

    private static volatile LocalRepository sInstance;
    private DaoSession mSession;

    private LocalRepository() {
        mSession = DaoDbHelper.getInstance().getSession();
    }

    public static LocalRepository getInstance() {
        if (sInstance == null) {
            synchronized (LocalRepository.class) {
                if (sInstance == null) {
                    sInstance = new LocalRepository();
                }
            }
        }
        return sInstance;
    }

    /******************************save**************************************/
    public Long saveBBill(BBill bill) {
        return mSession.getBBillDao().insert(bill);
    }

    public Long saveBPay(BPay pay) {
        return mSession.getBPayDao().insert(pay);
    }

    public Long saveBSort(BSort sort) {
        return mSession.getBSortDao().insert(sort);
    }

    /**
     * 批量添加支付方式
     *
     * @param pays
     */
    public void saveBPays(List<BPay> pays) {
        for (BPay pay : pays)
            saveBPay(pay);
    }

    /**
     * 批量添加账单分类
     *
     * @param sorts
     */
    public void saveBsorts(List<BSort> sorts) {
        for (BSort sort : sorts)
            saveBSort(sort);
    }

    /******************************get**************************************/
    public BBill getBBillById(int id) {
        List<BBill> queryList = mSession.getBBillDao().queryBuilder()
                .where(BBillDao.Properties.Id.eq(id)).list();
        if (null == queryList)
            return null;
        return queryList.get(0);
    }

    public Observable<List<BBill>> getBBillByUserId(int id) {
        QueryBuilder<BBill> queryBuilder = mSession.getBBillDao()
                .queryBuilder()
                .where(BBillDao.Properties.Userid.eq(id));
        return queryListToRx(queryBuilder);
    }

    public Observable<List<BBill>> getBBillByUserIdWithYM(int id, String year, String month) {
        String startStr = year + "-" + month + "-00 00:00:00";
        Date date = DateUtils.str2Date(startStr);
        Date endDate = DateUtils.addMonth(date, 1);
        QueryBuilder<BBill> queryBuilder = mSession.getBBillDao()
                .queryBuilder()
                .where(BBillDao.Properties.Userid.eq(id))
                .where(BBillDao.Properties.Crdate.between(DateUtils.getMillis(date), DateUtils.getMillis(endDate)))
                .orderDesc(BBillDao.Properties.Crdate);
        return queryListToRx(queryBuilder);
    }

    public Observable<List<BSort>> getBSort(boolean income){
        QueryBuilder<BSort> queryBuilder = mSession.getBSortDao()
                .queryBuilder()
                .where(BSortDao.Properties.Income.eq(income));
        return queryListToRx(queryBuilder);
    }

    public Observable<List<BSort>> getBSort(){
        QueryBuilder<BSort> queryBuilder = mSession.getBSortDao()
                .queryBuilder();
        return queryListToRx(queryBuilder);
    }

    public Observable<List<BPay>> getBPay(){
        QueryBuilder<BPay> queryBuilder = mSession.getBPayDao()
                .queryBuilder();
        return queryListToRx(queryBuilder);
    }


    /******************************update**************************************/
    public Observable<BBill> updateBBill(BBill bill) {
        return null;
    }

    /******************************delete**************************************/
    public void deleteBBillById(Long id) {
        mSession.getBBillDao().deleteByKey(id);
    }

    /******************************Rx**************************************/
    private <T> Observable<T> queryToRx(final QueryBuilder<T> builder) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                T data = builder.list().get(0);
                e.onNext(data);
                e.onComplete();
            }
        });
    }

    private <T> Observable<List<T>> queryListToRx(final QueryBuilder<T> builder) {
        return Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                List<T> data = builder.list();
                e.onNext(data);
                e.onComplete();
            }
        });
    }

}
