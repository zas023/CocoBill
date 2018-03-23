package com.copasso.cocobill.model.local;

import com.copasso.cocobill.model.bean.BBill;
import com.copasso.cocobill.model.bean.BPay;
import com.copasso.cocobill.model.bean.BSort;
import com.copasso.cocobill.model.gen.BBillDao;
import com.copasso.cocobill.model.gen.DaoSession;
import com.copasso.cocobill.utils.DateUtils;
import freemarker.template.utility.DateUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

public class LocalRepository implements ILocalHelper{

    private static final String TAG = "LocalRepository";
    private static final String DISTILLATE_ALL = "normal";
    private static final String DISTILLATE_BOUTIQUES = "distillate";

    private static volatile LocalRepository sInstance;
    private DaoSession mSession;
    private LocalRepository(){
        mSession = DaoDbHelper.getInstance().getSession();
    }

    public static LocalRepository getInstance(){
        if (sInstance == null){
            synchronized (LocalRepository.class){
                if (sInstance == null){
                    sInstance = new LocalRepository();
                }
            }
        }
        return sInstance;
    }

    /**************************Sava****************************/
    @Override
    public void saveBBill(BBill bill) {
        mSession.getBBillDao().insert(bill);
    }

    @Override
    public void saveBPay(BPay pay) {
        mSession.getBPayDao().insert(pay);
    }

    @Override
    public void saveBSort(BSort sort) {
        mSession.getBSortDao().insert(sort);
    }

    @Override
    public BBill getBBillById(int id) {
        List<BBill> queryList= mSession.getBBillDao().queryBuilder()
                .where(BBillDao.Properties.Id.eq(id)).list();
        if (null==queryList)
            return null;
        return queryList.get(0);
    }

    @Override
    public Observable<List<BBill>> getBBillByUserId(int id) {
        QueryBuilder<BBill> queryBuilder=mSession.getBBillDao()
                .queryBuilder()
                .where(BBillDao.Properties.Userid.eq(id));
        return queryToRx(queryBuilder);
    }

    @Override
    public Observable<List<BBill>> getBBillByUserIdWithYM(int id, String year, String month) {
        String startStr=year+"-"+month+"-00 00:00:00";
        Date date=DateUtils.str2Date(startStr);
        Date endDate=DateUtils.addMonth(date,1);
        QueryBuilder<BBill> queryBuilder=mSession.getBBillDao()
                .queryBuilder()
                .where(BBillDao.Properties.Userid.eq(id))
                .where(BBillDao.Properties.Crdate.between(DateUtils.getMillis(date),DateUtils.getMillis(endDate)));
        return queryToRx(queryBuilder);
    }

    @Override
    public Observable<List<BBill>> getBBillByUserIdWithYMD(int id, String ymd) {
        return null;
    }

    @Override
    public Observable<BBill> updateBBill(BBill bill) {
        return null;
    }

    private <T> Observable<List<T>> queryToRx(final QueryBuilder<T> builder){
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
