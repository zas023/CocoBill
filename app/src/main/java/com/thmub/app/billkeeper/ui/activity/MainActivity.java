package com.thmub.app.billkeeper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.SmartUtil;
import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.base.BaseActivity;
import com.thmub.app.billkeeper.bean.entity.GroupType;
import com.thmub.app.billkeeper.bean.entity.Record;
import com.thmub.app.billkeeper.bean.sql.LocalDatabase;
import com.thmub.app.billkeeper.constant.Constant;
import com.thmub.app.billkeeper.ui.adapter.RecordHomeAdapter;
import com.thmub.app.billkeeper.util.DateUtils;
import com.thmub.app.billkeeper.util.SharePreUtils;
import com.thmub.app.billkeeper.util.StatusBarUtil;
import com.thmub.app.billkeeper.util.UIUtils;
import com.thmub.app.billkeeper.util.ValidationUtils;
import com.thmub.app.billkeeper.widget.CirclePercentView;
import com.thmub.app.billkeeper.widget.MTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Enosh on 2020-03-07
 * Github: https://github.com/zas023
 * <p>
 * 主页
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.parallax)
    ImageView parallax;
    @BindView(R.id.header)
    ClassicsHeader header;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvMonthExpense)
    MTextView tvMonthExpense;
    @BindView(R.id.tv_info_outcome)
    MTextView tvInfoOutcome;
    @BindView(R.id.tv_info_income_label)
    MTextView tvInfoIncomeLabel;
    @BindView(R.id.tv_info_income)
    MTextView tvInfoIncome;
    @BindView(R.id.tv_info_balance_label)
    MTextView tvInfoBalanceLabel;
    @BindView(R.id.tv_info_balance)
    MTextView tvInfoBalance;

    @BindView(R.id.cp_progress)
    CirclePercentView percentView;
    @BindView(R.id.tv_average)
    MTextView tvAverage;
    @BindView(R.id.tv_budget_date)
    MTextView tvBudgetDate;
    @BindView(R.id.tv_remain)
    MTextView tvRemain;

    // ClassicsHeader移动距离
    private int mOffset = 0;
    private int mScrollY = 0;

    private RecordHomeAdapter adapter;
    private List<Record> records;
    private long startDate;
    private long endDate;
    private int days = 1;

    private List<GroupType> summaryData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        records = new ArrayList<>();
        adapter = new RecordHomeAdapter(records);
        long curDate = System.currentTimeMillis();
        startDate = DateUtils.getMonthStartTime(curDate);
        endDate = DateUtils.getMonthEndTime(curDate);
        // 当月剩余天数
        days = DateUtils.getDaysOfMonth(curDate) - DateUtils.getDayOfDate(curDate);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);
        StatusBarUtil.setMargin(this, findViewById(R.id.header));

        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        // 下拉刷新
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                processLogic();
                refreshLayout.finishRefresh();
            }

            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                // mOffset = offset / 2;
                mOffset = offset;
                // parallax.setTranslationY(mOffset - mScrollY);
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
            }
        });

        // adapter滑动监听
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            private int lastScrollY = 0;
            private int h = SmartUtil.dp2px(150);
            private int color = UIUtils.getColor(mContext, R.color.colorPrimary) & 0x00ffffff;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                    // parallax.setTranslationY(mOffset - mScrollY);
                }
                lastScrollY = scrollY;
            }
        });
        toolbar.setBackgroundColor(0);

        // item选择
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, RecordDetailActivity.class);
            Record record = (Record) adapter.getItem(position);
            intent.putExtra(RecordDetailActivity.EXTRA_RECORD_ID_KEY, record.getId());
            startActivity(intent);
        });
    }

    @OnClick({R.id.fab, R.id.action_statistic, R.id.action_search, R.id.view_budget})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:  // add
                startActivity(new Intent(mContext, RecordEditActivity.class));
                break;
            case R.id.action_search:  //search
                startActivity(new Intent(mContext, SearchActivity.class));
                break;
            case R.id.action_statistic:  //statistic
                startActivity(new Intent(mContext, StatisticActivity.class));
                break;
            case R.id.view_budget:  // budget
                startActivity(new Intent(mContext, BudgetActivity.class));
                break;
        }
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        // 当月信息
        summaryData = LocalDatabase.getInstance().recordDao().queryGroupByType(startDate, endDate);
        setSummaryData();
        // 账单列表
        records.clear();
        records.addAll(LocalDatabase.getInstance().recordDao().queryAll(startDate, endDate, 1000, 0));
        adapter.setItemType(records);
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置当月信息
     */
    private void setSummaryData() {
        // 补全信息
        if (ValidationUtils.isEmpty(summaryData)) {
            summaryData.add(new GroupType(0, Constant.TYPE_OUTCOME, 0F, startDate));
            summaryData.add(new GroupType(0, Constant.TYPE_INCOME, 0F, startDate));
        }
        if (summaryData.size() == 1) {
            summaryData.add(new GroupType(0, Constant.TYPE_INCOME - summaryData.get(0).getType(), 0F, startDate));
        }
        // 收支
        tvInfoOutcome.setText("¥" + summaryData.get(0).getAmount());
        tvInfoIncome.setText("¥" + summaryData.get(1).getAmount());
        tvInfoBalance.setText("¥" + (summaryData.get(1).getAmount() - summaryData.get(0).getAmount()));
        // 预算
        String strBudget = SharePreUtils.getString(mContext, Constant.SP_KEY_BUDGET);
        if (ValidationUtils.isEmpty(strBudget)) {
            strBudget = "0";
        }
        float budget = Float.parseFloat(strBudget);
        percentView.setPercentage(summaryData.get(0).getAmount() / (1 + budget) * 100);
        tvRemain.setText(String.valueOf(budget - summaryData.get(0).getAmount()));
        tvAverage.setText("¥" + (Math.max(0, (budget - summaryData.get(0).getAmount())) / days));
        tvBudgetDate.setText(String.valueOf(days));
    }

    /*************************LifeCycle***********************/
    /**
     * Activity恢复前台
     */
    @Override
    protected void onResume() {
        super.onResume();
        // 刷新数据（不加载刷新动画）
        processLogic();
    }
}
