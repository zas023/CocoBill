package com.copasso.cocobill.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;
import com.bigkoo.pickerview.TimePickerView;
import com.copasso.cocobill.R;
import com.copasso.cocobill.activity.BillAddActivity;
import com.copasso.cocobill.activity.BillEditActivity;
import com.copasso.cocobill.adapter.MonthDetailAdapter;
import com.copasso.cocobill.bean.BaseBean;
import com.copasso.cocobill.bean.BillBean;
import com.copasso.cocobill.bean.MonthDetailBean;
import com.copasso.cocobill.presenter.Imp.MonthDetailPresenterImp;
import com.copasso.cocobill.presenter.MonthDetailPresenter;
import com.copasso.cocobill.stickyheader.StickyHeaderGridLayoutManager;
import com.copasso.cocobill.common.Constants;
import com.copasso.cocobill.utils.DateUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.copasso.cocobill.utils.OkHttpUtils;
import com.copasso.cocobill.utils.SnackbarUtils;
import com.copasso.cocobill.view.MonthDetailView;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.copasso.cocobill.utils.DateUtils.FORMAT_M;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_Y;

/**
 * -明细
 */
public class MonthDetailFragment extends BaseFragment implements MonthDetailView{

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

    private MonthDetailPresenter presenter;

    private static final int SPAN_SIZE = 1;
    private StickyHeaderGridLayoutManager mLayoutManager;
    private MonthDetailAdapter adapter;
    private List<MonthDetailBean.DaylistBean> list;
    private MonthDetailBean data;

    private String setYear = DateUtils.getCurYear(FORMAT_Y);
    private String setMonth = DateUtils.getCurMonth(FORMAT_M);

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_menu_detail;
    }

    @Override
    protected void initEventAndData() {

        initView();

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
//        rvList.setOnTouchListener(new View.OnTouchListener() {
//            private float lastX;
//            private float lastY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        lastX = event.getX();
//                        lastY = event.getY();
//                        return false;
//                    case MotionEvent.ACTION_MOVE:
//                        float x = event.getX();
//                        float y = event.getY();
//                        boolean isUp = lastY - y > 2;
//                        //一次down，只变化一次，防止一次滑动时抖动下，造成某一个的向下时,y比lastY小
//                        if (isUp) {
//                            floatBtn.setVisibility(View.GONE);
//                        } else {
//                            floatBtn.setVisibility(View.VISIBLE);
//                        }
//                        break;
//
//                    default:
//                        break;
//                }
//                return false;
//            }
//
//        });

        //adapter的侧滑选项事件监听
        adapter.setOnStickyHeaderClickListener(new MonthDetailAdapter.OnStickyHeaderClickListener() {
            @Override
            public void OnDeleteClick(int id, final int section, final int offset) {
                OkHttpUtils.getInstance().get(Constants.BASE_URL + Constants.BILL_DELETE
                                + "/" +id, null,
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Gson gson = new Gson();
                                BaseBean baseBean = gson.fromJson(response.body().string(), BaseBean.class);
                                if (baseBean.getStatus() == 100) {
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.remove(section,offset);
                                        }
                                    });
                                } else {
                                    Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void OnEditClick(BillBean item, int section, int offset) {
                Intent intent = new Intent(mContext, BillEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", item.getId());
                bundle.putInt("sortId", item.getSortid());
                bundle.putInt("payId", item.getPayid());
                bundle.putString("content", item.getContent());
                bundle.putDouble("cost", item.getCost());
                bundle.putLong("date", item.getCrdate());
                bundle.putBoolean("income", item.isIncome());
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, 0);
            }
        });

        presenter=new MonthDetailPresenterImp(this);
        //请求当月数据
        getBills(Constants.currentUserId, setYear, setMonth);
    }

    /**
     * 初始化view
     */
    private void initView() {
        dataYear.setText(setYear + " 年");
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
                getBills(Constants.currentUserId, setYear, setMonth);
            }
        });
    }

    /**
     * 获取账单数据
     *
     * @param userid
     * @param year
     * @param month
     */
    private void getBills(int userid, String year, String month) {
        if (userid == 0) {
            Toast.makeText(getContext(), "请先登陆", Toast.LENGTH_SHORT).show();
            return;
        }
        dataYear.setText(year + " 年");
        dataMonth.setText(month);
        //请求数据前清空数据
        adapter.clear();
        tOutcome.setText("0.00");
        tIncome.setText("0.00");
        //请求某年某月数据
        presenter.getMonthDetailBills(String.valueOf(Constants.currentUserId), setYear, setMonth);

    }

    /**
     * 数据请求成功
     * @param tData
     */
    @Override
    public void loadDataSuccess(MonthDetailBean tData) {
        tOutcome.setText(tData.getT_outcome());
        tIncome.setText(tData.getT_income());
        list = tData.getDaylist();
        adapter.setmDatas(list);
        adapter.notifyAllSectionsDataSetChanged();//需调用此方法刷新
    }

    /**
     * 抛出异常
     * @param throwable
     */
    @Override
    public void loadDataError(Throwable throwable) {
        SnackbarUtils.show(mActivity,throwable.getMessage());
    }


    /**
     * 监听点击事件
     * @param view
     */
    @OnClick({R.id.float_btn, R.id.layout_data, R.id.top_ll_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.float_btn:  //添加
                if (Constants.currentUserId == 0) {
                    Toast.makeText(getContext(), "请先登陆", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), BillAddActivity.class);
                    startActivityForResult(intent, 0);
                }
                break;
            case R.id.layout_data:  //日期选择
                showTimePicker();
                break;
            case R.id.top_ll_out:

                break;
        }
    }

    /**
     * 时间选择器
     */
    public void showTimePicker(){
        //时间选择器
        new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                setYear = DateUtils.date2Str(date, "yyyy");
                setMonth = DateUtils.date2Str(date, "MM");
                getBills(Constants.currentUserId, setYear, setMonth);
            }
        }).setRangDate(null, Calendar.getInstance())
                .setType(new boolean[]{true, true, false, false, false, false})
//                        .isDialog(true)//是否显示为对话框样式
                .build()
                .show();
    }

    /**
     * Activity返回
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            getBills(Constants.currentUserId, setYear, setMonth);
        }
    }

}
