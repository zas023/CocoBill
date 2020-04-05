package com.thmub.app.billkeeper.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.base.BaseActivity;
import com.thmub.app.billkeeper.bean.entity.GroupCategory;
import com.thmub.app.billkeeper.bean.entity.GroupDaily;
import com.thmub.app.billkeeper.bean.entity.GroupType;
import com.thmub.app.billkeeper.bean.sql.LocalDatabase;
import com.thmub.app.billkeeper.constant.Constant;
import com.thmub.app.billkeeper.util.ChartUtils;
import com.thmub.app.billkeeper.util.DateUtils;
import com.thmub.app.billkeeper.util.ResUtils;
import com.thmub.app.billkeeper.util.UIUtils;
import com.thmub.app.billkeeper.util.ValidationUtils;
import com.thmub.app.billkeeper.widget.chart.MBarChart;
import com.thmub.app.billkeeper.widget.chart.MPieChart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Enosh on 2020-03-29
 * Github: https://github.com/zas023
 * <p>
 * 账单统计页面
 */
public class StatisticActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.chart_bar)
    MBarChart barChart;
    @BindView(R.id.chart_pie)
    MPieChart pieChart;


    @BindView(R.id.tv_bar_outcome)
    TextView tvBarOutcome;
    @BindView(R.id.tv_bar_income)
    TextView tvBarIncome;

    @BindView(R.id.tv_pie_outcome)
    TextView tvPieOutcome;
    @BindView(R.id.tv_pie_income)
    TextView tvPieIncome;

    @BindView(R.id.tv_info_date)
    TextView tvInfoDate;
    @BindView(R.id.tv_info_out)
    TextView tvInfoOut;
    @BindView(R.id.tv_info_in)
    TextView tvInfoIn;
    @BindView(R.id.tv_info_balance)
    TextView tvInfoBalance;

    private List<GroupType> summaryDatas;

    private List<GroupDaily> barOutcomeDates;
    private List<GroupDaily> barIncomeDates;

    private List<GroupCategory> pieOutcomeDates;
    private List<GroupCategory> pieIncomeDates;

    private long startDate;
    private long endDate;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_statistic;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        // 当月时间段
        long curDate = System.currentTimeMillis();
        startDate = DateUtils.getMonthStartTime(curDate);
        endDate = DateUtils.getMonthEndTime(curDate);

        // 初始话数据集合
        barOutcomeDates = new ArrayList<>();
        barIncomeDates = new ArrayList<>();
    }

    @Override
    protected void initWidget() {
        toolbar.setTitle(R.string.activity_statistic);

        // 设置收支Tag
        tvBarOutcome.setSelected(true);
        tvBarIncome.setSelected(false);

        tvPieOutcome.setSelected(true);
        tvPieIncome.setSelected(false);

        // 初始柱状图
        ChartUtils.setBarChat(mContext, barChart);
        // 初始饼状图
        ChartUtils.setPieChat(mContext, pieChart);
    }

    @Override
    protected void initClick() {
        // 标题栏返回
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.tv_bar_income, R.id.tv_bar_outcome, R.id.tv_pie_outcome, R.id.tv_pie_income})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_bar_income:
                if (!tvBarIncome.isSelected()) {
                    tvBarIncome.setSelected(true);
                    tvBarOutcome.setSelected(false);
                    showBarChart();
                }
                break;
            case R.id.tv_bar_outcome:
                if (!tvBarOutcome.isSelected()) {
                    tvBarIncome.setSelected(false);
                    tvBarOutcome.setSelected(true);
                    showBarChart();
                }
                break;
            case R.id.tv_pie_income:
                if (!tvPieIncome.isSelected()) {
                    tvPieIncome.setSelected(true);
                    tvPieOutcome.setSelected(false);
                    showPieChart();
                }
                break;
            case R.id.tv_pie_outcome:
                if (!tvPieOutcome.isSelected()) {
                    tvPieIncome.setSelected(false);
                    tvPieOutcome.setSelected(true);
                    showPieChart();
                }
                break;
        }
    }

    @Override
    protected void processLogic() {

        // 总量
        summaryDatas = LocalDatabase.getInstance().recordDao().queryGroupByType(startDate, endDate);
        setSummaryData();

        // 查询每日收支统计
        List<GroupDaily> outcomeList = LocalDatabase.getInstance().recordDao().queryGroupByDaily(startDate, endDate, Constant.TYPE_OUTCOME);
        List<GroupDaily> incomeList = LocalDatabase.getInstance().recordDao().queryGroupByDaily(startDate, endDate, Constant.TYPE_INCOME);

        // 补全自然月日期
        completeEmptyDailyData(outcomeList, incomeList);

        // 查询分类收支统计
        pieOutcomeDates = LocalDatabase.getInstance().recordDao().queryGroupByCategory(startDate, endDate, Constant.TYPE_OUTCOME);
        pieIncomeDates = LocalDatabase.getInstance().recordDao().queryGroupByCategory(startDate, endDate, Constant.TYPE_INCOME);

        // 展示柱状图
        showBarChart();

        // 展示饼状图
        showPieChart();
    }

    /**
     * 设置总量统计
     */
    private void setSummaryData() {
        // 补全信息
        if (ValidationUtils.isEmpty(summaryDatas)) {
            summaryDatas.add(new GroupType(0, Constant.TYPE_OUTCOME, 0F, startDate));
            summaryDatas.add(new GroupType(0, Constant.TYPE_INCOME, 0F, startDate));
        }
        if (summaryDatas.size() == 1) {
            summaryDatas.add(new GroupType(0, Constant.TYPE_INCOME - summaryDatas.get(0).getType(), 0F, startDate));
        }
        tvInfoDate.setText(DateUtils.getDateText(summaryDatas.get(0).getDate(), DateUtils.FORMAT_YEAR_MONTH) + "账单");
        tvInfoOut.setText("支出" + summaryDatas.get(0).getCount() + "笔，共 ¥" + summaryDatas.get(0).getAmount());
        tvInfoIn.setText("收入" + summaryDatas.get(1).getCount() + "笔，共 ¥" + summaryDatas.get(1).getAmount());
        tvInfoBalance.setText("结余 ¥" + (summaryDatas.get(1).getAmount() - summaryDatas.get(0).getAmount()));
    }

    /**
     * 补全起止日期内日记录数据，若有自然日没有记录，以空数据补全
     */
    private void completeEmptyDailyData(List<GroupDaily> outcomeList, List<GroupDaily> incomeList) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH) + 1;
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(endDate);
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);

        barOutcomeDates.clear();
        barIncomeDates.clear();

        // 遍历时间区间每个自然日，补充空数据
        int monthMaxDayCount = DateUtils.getDaysOfMonth(startDate);
        for (int day = startDay; day <= endDay; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            GroupDaily dailyData = new GroupDaily();
            dailyData.setDate(calendar.getTimeInMillis());
            dailyData.setAmount(0);
            dailyData.setCount(0);
            barOutcomeDates.add(dailyData);
            barIncomeDates.add(dailyData);
        }

        // 填充现有数据
        for (GroupDaily daily : outcomeList) {
            int dayIndex = DateUtils.getDayOfDate(daily.getDate()) - 1;
            barOutcomeDates.set(dayIndex, daily);
        }
        for (GroupDaily daily : incomeList) {
            int dayIndex = DateUtils.getDayOfDate(daily.getDate()) - 1;
            barIncomeDates.set(dayIndex, daily);
        }
    }

    /**
     * 显示柱状图
     */
    private void showBarChart() {

        List<GroupDaily> list = tvBarOutcome.isSelected() ? barOutcomeDates : barIncomeDates;

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            GroupDaily dailyData = list.get(i);
            BarEntry barEntry = new BarEntry(i, dailyData.getAmount());
            barEntry.setData(dailyData);
            entries.add(barEntry);
        }

        BarData barData;
        List<IBarDataSet> sets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setColor(Color.GRAY);
        barDataSet.setDrawValues(false);
        barDataSet.setFormLineWidth(0);
        barDataSet.setBarShadowColor(ResUtils.getColor(this, R.color.chartGridLine));
        barDataSet.setColor(tvBarOutcome.isSelected() ? UIUtils.getColor(mContext, R.color.icBgOutcome)
                : UIUtils.getColor(mContext, R.color.icBgIncome));
        sets.add(barDataSet);
        barData = new BarData(sets);
        barData.setBarWidth(0.5f);

        // 设置X轴坐标
        ChartUtils.setBarChartXAxis(mContext, barChart, barOutcomeDates);

        // 设置Y轴坐标
        ChartUtils.setBarChartYAxis(mContext, barChart, null);

        // 设置数据
        barChart.setData(barData);
        barChart.animateY(500);
    }

    /**
     * 显示饼状图
     */
    private void showPieChart() {

        List<GroupCategory> list = tvPieOutcome.isSelected() ? pieOutcomeDates : pieIncomeDates;
        List<PieEntry> pieEntryList = new ArrayList<>();

        for (GroupCategory groupCategory : list) {
            pieEntryList.add(new PieEntry((float) groupCategory.getAmount(), groupCategory.getCategoryName()));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
        // todo 为每个分类设计一种底色 like timi
        int[] colors = getResources().getIntArray(R.array.pieChartColors);
        pieDataSet.setColors(colors);
        ArrayList<Integer> colorList = new ArrayList<>(colors.length);
        for (int color : colors) {
            colorList.add(color);
        }

        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLineColor(ResUtils.getColor(mContext, R.color.textColorPrimary));
        pieDataSet.setValueTextColors(colorList);
        pieDataSet.setValueTextSize(9);
        // pieDataSet.setValueTypeface(Typeface.createFromAsset(getAssets(), "font/Quicksand-Regular.ttf"));
        pieDataSet.setValueLineVariableLength(true);
        pieDataSet.setValueFormatter(new ValueFormatter() {

            DecimalFormat percentFormat = new DecimalFormat("0.00");

            @Override
            public String getFormattedValue(float value) {
                return percentFormat.format(value) + "%";
            }
        });

        PieData pieData = new PieData(pieDataSet);

        // 设置数据
        pieChart.setData(pieData);
        pieChart.animateY(1400, Easing.EaseInOutQuart);
    }

}
