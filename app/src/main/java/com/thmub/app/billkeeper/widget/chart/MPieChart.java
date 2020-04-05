package com.thmub.app.billkeeper.widget.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.PieChart;

/**
 * Created by Enosh on 2020-03-27
 * Github: https://github.com/zas023
 */
public class MPieChart extends PieChart {

    public MPieChart(Context context) {
        super(context);
    }

    public MPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MPieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mRenderer = new MPieChartRenderer(this, mAnimator, mViewPortHandler);
    }
}
