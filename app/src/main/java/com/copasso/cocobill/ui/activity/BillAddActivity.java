package com.copasso.cocobill.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.copasso.cocobill.MyApplication;
import com.copasso.cocobill.R;
import com.copasso.cocobill.base.BaseMVPActivity;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.local.BSort;
import com.copasso.cocobill.model.bean.local.NoteBean;
import com.copasso.cocobill.presenter.contract.BillContract;
import com.copasso.cocobill.presenter.BillPresenter;
import com.copasso.cocobill.ui.adapter.BookNoteAdapter;
import com.copasso.cocobill.ui.adapter.MonthAccountAdapter;
import com.copasso.cocobill.utils.DateUtils;
import com.copasso.cocobill.utils.ProgressUtils;
import com.copasso.cocobill.utils.SnackbarUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static com.copasso.cocobill.utils.DateUtils.FORMAT_D;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_M;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_Y;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_YMD;

/**
 * Created by Zhouas666 on 2019-01-10
 * Github: https://github.com/zas023
 * <p>
 * 账单添加、编辑activity
 */
public class BillAddActivity extends BaseMVPActivity<BillContract.Presenter>
        implements BillContract.View, View.OnClickListener {

    private TextView incomeTv;    //收入按钮
    private TextView outcomeTv;   //支出按钮
    private TextView sortTv;     //显示选择的分类
    private TextView moneyTv;     //金额
    private TextView dateTv;      //时间选择
    private TextView cashTv;      //支出方式选择
    private ImageView remarkIv;   //
    private ViewPager viewpagerItem;
    private LinearLayout layoutIcon;

    //计算器
    protected boolean isDot;
    protected String num = "0";               //整数部分
    protected String dotNum = ".00";          //小数部分
    protected final int MAX_NUM = 9999999;    //最大整数
    protected final int DOT_NUM = 2;          //小数部分最大位数
    protected int count = 0;
    //选择器
    protected List<String> cardItems;
    protected int selectedPayinfoIndex = 0;      //选择的支付方式序号
    //viewpager数据
    protected int page;
    protected boolean isTotalPage;
    protected int sortPage = -1;
    protected List<BSort> mDatas;
    protected List<BSort> tempList;
    protected List<View> viewList;
    protected ImageView[] icons;

    //记录上一次点击后的分类
    public BSort lastBean;

    public boolean isOutcome = true;
    public boolean isEdit = false;
    //old Bill
    private Bundle bundle;

    //选择时间
    protected int mYear;
    protected int mMonth;
    protected int mDay;
    protected String days;

    //备注
    protected String remarkInput = "";
    protected NoteBean noteBean = null;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        //设置日期选择器初始日期
        mYear = Integer.parseInt(DateUtils.getCurYear(FORMAT_Y));
        mMonth = Integer.parseInt(DateUtils.getCurMonth(FORMAT_M));
        mDay = Integer.parseInt(DateUtils.getCurDay(FORMAT_D));
        //设置当前 日期
        days = DateUtils.getCurDateStr("yyyy-MM-dd");

        bundle = getIntent().getBundleExtra("bundle");

        if (bundle != null) {    //edit
            isEdit = true;
            //设置账单日期
            days = DateUtils.long2Str(bundle.getLong("date"), FORMAT_YMD);
            isOutcome = !bundle.getBoolean("income");
            remarkInput = bundle.getString("content");
            DecimalFormat df = new DecimalFormat("######0.00");
            String money = df.format(bundle.getDouble("cost"));
            //小数取整
            num = money.split("\\.")[0];
            //截取小数部分
            dotNum = "." + money.split("\\.")[1];
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        incomeTv = findViewById(R.id.tb_note_income);
        outcomeTv = findViewById(R.id.tb_note_outcome);
        sortTv = findViewById(R.id.item_tb_type_tv);
        moneyTv = findViewById(R.id.tb_note_money);
        dateTv = findViewById(R.id.tb_note_date);
        cashTv = findViewById(R.id.tb_note_cash);
        remarkIv = findViewById(R.id.tb_note_remark);
        viewpagerItem = findViewById(R.id.viewpager_item);
        layoutIcon = findViewById(R.id.layout_icon);

        //设置账单日期
        dateTv.setText(days);
        //设置金额
        moneyTv.setText(num + dotNum);
    }

    @Override
    protected void initClick() {
        super.initClick();
        incomeTv.setOnClickListener(this);
        outcomeTv.setOnClickListener(this);
        cashTv.setOnClickListener(this);
        dateTv.setOnClickListener(this);
        remarkIv.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.getBillNote();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.tb_note_income://收入
                isOutcome = false;
                setTitleStatus();
                break;
            case R.id.tb_note_outcome://支出
                isOutcome = true;
                setTitleStatus();
                break;
            case R.id.tb_note_cash://现金
                showPayinfoSelector();
                break;
            case R.id.tb_note_date://日期
                showTimeSelector();
                break;
            case R.id.tb_note_remark://备注
                showContentDialog();
                break;
            case R.id.tb_calc_num_done://确定
                doCommit();
                break;
            case R.id.tb_calc_num_1:
                calcMoney(1);
                break;
            case R.id.tb_calc_num_2:
                calcMoney(2);
                break;
            case R.id.tb_calc_num_3:
                calcMoney(3);
                break;
            case R.id.tb_calc_num_4:
                calcMoney(4);
                break;
            case R.id.tb_calc_num_5:
                calcMoney(5);
                break;
            case R.id.tb_calc_num_6:
                calcMoney(6);
                break;
            case R.id.tb_calc_num_7:
                calcMoney(7);
                break;
            case R.id.tb_calc_num_8:
                calcMoney(8);
                break;
            case R.id.tb_calc_num_9:
                calcMoney(9);
                break;
            case R.id.tb_calc_num_0:
                calcMoney(0);
                break;
            case R.id.tb_calc_num_dot:
                if (dotNum.equals(".00")) {
                    isDot = true;
                    dotNum = ".";
                }
                moneyTv.setText(num + dotNum);
                break;
            case R.id.tb_note_clear://清空
                doClear();
                break;
            case R.id.tb_calc_num_del://删除
                doDelete();
                break;
        }
    }

    /**
     * 监听Activity返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    mPresenter.getBillNote();
                    break;
            }
        }
    }

    /**
     * 显示支付方式选择器
     */
    public void showPayinfoSelector() {
        new MaterialDialog.Builder(mContext)
                .title("选择支付方式")
                .titleGravity(GravityEnum.CENTER)
                .items(cardItems)
                .positiveText("确定")
                .negativeText("取消")
                .itemsCallbackSingleChoice(selectedPayinfoIndex, (dialog, itemView, which, text) -> {
                    selectedPayinfoIndex = which;
                    cashTv.setText(cardItems.get(which));
                    dialog.dismiss();
                    return false;
                }).show();
    }

    /**
     * 显示日期选择器
     */
    public void showTimeSelector() {
        new DatePickerDialog(this, (DatePicker datePicker, int i, int i1, int i2) -> {
            mYear = i;
            mMonth = i1;
            mDay = i2;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            }
            dateTv.setText(days);
        }, mYear, mMonth, mDay).show();
    }

    /**
     * 显示备注内容输入框
     */
    public void showContentDialog() {

        new MaterialDialog.Builder(this)
                .title("备注")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(0, 200, R.color.textRed)
                .input("写点什么", remarkInput, (dialog, input) -> {
                    if (input.equals("")) {
                        Toast.makeText(getApplicationContext(), "内容不能为空！" + input,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        remarkInput = input.toString();
                    }
                })
                .positiveText("确定")
                .show();
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
        BBill bBill;
        if (isEdit) {
            bBill = new BBill(bundle.getLong("id"), bundle.getString("rid"),
                    Float.valueOf(num + dotNum), remarkInput, MyApplication.getCurrentUserId(),
                    noteBean.getPayinfo().get(selectedPayinfoIndex).getPayName(),
                    noteBean.getPayinfo().get(selectedPayinfoIndex).getPayImg(),
                    lastBean.getSortName(), lastBean.getSortImg(),
                    DateUtils.getMillis(crDate), !isOutcome, bundle.getInt("version") + 1);
            mPresenter.updateBill(bBill);
        } else {
            bBill = new BBill(null, null, Float.valueOf(num + dotNum), remarkInput,
                    MyApplication.getCurrentUserId(),
                    noteBean.getPayinfo().get(selectedPayinfoIndex).getPayName(),
                    noteBean.getPayinfo().get(selectedPayinfoIndex).getPayImg(),
                    lastBean.getSortName(), lastBean.getSortImg(),
                    DateUtils.getMillis(crDate), !isOutcome, 0);
            mPresenter.addBill(bBill);
        }
    }


    /**
     * 清空金额
     */
    public void doClear() {
        num = "0";
        count = 0;
        dotNum = ".00";
        isDot = false;
        moneyTv.setText("0.00");
    }

    /**
     * 删除上次输入
     */
    public void doDelete() {
        if (isDot) {
            if (count > 0) {
                dotNum = dotNum.substring(0, dotNum.length() - 1);
                count--;
            }
            if (count == 0) {
                isDot = false;
                dotNum = ".00";
            }
            moneyTv.setText(num + dotNum);
        } else {
            if (num.length() > 0)
                num = num.substring(0, num.length() - 1);
            if (num.length() == 0)
                num = "0";
            moneyTv.setText(num + dotNum);
        }
    }

    /**
     * 计算金额
     *
     * @param money
     */
    protected void calcMoney(int money) {
        if (num.equals("0") && money == 0)
            return;
        if (isDot) {
            if (count < DOT_NUM) {
                count++;
                dotNum += money;
                moneyTv.setText(num + dotNum);
            }
        } else if (Integer.parseInt(num) < MAX_NUM) {
            if (num.equals("0"))
                num = "";
            num += money;
            moneyTv.setText(num + dotNum);
        }
    }

    /**
     * 设置状态
     */
    protected void setTitleStatus() {

        setTitle();
        //默认选择第一个分类
        lastBean = mDatas.get(0);
        //设置选择的分类
        sortTv.setText(lastBean.getSortName());

        //加载支付方式信息
        cardItems = new ArrayList<>();
        for (int i = 0; i < noteBean.getPayinfo().size(); i++) {
            String itemStr = noteBean.getPayinfo().get(i).getPayName();
            cardItems.add(itemStr);
        }

        initViewPager();
    }

    protected void setTitle() {
        if (isOutcome) {
            //设置支付状态
            outcomeTv.setSelected(true);
            incomeTv.setSelected(false);
            mDatas = noteBean.getOutSortlis();
        } else {
            //设置收入状态
            incomeTv.setSelected(true);
            outcomeTv.setSelected(false);
            mDatas = noteBean.getInSortlis();
        }
    }

    protected void initViewPager() {
        LayoutInflater inflater = this.getLayoutInflater();// 获得一个视图管理器LayoutInflater
        viewList = new ArrayList<>();// 创建一个View的集合对象
        //声明一个局部变量来存储分类集合
        //否则在收入支出类型切换时末尾一直添加选项
        List<BSort> tempData = new ArrayList<>();
        tempData.addAll(mDatas);
        //末尾加上添加选项
        tempData.add(new BSort(null, "添加", "sort_tianjia.png", 0, 0, null));
        if (tempData.size() % 15 == 0)
            isTotalPage = true;
        page = (int) Math.ceil(tempData.size() * 1.0 / 15);
        for (int i = 0; i < page; i++) {
            tempList = new ArrayList<>();
            View view = inflater.inflate(R.layout.item_tb_type_page, null);
            RecyclerView recycle = view.findViewById(R.id.pager_type_recycle);
            if (i != page - 1 || (i == page - 1 && isTotalPage)) {
                for (int j = 0; j < 15; j++) {
                    tempList.add(tempData.get(i * 15 + j));
                }
            } else {
                for (int j = 0; j < tempData.size() % 15; j++) {
                    tempList.add(tempData.get(i * 15 + j));
                }
            }

            BookNoteAdapter mAdapter = new BookNoteAdapter(this, tempList);
            mAdapter.setOnBookNoteClickListener(new BookNoteAdapter.OnBookNoteClickListener() {
                @Override
                public void OnClick(int index) {
                    //获取真实index
                    index = index + viewpagerItem.getCurrentItem() * 15;
                    if (index == mDatas.size()) {
                        //修改分类
                        Intent intent = new Intent(mContext, BillSortActivity.class);
                        intent.putExtra("type", isOutcome);
                        startActivityForResult(intent, 0);
                    } else {
                        //选择分类
                        lastBean = mDatas.get(index);
                        sortTv.setText(lastBean.getSortName());
                    }
                }

                @Override
                public void OnLongClick(int index) {
                    Toast.makeText(BillAddActivity.this, "长按", Toast.LENGTH_SHORT).show();
                }
            });
            GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
            recycle.setLayoutManager(layoutManager);
            recycle.setAdapter(mAdapter);
            viewList.add(view);
        }

        viewpagerItem.setAdapter(new MonthAccountAdapter(viewList));
        viewpagerItem.setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewpagerItem.setOffscreenPageLimit(1);//预加载数据页
        viewpagerItem.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    for (int i = 0; i < viewList.size(); i++) {
                        icons[i].setImageResource(R.drawable.icon_banner_point2);
                    }
                    icons[position].setImageResource(R.drawable.icon_banner_point1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initIcon();
    }

    /**
     * 添加账单分类指示器
     */
    protected void initIcon() {
        icons = new ImageView[viewList.size()];
        layoutIcon.removeAllViews();
        for (int i = 0; i < icons.length; i++) {
            icons[i] = new ImageView(this);
            icons[i].setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            icons[i].setImageResource(R.drawable.icon_banner_point2);
            if (viewpagerItem.getCurrentItem() == i) {
                icons[i].setImageResource(R.drawable.icon_banner_point1);
            }
            icons[i].setPadding(5, 0, 5, 0);
            icons[i].setAdjustViewBounds(true);
            layoutIcon.addView(icons[i]);
        }
        if (sortPage != -1)
            viewpagerItem.setCurrentItem(sortPage);
    }

    /***********************************************************************/
    @Override
    protected BillContract.Presenter bindPresenter() {
        return new BillPresenter();
    }

    @Override
    public void onSuccess() {
        ProgressUtils.dismiss();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onFailure(Throwable e) {
        ProgressUtils.dismiss();
        SnackbarUtils.show(mContext, e.getMessage());
    }

    @Override
    public void loadDataSuccess(NoteBean bean) {
        noteBean = bean;
        //成功后加载布局
        setTitleStatus();
    }
}
