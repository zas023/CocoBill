package com.thmub.app.billkeeper.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

/**
 * Created by Enosh on 2020-03-27
 * Github: https://github.com/zas023
 */
public class MBarChart extends BarChart {

    private boolean mDrawMarkOnTop;

    public MBarChart(Context context) {
        super(context);
    }

    public MBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mChartTouchListener = new MChartTouchListener(this, mViewPortHandler.getMatrixTouch(), 3f);
        mRenderer = new MBarChartRenderer(this, mAnimator, mViewPortHandler);
    }

    public void setDrawMarkOnTop(boolean drawMarkOnTop) {
        this.mDrawMarkOnTop = drawMarkOnTop;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 参考 http://www.jianshu.com/p/fe3d109eb27e
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        if (mDrawMarkOnTop) {
            drawTopMarkers(canvas);
        } else {
            super.drawMarkers(canvas);
        }
    }

    private void drawTopMarkers(Canvas canvas) {
        if (mMarker == null) {
            return;
        }

        if (!isDrawMarkersEnabled() || !valuesToHighlight()) {
            return;
        }

        for (int i = 0; i < mIndicesToHighlight.length; i++) {

            Highlight highlight = mIndicesToHighlight[i];

            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());

            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            int entryIndex = set.getEntryIndex(e);

            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX()) {
                continue;
            }

            if (mMarker != null && mMarker instanceof View) {
                View markerView = (View) this.mMarker;

                int measuredHeight = markerView.getHeight();
                int measuredWidth = markerView.getWidth();

                float x = highlight.getDrawX() - measuredWidth / 2;
                if (!mViewPortHandler.isInBoundsLeft(x)) {
                    x = mViewPortHandler.contentLeft();
                }
                if (x + measuredWidth >= mViewPortHandler.contentRight()) {
                    x = mViewPortHandler.contentRight() - measuredWidth;
                }

                float y = mViewPortHandler.contentTop() - measuredHeight;

                // callbacks to update the content
                mMarker.refreshContent(e, highlight);

                // draw the marker
                mMarker.draw(canvas, x, y);
            }
        }
    }
}
