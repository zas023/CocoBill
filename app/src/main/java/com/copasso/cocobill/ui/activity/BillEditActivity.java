package com.copasso.cocobill.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.BindView;
import butterknife.OnClick;
import com.bigkoo.pickerview.OptionsPickerView;
import com.copasso.cocobill.R;
import com.copasso.cocobill.ui.adapter.BookNoteAdapter;
import com.copasso.cocobill.ui.adapter.MonthAccountAdapter;
import com.copasso.cocobill.bean.BPay;
import com.copasso.cocobill.bean.BSort;
import com.copasso.cocobill.bean.BaseBean;
import com.copasso.cocobill.bean.NoteBean;
import com.copasso.cocobill.common.Constants;
import com.copasso.cocobill.mvp.presenter.BillPresenter;
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
public class BillEditActivity extends BaseActivity implements BillView {

    @BindView(R.id.tb_note_income)
    TextView incomeTv;
    @BindView(R.id.tb_note_outcome)
    TextView outcomeTv;
    @BindView(R.id.item_tb_type_tv)
    TextView sortTv;     //显示选择的分类
    @BindView(R.id.tb_note_remark)
    ImageView remarkv;
    @BindView(R.id.tb_note_money)
    TextView moneyTv;
    @BindView(R.id.tb_note_date)
    TextView dateTv;
    @BindView(R.id.tb_note_cash)
    TextView cashTv;
    @BindView(R.id.viewpager_item)
    ViewPager viewpagerItem;
    @BindView(R.id.layout_icon)
    LinearLayout layoutIcon;

    private BillPresenter presenter;

    public boolean isOutcome = true;
    //计算器
    private boolean isDot;
    private String num = "0";
    private String dotNum = ".00";
    private final int MAX_NUM = 9999999;
    private final int DOT_NUM = 2;
    private int count = 0;
    //选择器
    private OptionsPickerView pvCustomOptions;
    private List<String> cardItem;
    private int selectedPayinfoIndex = 0;
    //viewpager数据
    private int page;
    private boolean isTotalPage;
    private int sortPage = -1;
    private List<BSort> mDatas;
    private List<BSort> tempList;
    //记录上一次点击后的imageview
    public BSort lastBean;

    //备注对话框
    private AlertDialog alertDialog;

    //选择时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private String days;

    private String remarkInput = "";
    private NoteBean noteBean = null;

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

    @Override
    public void loadDataSuccess(NoteBean tData) {
        noteBean=tData;
        //成功后加载布局
        setTitleStatus();
        //保存数据
        SharedPUtils.setUserNoteBean(mContext,tData);
    }

    @Override
    public void loadDataSuccess(BaseBean tData) {
        ProgressUtils.dismiss();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void loadDataError(Throwable throwable) {
        ProgressUtils.dismiss();
        SnackbarUtils.show(mContext,throwable.getMessage());
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
     * 初始化分类数据
     */
    private void initSortView(){
        //获取本地分类、支付方式信息
        noteBean=SharedPUtils.getUserNoteBean(BillEditActivity.this);
        //本地获取失败后
        if (noteBean==null){
            //同步获取分类、支付方式信息
            presenter.getNote(currentUser.getId());
        }else {
            //成功后加载布局
            setTitleStatus();
        }
    }

    /**
     * 通过id查询分类信息
     *
     * @param id
     * @return
     */
    private BSort findSortById(int id) {
        if (isOutcome) {
            for (BSort e : noteBean.getOutSortlis()) {
                if (e.getId() == id)
                    return e;
            }
        } else {
            for (BSort e : noteBean.getInSortlis()) {
                if (e.getId() == id)
                    return e;
            }
        }
        return isOutcome? noteBean.getOutSortlis().get(0):noteBean.getInSortlis().get(0);
    }

    /**
     * 通过id查找支付方式，返回其数组中的序号
     *
     * @param id
     * @return
     */
    private int findPayById(int id) {
        List<BPay> list = noteBean.getPayinfo();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id)
                return i;
        }
        return 0;
    }

    /**
     * 设置状态
     */
    private void setTitleStatus() {

        //设置类型
        if (isOutcome) {
            outcomeTv.setSelected(true);
            incomeTv.setSelected(false);
            mDatas = noteBean.getOutSortlis();
        } else {
            incomeTv.setSelected(true);
            outcomeTv.setSelected(false);
            mDatas = noteBean.getInSortlis();
        }

        lastBean = findSortById(bundle.getInt("sortId"));
        //当前分类未查询到次账单的分类
        //存在该分类被删除的情况，以及切换账单收入支出类型
        if (lastBean==null)
            lastBean=mDatas.get(0);
        sortTv.setText(lastBean.getSortName());

        cardItem = new ArrayList<>();
        for (int i = 0; i < noteBean.getPayinfo().size(); i++) {
            String itemStr = noteBean.getPayinfo().get(i).getPayName();
            if (noteBean.getPayinfo().get(i).getPayNum()!=null)
                itemStr=itemStr+noteBean.getPayinfo().get(i).getPayNum();
            cardItem.add(itemStr);
        }
        //设置支付方式
        selectedPayinfoIndex = findPayById(bundle.getInt("payId"));
        cashTv.setText(cardItem.get(selectedPayinfoIndex));

        initViewPager();
    }

    /**
     * 初始化账单分类视图
     */
    private void initViewPager() {
        LayoutInflater inflater = this.getLayoutInflater();// 获得一个视图管理器LayoutInflater
        viewList = new ArrayList<>();// 创建一个View的集合对象
        //声明一个局部变量来存储分类集合
        //否则在收入支出类型切换时末尾一直添加选项
        List<BSort> tempData=new ArrayList<>();
        tempData.addAll(mDatas);
        //末尾加上添加选项
        tempData.add(new BSort("添加","sort_tianjia.png"));
        if (tempData.size() % 15 == 0)
            isTotalPage = true;
        page = (int) Math.ceil(tempData.size() * 1.0 / 15);
        for (int i = 0; i < page; i++) {
            tempList = new ArrayList<>();
            View view = inflater.inflate(R.layout.pager_item_tb_type, null);
            RecyclerView recycle = (RecyclerView) view.findViewById(R.id.pager_type_recycle);
            if (i != page - 1 || (i == page - 1 && isTotalPage)) {
                for (int j = 0; j < 15; j++) {
                    if (i != 0) {
                        tempList.add(tempData.get(i * 15 + j));
                    } else {
                        tempList.add(tempData.get(i + j));
                    }
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
                    index=index + viewpagerItem.getCurrentItem() * 15;
                    if (index==mDatas.size()) {
                        //修改分类
                        Intent intent = new Intent(BillEditActivity.this, SortEditActivity.class);
                        intent.putExtra("type", isOutcome);
                        startActivityForResult(intent, 0);
                    } else{
                        //选择分类
                        lastBean = mDatas.get(index);
                        sortTv.setText(lastBean.getSortName());
                    }
                }

                @Override
                public void OnLongClick(int index) {
                    Toast.makeText(BillEditActivity.this,"长按",Toast.LENGTH_SHORT).show();
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

    private List<View> viewList;
    private ImageView[] icons;

    /**
     * 初始化账单分类视图的指示小红点
     */
    private void initIcon() {
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

    /**
     * 监听点击事件
     *
     * @param view
     */
    @OnClick({R.id.tb_note_income, R.id.tb_note_outcome, R.id.tb_note_cash, R.id.tb_note_date,
            R.id.tb_note_remark, R.id.tb_calc_num_done, R.id.tb_calc_num_del, R.id.tb_calc_num_1,
            R.id.tb_calc_num_2, R.id.tb_calc_num_3, R.id.tb_calc_num_4, R.id.tb_calc_num_5,
            R.id.tb_calc_num_6, R.id.tb_calc_num_7, R.id.tb_calc_num_8, R.id.tb_calc_num_9,
            R.id.tb_calc_num_0, R.id.tb_calc_num_dot, R.id.tb_note_clear, R.id.back_btn})
    protected void onClick(View view) {
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
     * 显示支付方式选择器
     */
    public void showPayinfoSelector() {
        pvCustomOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                selectedPayinfoIndex = options1;
                cashTv.setText(cardItem.get(options1));
            }
        })
                .build();
        pvCustomOptions.setPicker(cardItem);
        pvCustomOptions.show();
    }

    /**
     * 显示日期选择器
     */
    public void showTimeSelector() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
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
            }
        }, mYear, mMonth, mDay).show();
    }

    /**
     * 显示备注内容输入框
     */
    public void showContentDialog() {
        final EditText editText = new EditText(BillEditActivity.this);

        editText.setText(remarkInput);
        //将光标移至文字末尾
        editText.setSelection(remarkInput.length());

        //弹出输入框
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("备注")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = editText.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getApplicationContext(), "内容不能为空！" + input,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            remarkInput = input;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                //调用系统输入法
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
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

        ProgressUtils.show(BillEditActivity.this, "正在修改...");

        presenter.update(bundle.getInt("id"),Constants.currentUserId,lastBean.getId(),
                noteBean.getPayinfo().get(selectedPayinfoIndex).getId(),num + dotNum,remarkInput,
                crDate,!isOutcome);
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
    private void calcMoney(int money) {
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
                    initSortView();
                    break;
            }
        }
    }

}
