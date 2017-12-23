package com.copasso.cocobill.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;
import com.bigkoo.pickerview.TimePickerView;
import com.copasso.cocobill.R;
import com.copasso.cocobill.activity.AddBillActivity;
import com.copasso.cocobill.adapter.MonthDetailAdapter;
import com.copasso.cocobill.bean.MonthDetailBean;
import com.copasso.cocobill.bean.NoteBean;
import com.copasso.cocobill.stickyheader.StickyHeaderGridLayoutManager;
import com.copasso.cocobill.utils.Constants;
import com.copasso.cocobill.utils.HttpUtils;
import com.copasso.cocobill.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.google.gson.Gson;

import static com.copasso.cocobill.utils.DateUtils.FORMAT_M;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_Y;

/**
 * 记账本--明细
 */
public class MenuDetailFragment extends BaseFragment {

    @BindView(R.id.data_year)
    TextView dataYear;
    @BindView(R.id.data_month)
    TextView dataMonth;
    @BindView(R.id.layout_data)
    LinearLayout layoutData;
    @BindView(R.id.top_ll_out)
    LinearLayout layoutOutin;
    @BindView(R.id.t_outcome)
    TextView tOutcome;
    @BindView(R.id.t_income)
    TextView tIncome;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.float_btn)
    FloatingActionButton floatBtn;

    private static final int SPAN_SIZE = 1;
    private StickyHeaderGridLayoutManager mLayoutManager;
    private MonthDetailAdapter adapter;
    private List<MonthDetailBean.DaylistBean> list;
    private MonthDetailBean data;

    private String setYear=DateUtils.getCurYear(FORMAT_Y);
    private String setMonth=DateUtils.getCurMonth(FORMAT_M);

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_menu_detail;
    }


    @Override
    protected void initEventAndData() {

        dataYear.setText(setYear+" 年");
        dataMonth.setText(setMonth);
        //改变加载显示的颜色
        swipe.setColorSchemeColors(getResources().getColor(R.color.text_red), getResources().getColor(R.color.text_red));
        //设置向下拉多少出现刷新
        swipe.setDistanceToTriggerSync(200);
        //设置刷新出现的位置
        swipe.setProgressViewEndTarget(false, 200);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                setBillData(Constants.currentUserId,setYear,setMonth);
            }
        });

        mLayoutManager = new StickyHeaderGridLayoutManager(SPAN_SIZE);
        mLayoutManager.setHeaderBottomOverlapMargin(5);

        rvList.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                dispatchRemoveFinished(holder);
                return false;
            }
        });
        rvList.setLayoutManager(mLayoutManager);
        adapter = new MonthDetailAdapter(mContext, list);
        rvList.setAdapter(adapter);
        //list的滑动监听
        rvList.setOnTouchListener(new View.OnTouchListener() {
            private float lastX;
            private float lastY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getX();
                        lastY = event.getY();
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getX();
                        float y = event.getY();
                        boolean isUp=lastY- y>2;
                        //一次down，只变化一次，防止一次滑动时抖动下，造成某一个的向下时,y比lastY小
                        if(isUp){
                            floatBtn.setVisibility(View.GONE);
                        }
                        else{
                            floatBtn.setVisibility(View.VISIBLE);
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }

        });
        //请求当月数据
        setBillData(Constants.currentUserId,setYear,setMonth);
    }


    private void setBillData(final int userid, String year, String month) {
        if (userid==0){
            Toast.makeText(getContext(), "请先登陆", Toast.LENGTH_SHORT).show();
            return;
        }
        dataYear.setText(year+" 年");
        dataMonth.setText(month);
        //请求数据前清空数据
        adapter.clear();
        tOutcome.setText("0.00");
        tIncome.setText("0.00");
        //请求某年某月数据
        HttpUtils.getMonthBills(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Gson gson = new Gson();
                data = gson.fromJson(msg.obj.toString(), MonthDetailBean.class);
                //status==100:处理成功！
                if (data.getStatus() == 100) {
                    tOutcome.setText(data.getT_outcome());
                    tIncome.setText(data.getT_income());
                    list = data.getDaylist();
                    adapter.setmDatas(list);
                    adapter.notifyAllSectionsDataSetChanged();//需调用此方法刷新
                }
            }
        }, userid, year, month);
    }


    @OnClick({R.id.float_btn, R.id.layout_data,R.id.top_ll_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.float_btn:
                if(Constants.currentUserId==0) {
                    Toast.makeText(getContext(), "请先登陆", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getContext(), AddBillActivity.class);
                    startActivityForResult(intent, 0);
                }
                break;
            case R.id.layout_data:
                //时间选择器
                new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        setYear=DateUtils.date2Str(date, "yyyy");
                        setMonth=DateUtils.date2Str(date, "MM");
                        setBillData(Constants.currentUserId,setYear,setMonth);
                    }
                }).setRangDate(null, Calendar.getInstance())
                        .setType(new boolean[]{true, true, false, false, false, false})
                        .build()
                        .show();
                break;
            case R.id.top_ll_out:

                break;
        }
    }

    //AdBillActivity返回值
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            setBillData(Constants.currentUserId,setYear,setMonth);
        }
    }
}
