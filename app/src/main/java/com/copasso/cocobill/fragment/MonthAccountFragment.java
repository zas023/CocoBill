package com.copasso.cocobill.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;
import butterknife.OnClick;
import com.bigkoo.pickerview.TimePickerView;
import com.copasso.cocobill.R;
import com.copasso.cocobill.adapter.AccountCardAdapter;
import com.copasso.cocobill.bean.MonthAccountBean;
import com.copasso.cocobill.common.Constants;
import com.copasso.cocobill.utils.DateUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import com.copasso.cocobill.utils.OkHttpUtils;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.copasso.cocobill.utils.DateUtils.FORMAT_M;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_Y;

/**
 * 我的账户
 */
public class MonthAccountFragment extends BaseFragment {

    @BindView(R.id.data_year)
    TextView dataYear;
    @BindView(R.id.data_month)
    TextView dataMonth;
    @BindView(R.id.layout_data)
    LinearLayout layoutData;
    @BindView(R.id.t_outcome)
    TextView tOutcome;
    @BindView(R.id.t_income)
    TextView tIncome;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;


    private AccountCardAdapter adapter;

    private MonthAccountBean monthAccountBean;
    private List<MonthAccountBean.PayTypeListBean> list;

    private String setYear = DateUtils.getCurYear(FORMAT_Y);
    private String setMonth = DateUtils.getCurMonth(FORMAT_M);


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_menu_account;
    }


    @Override
    protected void initEventAndData() {

        dataYear.setText(DateUtils.getCurYear("yyyy 年"));
        dataMonth.setText(DateUtils.date2Str(new Date(), "MM"));
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
                setAcountData(Constants.currentUserId, setYear, setMonth);
            }
        });

        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AccountCardAdapter(getActivity(), list);
        adapter.setmListener(new AccountCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });
        rvList.setAdapter(adapter);

        //请求当月数据
        setAcountData(Constants.currentUserId, setYear, setMonth);
    }


    /**
     * 获取账单数据
     * @param userid
     * @param year
     * @param month
     */
    private void setAcountData(final int userid, String year, String month) {
        if (userid==0){
            Toast.makeText(getContext(), "请先登陆", Toast.LENGTH_SHORT).show();
            return;
        }
        dataYear.setText(setYear + " 年");
        dataMonth.setText(setMonth);
        OkHttpUtils.getInstance().get(Constants.BASE_URL + Constants.BILL_MONTH_CARD
                        + "/" + userid + "/" + year + "/" + month,
                null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson gson = new Gson();
                        monthAccountBean = gson.fromJson(response.body().string(), MonthAccountBean.class);
                        //data不为空且status==100:处理成功！
                        if (monthAccountBean!=null && monthAccountBean.getStatus() == 100) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tOutcome.setText(monthAccountBean.getTotalOut());
                                    tIncome.setText(monthAccountBean.getTotalIn());
                                    list = monthAccountBean.getList();
                                    adapter.setmDatas(list);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
    }

    @OnClick({R.id.layout_data,R.id.top_ll_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_data:
                //时间选择器
                new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        setYear=DateUtils.date2Str(date, "yyyy");
                        setMonth=DateUtils.date2Str(date, "MM");
                        setAcountData(Constants.currentUserId,setYear,setMonth);
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

}
