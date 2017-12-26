package com.copasso.cocobill.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.bigkoo.pickerview.OptionsPickerView;
import com.copasso.cocobill.R;
import com.copasso.cocobill.adapter.BookNoteAdapter;
import com.copasso.cocobill.adapter.MonthAccountAdapter;
import com.copasso.cocobill.bean.*;
import com.copasso.cocobill.utils.HttpUtils;
import com.copasso.cocobill.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.google.gson.Gson;

import static com.copasso.cocobill.utils.DateUtils.FORMAT_M;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_Y;

/**
 * 添加账单
 */
public class AddBillActivity extends BaseActivity {

    @BindView(R.id.tb_note_income)
    TextView incomeTv;    //收入按钮
    @BindView(R.id.tb_note_outcome)
    TextView outcomeTv;   //支出按钮
    @BindView(R.id.tb_note_remark)
    ImageView remarkTv;   //
    @BindView(R.id.tb_note_money)
    TextView moneyTv;     //金额
    @BindView(R.id.tb_note_date)
    TextView dateTv;      //时间选择
    @BindView(R.id.tb_note_cash)
    TextView cashTv;      //支出方式选择
    @BindView(R.id.viewpager_item)
    ViewPager viewpagerItem;
    @BindView(R.id.layout_icon)
    LinearLayout layoutIcon;

    public boolean isOutcome = true;
    //计算器
    private boolean isDot;
    private String num = "0";               //整数部分
    private String dotNum = ".00";          //小数部分
    private final int MAX_NUM = 9999999;    //最大整数
    private final int DOT_NUM = 2;          //小数部分最大位数
    private int count = 0;
    //选择器
    private OptionsPickerView pvCustomOptions;
    private List<String> cardItem;
    private int selectedPayinfoIndex=0;      //选择的支付方式序号
    //viewpager数据
    private int page ;
    private boolean isTotalPage;
    private int sortPage = -1;
    private List<NoteBean.SortlisBean> mDatas;
    private List<NoteBean.SortlisBean> tempList;
    //记录上一次点击后的imageview
    public NoteBean.SortlisBean lastBean;
    public ImageView lastImg;

    //备注对话框
    private AlertDialog alertDialog;

    //选择时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private String days;

    //备注
    private String remarkInput="";
    private NoteBean noteBean = null;


    @Override
    protected int getLayout() {
        return R.layout.activity_add;
    }

    @Override
    protected void initEventAndData() {
        //获取分类、支付方式信息
        HttpUtils.getNote(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Gson gson = new Gson();
                noteBean = gson.fromJson(msg.obj.toString(), NoteBean.class);
                //status==100:处理成功！
                if (noteBean.getStatus() == 100) {
                    //成功后加载布局
                    setTitleStatus();
                }
            }
        },1);

        //设置日期选择器初始日期
        mYear = Integer.parseInt(DateUtils.getCurYear(FORMAT_Y));
        mMonth = Integer.parseInt(DateUtils.getCurMonth(FORMAT_M));
        //设置当前 日期
        days=DateUtils.getCurDateStr("yyyy-MM-dd");
        dateTv.setText(days);

    }

    /**
     * 设置状态
     */
    private void setTitleStatus() {

        if (isOutcome){
            //设置支付状态
            outcomeTv.setSelected(true);
            incomeTv.setSelected(false);
            mDatas = noteBean.getOutSortlis();
        }else{
            //设置收入状态
            incomeTv.setSelected(true);
            outcomeTv.setSelected(false);
            mDatas = noteBean.getInSortlis();
        }

        //默认选择第一个分类
        lastBean = mDatas.get(0);
        lastImg = new ImageView(this);

        //加载支付方式信息
        cardItem = new ArrayList<>();
        for (int i = 0; i < noteBean.getPayinfo().size(); i++) {
            String itemStr=noteBean.getPayinfo().get(i).getPayName();
            cardItem.add(itemStr);
        }

        initViewPager();
    }

    private void initViewPager() {
        LayoutInflater inflater = this.getLayoutInflater();// 获得一个视图管理器LayoutInflater
        viewList = new ArrayList<>();// 创建一个View的集合对象
        if (mDatas.size() % 15 == 0)
            isTotalPage = true;
        page = (int) Math.ceil(mDatas.size() * 1.0 / 15);
        for (int i = 0; i < page; i++) {
            tempList = new ArrayList<>();
            View view = inflater.inflate(R.layout.pager_item_tb_type, null);
            RecyclerView recycle = (RecyclerView) view.findViewById(R.id.pager_type_recycle);
            if (i != page - 1 || (i == page -1 && isTotalPage)){
                for (int j = 0; j < 15; j++) {
                    if (i != 0 ){
                        tempList.add(mDatas.get(i * 15 + j));
                    }else {
                        tempList.add(mDatas.get(i + j));
                    }
                }
            }else {
                for (int j = 0; j < mDatas.size() % 15; j++) {
                    tempList.add(mDatas.get(i * 15 + j));
                }
            }

            BookNoteAdapter mAdapter = new BookNoteAdapter(this, tempList);
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
                }catch (Exception e){
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
    private void initIcon() {
        icons = new ImageView[viewList.size()];
        layoutIcon.removeAllViews();
        for (int i = 0; i < icons.length; i++) {
            icons[i] = new ImageView(this);
            icons[i].setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            icons[i].setImageResource(R.drawable.icon_banner_point2);
            if(viewpagerItem.getCurrentItem() == i){
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
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.tb_note_income, R.id.tb_note_outcome, R.id.tb_note_cash, R.id.tb_note_date,
            R.id.tb_note_remark, R.id.tb_calc_num_done, R.id.tb_calc_num_del, R.id.tb_calc_num_1,
            R.id.tb_calc_num_2, R.id.tb_calc_num_3, R.id.tb_calc_num_4, R.id.tb_calc_num_5,
            R.id.tb_calc_num_6, R.id.tb_calc_num_7, R.id.tb_calc_num_8, R.id.tb_calc_num_9,
            R.id.tb_calc_num_0, R.id.tb_calc_num_dot, R.id.tb_note_clear, R.id.back_btn})
    protected void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.tb_note_income://收入
                isOutcome=false;
                setTitleStatus();
                break;
            case R.id.tb_note_outcome://支出
                isOutcome=true;
                setTitleStatus();
                break;
            case R.id.tb_note_cash://现金
                pvCustomOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        selectedPayinfoIndex=options1;
                        cashTv.setText(cardItem.get(options1));
                    }
                })
                        .build();
                pvCustomOptions.setPicker(cardItem);
                pvCustomOptions.show();
                break;
            case R.id.tb_note_date://日期

                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        mYear = i;
                        mMonth = i1;
                        mDay = i2;
                        //构造时间字符串
                        if (mMonth + 1 < 10) {
                            if (mDay < 10) {
                                days = new StringBuffer().append(mYear).append("-").append("0").
                                        append(mMonth + 1).append("-").append("0").append(mDay).append("-").toString();
                            } else {
                                days = new StringBuffer().append(mYear).append("年").append("0").
                                        append(mMonth + 1).append("-").append(mDay).append("-").toString();
                            }

                        } else {
                            if (mDay < 10) {
                                days = new StringBuffer().append(mYear).append("-").
                                        append(mMonth + 1).append("-").append("0").append(mDay).append("-").toString();
                            } else {
                                days = new StringBuffer().append(mYear).append("-").
                                        append(mMonth + 1).append("-").append(mDay).toString();
                            }

                        }
                        dateTv.setText(days);
                    }
                },mYear, mMonth, mDay ).show();
                break;
            case R.id.tb_note_remark://备注

                final EditText editText = new EditText(AddBillActivity.this);

                editText.setText(remarkInput);
                //将光标移至文字末尾
                editText.setSelection(remarkInput.length());

                //弹出输入框
                alertDialog=new AlertDialog.Builder(this)
                        .setTitle("备注")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String input = editText.getText().toString();
                                if (input.equals("")) {
                                    Toast.makeText(getApplicationContext(), "内容不能为空！" + input,
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    remarkInput=input;
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
                break;
            case R.id.tb_calc_num_done://确定
                final SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm:ss");
                final String crDate=days+sdf.format(new Date());
                if ((num+dotNum).equals("0.00")) {
                    Toast.makeText(this, "唔姆，你还没输入金额", Toast.LENGTH_SHORT).show();
                    break;
                }
                HttpUtils.addBill(new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Gson gson = new Gson();
                        if(msg.obj.toString()!="null"){
                            Intent intent = new Intent();
                            intent.putExtra("billJsonStr",msg.obj.toString());
                            setResult(RESULT_OK, intent);
                            finish();
                        }else{
                            Toast.makeText(AddBillActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                },Float.valueOf(num+dotNum),remarkInput,1,lastBean.getId(),
                        noteBean.getPayinfo().get(selectedPayinfoIndex).getId(),
                        crDate,
                        !isOutcome);
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
                if (dotNum.equals(".00")){
                    isDot = true;
                    dotNum = ".";
                }
                moneyTv.setText(num + dotNum);
                break;
            case R.id.tb_note_clear://清空
                num = "0";
                count = 0;
                dotNum = ".00";
                isDot = false;
                moneyTv.setText("0.00");
                break;
            case R.id.tb_calc_num_del://删除
                if (isDot){
                    if (count > 0){
                        dotNum = dotNum.substring(0, dotNum.length() - 1);
                        count--;
                    }
                    if (count == 0){
                        isDot = false;
                        dotNum = ".00";
                    }
                    moneyTv.setText(num  + dotNum);
                }else {
                    if (num.length() > 0)
                        num = num.substring(0, num.length() - 1);
                    if (num.length() == 0)
                        num = "0";
                    moneyTv.setText(num + dotNum);
                }
                break;
        }
    }

    /**
     * 计算金额
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
        }else if (Integer.parseInt(num) < MAX_NUM) {
            if (num.equals("0"))
                num = "";
            num += money;
            moneyTv.setText(num + dotNum);
        }
    }

}
