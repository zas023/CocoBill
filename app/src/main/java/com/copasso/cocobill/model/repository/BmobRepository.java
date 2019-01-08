package com.copasso.cocobill.model.repository;

import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.remote.CoBill;
import com.copasso.cocobill.model.event.SyncEvent;
import com.copasso.cocobill.utils.BillUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;

public class BmobRepository {

    private static final String TAG = "BmobRepository";

    private static volatile BmobRepository sInstance;

    private BmobRepository() {
    }

    public static BmobRepository getInstance() {
        if (sInstance == null) {
            synchronized (BmobRepository.class) {
                if (sInstance == null) {
                    sInstance = new BmobRepository();
                }
            }
        }
        return sInstance;
    }

    /**********************批量操作***************************/
    /**
     * 批量上传账单
     *
     * @param list
     */
    public void saveBills(List<BmobObject> list, final List<BBill> listB) {
        new BmobBatch().insertBatch(list).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if (e == null) {
                    for (int i = 0, n = o.size(); i < n; i++) {
                        if (o.get(i).isSuccess()) {
                            //上传成功后更新本地账单，否则会重复同步
                            BBill bBill = listB.get(i);
                            bBill.setRid(o.get(i).getObjectId());
                            LocalRepository.getInstance().updateBBillByBmob(bBill);
                        }
                    }

                }
            }
        });
    }

    /**
     * 批量更新账单
     *
     * @param list
     */
    public void updateBills(List<BmobObject> list) {

        new BmobBatch().updateBatch(list).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if (e == null) {

                }
            }
        });
    }

    /**
     * 批量更新账单
     *
     * @param list
     */
    public void deleteBills(List<BmobObject> list) {

        new BmobBatch().deleteBatch(list).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if (e == null) {

                }
            }
        });
    }

    /**************************同步账单******************************/
    /**
     * 同步账单
     */
    public void syncBill(String userid) {

        BmobQuery<CoBill> query = new BmobQuery<>();
        query.addWhereEqualTo("userid", userid);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<CoBill>() {
            @Override
            public void done(List<CoBill> object, BmobException e) {
                if (e == null) {
                    List<BBill> bBills = LocalRepository.getInstance().getBBills();
                    //需要上传的账单
                    List<BmobObject> listUpload = new ArrayList<>();
                    List<BBill> listBBillUpdate = new ArrayList<>();
                    //需要更新的账单
                    List<BmobObject> listUpdate = new ArrayList<>();
                    //需要删除的账单
                    List<BmobObject> listDelete = new ArrayList<>();

                    HashMap<String, BBill> bMap = new HashMap<>();


                    for (BBill bBill : bBills) {
                        if (bBill.getRid() == null) {
                            //服务器端id为空，则表示为上传
                            listUpload.add(new CoBill(bBill));
                            //以便账单成功上传后更新本地数据
                            listBBillUpdate.add(bBill);
                        } else
                            bMap.put(bBill.getRid(), bBill);
                    }

                    HashMap<String, CoBill> cMap = new HashMap<>();
                    //服务器账单==》键值对
                    for (CoBill coBill : object) {
                        cMap.put(coBill.getObjectId(), coBill);
                    }

                    List<BBill> listsave = new ArrayList<>();
                    List<BBill> listdelete = new ArrayList<>();
                    for (Map.Entry<String, BBill> entry : bMap.entrySet()) {
                        String rid = entry.getKey();
                        BBill bBill=entry.getValue();
                        if (cMap.containsKey(rid)) {
                            if (bBill.getVersion() < 0) {
                                //需要删除的账单
                                listDelete.add(new CoBill(bBill));
                                listdelete.add(bBill);
                            } else {
                                //服务器端数据过期
                                if (bBill.getVersion()>cMap.get(rid).getVersion()) {
                                    listUpdate.add(new CoBill(bBill));
                                }
                            }
                            cMap.remove(rid);
                        }
                    }
                    //提交服务器数据的批量操作
                    if(!listUpload.isEmpty()) saveBills(listUpload,listBBillUpdate);
                    if(!listUpdate.isEmpty()) updateBills(listUpdate);
                    if(!listDelete.isEmpty()) deleteBills(listDelete);

                    //CoBill==》BBill
                    for (Map.Entry<String, CoBill> entry : cMap.entrySet()) {
                        //需要保存到本地的账单
                        listsave.add(BillUtils.toBBill(entry.getValue()));
                    }
                    //向本地数据库提交的批量操作
                    LocalRepository.getInstance().saveBBills(listsave);
                    LocalRepository.getInstance().deleteBills(listdelete);
                    // 发送同步成功事件
                    EventBus.getDefault().post(new SyncEvent(100));
                }
                else
                    EventBus.getDefault().post(new SyncEvent(200));
            }
        });
    }

}
