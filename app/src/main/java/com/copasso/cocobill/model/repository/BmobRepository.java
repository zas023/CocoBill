package com.copasso.cocobill.model.repository;

import android.util.Log;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.remote.CoBill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 通过用户获取账单
     *
     * @param id
     * @return
     */
    public List<CoBill> getBillByUserId(String id) {
        final List<CoBill> list = new ArrayList<>();
        BmobQuery<CoBill> query = new BmobQuery<>();
        query.addWhereEqualTo("userid", id);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<CoBill>() {
            @Override
            public void done(List<CoBill> object, BmobException e) {
                if (e == null) {
                    list.addAll(object);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return list;
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

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
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

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
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

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 批量更新账单
     */
    public void commitBills(List<BmobObject> listUpload, List<BmobObject> listUpdate, List<BmobObject> listDelete) {

        BmobBatch batch = new BmobBatch();
        batch.insertBatch(listUpload);
        batch.updateBatch(listUpdate);
        batch.deleteBatch(listDelete);
        batch.doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if (e == null) {

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**************************同步账单******************************/
    /**
     * 同步账单
     */
    public void sycBill(String userid) {
        List<BBill> bBills = LocalRepository.getInstance().getBBills();
        List<CoBill> coBills = BmobRepository.getInstance().getBillByUserId(userid);
        //需要上传的账单
        List<BmobObject> listUpload = new ArrayList<>();
        List<BBill> listBBillUpdate = new ArrayList<>();
        //需要更新的账单
        List<BmobObject> listUpdate = new ArrayList<>();
        //需要删除的账单
        List<BmobObject> listDelete = new ArrayList<>();

        HashMap<String, BBill> bMap = new HashMap<>();
        HashMap<String, CoBill> cMap = new HashMap<>();

        //服务器账单==》键值对
        for (CoBill coBill : coBills) {
            cMap.put(coBill.getObjectId(), coBill);
        }

        for (BBill bBill : bBills) {
            if (bBill.getRid() == null) {
                //服务器端id为空，则表示为上传
                listUpload.add(new CoBill(bBill));
                //以便账单成功上传后更新本地数据
                listBBillUpdate.add(bBill);
            } else
                bMap.put(bBill.getRid(), bBill);
        }

        List<BBill> listsave = new ArrayList<>();
        List<BBill> listdelete = new ArrayList<>();
        for (Map.Entry<String, BBill> entry : bMap.entrySet()) {
            String rid = entry.getKey();
            if (cMap.containsKey(rid)) {
                if (entry.getValue().getVersion() < 0) {
                    //需要删除的账单
                    listDelete.add(new CoBill(entry.getValue()));
                    listdelete.add(entry.getValue());
                } else {
                    //服务器端数据过期
                    if (cMap.get(rid).getVersion() < entry.getValue().getVersion()) {
                        listUpdate.add(new CoBill(entry.getValue()));
                    }
                }
                cMap.remove(rid);
            }
        }
        //提交服务器数据的批量操作
        saveBills(listUpload,listBBillUpdate);
        updateBills(listUpdate);
        deleteBills(listDelete);

        //CoBill==》BBill
        for (Map.Entry<String, CoBill> entry : cMap.entrySet()) {
            BBill bBill = new BBill();
            CoBill coBill = entry.getValue();
            bBill.setRid(coBill.getObjectId());
            bBill.setVersion(coBill.getVersion());
            bBill.setIncome(coBill.getIncome());
            bBill.setCrdate(coBill.getCrdate());
            bBill.setSortImg(coBill.getSortImg());
            bBill.setSortName(coBill.getSortName());
            bBill.setPayImg(coBill.getPayImg());
            bBill.setPayName(coBill.getPayName());
            bBill.setUserid(userid);
            bBill.setContent(coBill.getContent());
            bBill.setCost(coBill.getCost());
            //需要保存到本地的账单
            listsave.add(bBill);
        }
        //提交本地数据的批量操作
        Log.i(TAG, listsave.toString());
        LocalRepository.getInstance().saveBBills(listsave);
    }
}
