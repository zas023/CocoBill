package com.thmub.app.billkeeper.util;

import android.content.Context;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.bean.entity.GroupDaily;
import com.thmub.app.billkeeper.widget.chart.MBarChart;
import com.thmub.app.billkeeper.widget.chart.MPieChart;
import com.thmub.app.billkeeper.widget.chart.MarkerViewDailyData;

import java.util.List;

/**
 * Created by Enosh on 2020-03-29
 * Github: https://github.com/zas023
 */
public class ChartUtils {

    /**
     * 设置柱状图
     */
    public static void setBarChat(Context context, MBarChart barChart) {
        int px = UIUtils.dp2px(context, 16F);
        barChart.setViewPortOffsets(px, UIUtils.dp2px(context, 40F), px, px);
        MarkerViewDailyData markerView = new MarkerViewDailyData(context);
        //markerView.setOnClickListener((v, e) -> mViewModel.onDailyMarkerViewClick(self(), (DailyData) e.getData()));
        barChart.setDrawMarkOnTop(true);
        barChart.setMarker(markerView);
        barChart.setNoDataText(UIUtils.getString(context, R.string.tip_empty_chart));
        barChart.setNoDataTextColor(UIUtils.getColor(context, R.color.textColorPrimary));
        barChart.setScaleEnabled(false);
        barChart.setDrawBorders(false);
        barChart.setDrawBarShadow(true);
        barChart.setDrawGridBackground(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setDescription(null);
        barChart.getLegend().setEnabled(false);
        barChart.animateY(500);
    }

    /**
     * 设置柱状图x轴坐标
     */
    public static void setBarChartXAxis(Context context, MBarChart barChart, List<GroupDaily> list) {
        // 初始化数据
        int count = list.size();
        int colorChartGridLine = context.getResources().getColor(R.color.chartGridLine);
        // 获取当前x轴
        XAxis xAxis = barChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(colorChartGridLine);
        xAxis.setDrawGridLines(false);
        xAxis.setGridColor(colorChartGridLine);
        xAxis.setDrawAxisLine(true);
        xAxis.setLabelCount(count);
        // 设置轴坐标数据
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String format = "";
                // 只显示两端和中间日期
                if (value == 0 || value == count - 1 || value == count / 2) {
                    GroupDaily dailyData = list != null ? list.get((int) value) : null;
                    if (dailyData != null) {
                        return DateUtils.getDateText(dailyData.getDate(), DateUtils.FORMAT_MONTH_DAY_SIMPLE);
                    }
                }
                return format;
            }
        });
    }

    /**
     * 设置柱状图y轴坐标
     */
    public static void setBarChartYAxis(Context context, MBarChart barChart, List<GroupDaily> list) {
        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setAxisMinimum(0);
        axisLeft.setDrawLabels(false);
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);

        YAxis axisRight = barChart.getAxisRight();
        axisRight.setAxisMinimum(0);
        axisRight.setDrawLabels(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawAxisLine(false);
    }


    /**
     * 设置饼状图
     */
    public static void setPieChat(Context context, MPieChart pieChart) {
        pieChart.setNoDataText(UIUtils.getString(context, R.string.tip_empty_chart));
        pieChart.setNoDataTextColor(UIUtils.getColor(context, R.color.textColorPrimary));
        pieChart.setExtraTopOffset(20);
        pieChart.setExtraBottomOffset(20);
        pieChart.setUsePercentValues(true);
        pieChart.setDescription(null);
        pieChart.setCenterTextSize(20f);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.getLegend().setEnabled(false);
        pieChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        pieChart.getLegend().setWordWrapEnabled(true);
    }
}
