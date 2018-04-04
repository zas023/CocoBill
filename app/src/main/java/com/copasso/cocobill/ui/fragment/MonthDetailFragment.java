package com.copasso.cocobill.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;
import com.bigkoo.pickerview.TimePickerView;
import com.copasso.cocobill.R;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.event.SyncEvent;
import com.copasso.cocobill.ui.activity.BillAddActivity;
import com.copasso.cocobill.ui.activity.BillEditActivity;
import com.copasso.cocobill.ui.adapter.MonthDetailAdapter;
import com.copasso.cocobill.model.bean.BaseBean;
import com.copasso.cocobill.model.bean.local.MonthDetailBean;
import com.copasso.cocobill.mvp.presenter.Imp.MonthDetailPresenterImp;
import com.copasso.cocobill.mvp.presenter.MonthDetailPresenter;
import com.copasso.cocobill.widget.stickyheader.StickyHeaderGridLayoutManager;
import com.copasso.cocobill.common.Constants;
import com.copasso.cocobill.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.copasso.cocobill.utils.SnackbarUtils;
import com.copasso.cocobill.mvp.view.MonthDetailView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.copasso.cocobill.utils.DateUtils.FORMAT_M;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_Y;

/**
 * 明细
 */
public class MonthDetailFragment extends BaseFragment implements MonthDetailView {

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

    int part, index;

    private String setYear = DateUtils.getCurYear(FORMAT_Y);
    private String setMonth = DateUtils.getCurMonth(FORMAT_M);

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(SyncEvent event) {
        if (event.getState()==100)
            getBills(Constants.currentUserId, setYear, setMonth);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_menu_detail;
    }

    @Override
    protected void initEventAndData() {
        //注册 EventBus
        EventBus.getDefault().register(this);

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

        //adapter的侧滑选项事件监听
        adapter.setOnStickyHeaderClickListener(new MonthDetailAdapter.OnStickyHeaderClickListener() {
            @Override
            public void OnDeleteClick(BBill item, int section, int offset) {
                item.setVersion(-1);
                //将删除的账单版本号设置为负，而非直接删除
                //便于同步删除服务器数据
                presenter.updateBill(item);
                part = section;
                index = offset;
            }

            @Override
            public void OnEditClick(BBill item, int section, int offset) {
                Intent intent = new Intent(mContext, BillEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", item.getId());
                bundle.putString("rid", item.getRid());
                bundle.putString("sortName", item.getSortName());
                bundle.putString("payName", item.getPayName());
                bundle.putString("content", item.getContent());
                bundle.putDouble("cost", item.getCost());
                bundle.putLong("date", item.getCrdate());
                bundle.putBoolean("income", item.isIncome());
                bundle.putInt("version", item.getVersion());
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, 0);
            }
        });

        presenter = new MonthDetailPresenterImp(this);
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

        dataYear.setText(year + " 年");
        dataMonth.setText(month);
        //请求数据前清空数据
        adapter.clear();
        tOutcome.setText("0.00");
        tIncome.setText("0.00");
        //请求某年某月数据
        presenter.getMonthDetailBills(Constants.currentUserId, setYear, setMonth);
    }

    /**
     * 数据请求成功
     *
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
     * 删除账单成功
     *
     * @param tData
     */
    @Override
    public void loadDataSuccess(BaseBean tData) {
        adapter.remove(part, index);
    }

    /**
     * 抛出异常
     *
     * @param throwable
     */
    @Override
    public void loadDataError(Throwable throwable) {
        SnackbarUtils.show(mActivity, throwable.getMessage());
    }


    /**
     * 监听点击事件
     *
     * @param view
     */
    @OnClick({R.id.float_btn, R.id.layout_data, R.id.top_ll_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.float_btn:  //添加
                if (BmobUser.getCurrentUser()==null)
                    SnackbarUtils.show(mContext,"请先登录");
                else{
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
    public void showTimePicker() {
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

    @Override
    protected void beforeDestroy() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * Activity返回
     *
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
