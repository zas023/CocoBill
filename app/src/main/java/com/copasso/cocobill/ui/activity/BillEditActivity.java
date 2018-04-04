package com.copasso.cocobill.ui.activity;

import android.os.Bundle;
import android.widget.*;
import com.copasso.cocobill.R;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.local.BPay;
import com.copasso.cocobill.model.bean.local.BSort;
import com.copasso.cocobill.model.bean.local.NoteBean;
import com.copasso.cocobill.mvp.presenter.Imp.BillPresenterImp;
import com.copasso.cocobill.utils.*;
import com.copasso.cocobill.mvp.view.BillView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.copasso.cocobill.utils.DateUtils.*;

/**
 * 修改账单
 */
public class BillEditActivity extends BillAddActivity implements BillView {

    //old Bill
    private Bundle bundle;

    @Override
    protected int getLayout() {
        return R.layout.activity_add;
    }

    @Override
    protected void initEventAndData() {

        presenter=new BillPresenterImp(this);
        //获取旧数据
        setOldBill();

        //初始化分类数据
        initSortView();

        //设置日期选择器初始日期
        mYear = Integer.parseInt(DateUtils.getCurYear(FORMAT_Y));
        mMonth = Integer.parseInt(DateUtils.getCurMonth(FORMAT_M));

    }

    /**
     * 获取旧数据
     */
    private void setOldBill() {

        bundle = getIntent().getBundleExtra("bundle");
        if (bundle == null)
            return;
        //设置账单日期
        days = DateUtils.long2Str(bundle.getLong("date"), FORMAT_YMD);
        dateTv.setText(days);
        isOutcome = !bundle.getBoolean("income");
        remarkInput = bundle.getString("content");
        DecimalFormat df = new DecimalFormat("######0.00");
        String money = df.format(bundle.getDouble("cost"));
        //小数取整
        num = money.split("\\.")[0];
        //截取小数部分
        dotNum = "." + money.split("\\.")[1];

        //设置金额
        moneyTv.setText(num + dotNum);
    }


    /**
     * 通过name查询分类信息
     *
     * @param name
     * @return
     */
    private BSort findSortByName(String name) {
        if (isOutcome) {
            for (BSort e : noteBean.getOutSortlis()) {
                if (e.getSortName() == name)
                    return e;
            }
        } else {
            for (BSort e : noteBean.getInSortlis()) {
                if (e.getSortName() == name)
                    return e;
            }
        }
        return null;
    }

    @Override
    public void loadDataSuccess(NoteBean tData) {
        noteBean=tData;
        //成功后加载布局
        setTitleStatus();
    }

    /**
     * 通过name查找支付方式，返回其数组中的序号
     *
     * @param name
     * @return
     */
    private int findPayByName(String name) {
        List<BPay> list = noteBean.getPayinfo();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPayName() == name)
                return i;
        }
        return 0;
    }

    /**
     * 设置状态
     */
    protected void setTitleStatus() {

        //设置类型
        setTitle();

        lastBean = findSortByName(bundle.getString("sortName"));
        //当前分类未查询到次账单的分类
        //存在该分类被删除的情况，以及切换账单收入支出类型
        if (lastBean==null)
            lastBean=mDatas.get(0);
        sortTv.setText(lastBean.getSortName());

        cardItem = new ArrayList<>();
        for (int i = 0; i < noteBean.getPayinfo().size(); i++) {
            String itemStr = noteBean.getPayinfo().get(i).getPayName();
            cardItem.add(itemStr);
        }
        //设置支付方式
        selectedPayinfoIndex = findPayByName(bundle.getString("payName"));
        cashTv.setText(cardItem.get(selectedPayinfoIndex));

        initViewPager();
    }

    /**
     * 提交账单
     */
    public void doCommit() {
        final SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm:ss");
        final String crDate = days + sdf.format(new Date());
        if ((num + dotNum).equals("0.00")) {
            Toast.makeText(this, "唔姆，你还没输入金额", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressUtils.show(mContext, "正在提交...");
        presenter.update(new BBill(bundle.getLong("id"),bundle.getString("rid"),
                Float.valueOf(num + dotNum),remarkInput,currentUser.getObjectId(),
                noteBean.getPayinfo().get(selectedPayinfoIndex).getPayName(),
                noteBean.getPayinfo().get(selectedPayinfoIndex).getPayImg(),
                lastBean.getSortName(),lastBean.getSortImg(),
                DateUtils.getMillis(crDate),!isOutcome,bundle.getInt("version")+1));
    }


}
