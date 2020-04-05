package com.thmub.app.billkeeper.widget.chart;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.bean.entity.GroupDaily;
import com.thmub.app.billkeeper.util.DateUtils;

/**
 * Created by Enosh on 2020-03-27
 * Github: https://github.com/zas023
 * <p>
 * 月账单柱状图每日的数据
 */
public class MarkerViewDailyData extends MMarkerView {

    private TextView mTvData;
    private TextView mTvDate;
    private Entry mEntry;
    private OnClickListener mListener;

    public MarkerViewDailyData(Context context) {
        super(context, R.layout.item_chart_maker_view);
        mTvData = findViewById(R.id.tvData);
        mTvDate = findViewById(R.id.tvDate);
        setOnClickListener(v -> {
            if (mListener != null && mEntry != null) {
                mListener.onClick(v, mEntry);
            }
        });
    }

    public void setOnClickListener(OnClickListener l) {
        mListener = l;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        mEntry = e;
        GroupDaily dailyData = (GroupDaily) e.getData();
        String dayInfo = DateUtils.getDateText(dailyData.getDate(), DateUtils.FORMAT_YEAR_MONTH_DAY);
        String moneyInfo = "¥" + dailyData.getAmount();
        mTvDate.setText(dayInfo);
        mTvData.setText(moneyInfo);
        super.refreshContent(e, highlight);
    }

    public interface OnClickListener {
        /**
         * 点击回调
         *
         * @param view  view
         * @param entry 数据
         */
        void onClick(View view, Entry entry);
    }
}
