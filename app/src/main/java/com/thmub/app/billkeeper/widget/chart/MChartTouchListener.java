package com.thmub.app.billkeeper.widget.chart;

import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;

/**
 * Created by Enosh on 2020-03-27
 * Github: https://github.com/zas023
 */
public class MChartTouchListener extends BarLineChartTouchListener {

    private boolean mHandlerByMarkerView = false;
    private float mDownX;
    private float mDownY;

    /**
     * Constructor with initialization parameters.
     *
     * @param chart               instance of the chart
     * @param touchMatrix         the touch-matrix of the chart
     * @param dragTriggerDistance the minimum movement distance that will be interpreted as a "drag" gesture in dp (3dp equals
     */
    public MChartTouchListener(BarLineChartBase<? extends BarLineScatterCandleBubbleData<? extends
            IBarLineScatterCandleBubbleDataSet<? extends Entry>>> chart, Matrix touchMatrix, float dragTriggerDistance) {
        super(chart, touchMatrix, dragTriggerDistance);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 处理 marker view 点击
        IMarker marker = mChart.getMarker();
        if (marker instanceof MMarkerView) {
            MMarkerView markerView = (MMarkerView) marker;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:  // 按下
                    mDownX = event.getX();
                    mDownY = event.getY();
                    mHandlerByMarkerView = markerView.getBound().contains(event.getX(), event.getY());
                    if (mHandlerByMarkerView) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP: // 抬起
                    if (mHandlerByMarkerView) {
                        if (Math.abs(mDownX - event.getX()) <= 5 && Math.abs(mDownY - event.getY()) <= 5) {
                            markerView.performClick();
                        }
                        return true;
                    }
                    break;
            }
        }
        return super.onTouch(v, event);
    }
}
