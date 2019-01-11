package com.copasso.cocobill.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.copasso.cocobill.MyApplication;
import com.copasso.cocobill.R;
import com.copasso.cocobill.base.BaseMVPFragment;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.local.MonthChartBean;
import com.copasso.cocobill.presenter.MonthChartPresenter;
import com.copasso.cocobill.presenter.contract.MonthChartContract;
import com.copasso.cocobill.ui.adapter.binder.MonthChartBillViewBinder;
import com.copasso.cocobill.utils.DateUtils;
import com.copasso.cocobill.utils.PieChartUtils;
import com.copasso.cocobill.utils.SnackbarUtils;
import com.copasso.cocobill.widget.CircleImageView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.copasso.cocobill.utils.DateUtils.FORMAT_M;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_Y;

/**
 * Created by Zhouas666 on 2019-01-09
 * Github: https://github.com/zas023
 */
public class MonthChartFragment extends BaseMVPFragment<MonthChartContract.Presenter>
        implements MonthChartContract.View, OnChartValueSelectedListener, View.OnClickListener {

    private PieChart mChart;
    private TextView centerTitle;
    private TextView centerMoney;
    private LinearLayout layoutCenter;
    private ImageView centerImg;
    private CircleImageView circleBg;
    private ImageView circleImg;
    private RelativeLayout layoutCircle;
    private TextView title;
    private TextView money;
    private TextView rankTitle;
    private RelativeLayout layoutOther;
    private TextView otherMoney;
    private SwipeRefreshLayout swipe;
    private RelativeLayout itemType;
    private RelativeLayout itemOther;
    private RecyclerView rvList;
    private LinearLayout layoutTypedata;

    private boolean TYPE = true;//默认总收入true
    private List<MonthChartBean.SortTypeList> tMoneyBeanList;
    private String sort_image;//饼状图与之相对应的分类图片地址
    private String sort_name;
    private String back_color;

    private MonthChartBean monthChartBean;

    private MultiTypeAdapter adapter;

    private String setYear = DateUtils.getCurYear(FORMAT_Y);
    private String setMonth = DateUtils.getCurMonth(FORMAT_M);

    /*****************************************************************************/

    public void changeDate(String yyyy, String mm) {
        setYear=yyyy;
        setMonth=mm;
        mPresenter.getMonthChart(MyApplication.getCurrentUserId(),setYear,setMonth);
    }

    /*****************************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_month_chart;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        mChart = getViewById(R.id.chart);
        centerTitle = getViewById(R.id.center_title);
        centerMoney = getViewById(R.id.center_money);
        layoutCenter = getViewById(R.id.layout_center);
        centerImg = getViewById(R.id.center_img);
        circleBg = getViewById(R.id.circle_bg);
        circleImg = getViewById(R.id.circle_img);
        layoutCircle = getViewById(R.id.layout_circle);
        title = getViewById(R.id.title);
        money = getViewById(R.id.money);
        rankTitle = getViewById(R.id.rank_title);
        layoutOther = getViewById(R.id.layout_other);
        otherMoney = getViewById(R.id.other_money);
        swipe = getViewById(R.id.swipe);
        itemType = getViewById(R.id.item_type);
        itemOther = getViewById(R.id.item_other);
        rvList = getViewById(R.id.rv_list);
        layoutTypedata = getViewById(R.id.layout_typedata);


        //初始化饼状图
        PieChartUtils.initPieChart(mChart);
        //设置圆盘是否转动，默认转动
        mChart.setRotationEnabled(true);
        mChart.setOnChartValueSelectedListener(this);
        //改变加载显示的颜色
        swipe.setColorSchemeColors(getResources().getColor(R.color.text_red), getResources().getColor(R.color.text_red));
        //设置向下拉多少出现刷新
        swipe.setDistanceToTriggerSync(200);
        //设置刷新出现的位置
        swipe.setProgressViewEndTarget(false, 200);
        swipe.setOnRefreshListener(()->{
            swipe.setRefreshing(false);
            mPresenter.getMonthChart(MyApplication.getCurrentUserId(), setYear, setMonth);
        });

        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MultiTypeAdapter();
        adapter.register(BBill.class, new MonthChartBillViewBinder(mContext));
        rvList.setAdapter(adapter);

    }

    @Override
    protected void initClick() {
        super.initClick();
        layoutCenter.setOnClickListener(this);
        itemType.setOnClickListener(this);
        itemOther.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.getMonthChart(MyApplication.getCurrentUserId(), setYear, setMonth);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_center:  //图表中心键
                TYPE = !TYPE;
                setReportData();
                break;
            case R.id.item_type:
                break;
            case R.id.item_other:
                break;
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        int entryIndex = (int) h.getX();
        PieChartUtils.setRotationAngle(mChart, entryIndex);
        setNoteData(entryIndex,e.getY());
    }


    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    /**
     * 报表数据
     */
    private void setReportData() {

        if (monthChartBean == null) {
            return;
        }

        float totalMoney;
        if (TYPE) {
            centerTitle.setText("总支出");
            centerImg.setImageResource(R.mipmap.tallybook_output);
            tMoneyBeanList = monthChartBean.getOutSortlist();
            totalMoney = monthChartBean.getTotalOut();
        } else {
            centerTitle.setText("总收入");
            centerImg.setImageResource(R.mipmap.tallybook_input);
            tMoneyBeanList = monthChartBean.getInSortlist();
            totalMoney = monthChartBean.getTotalIn();
        }

        centerMoney.setText("" + totalMoney);

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        if (tMoneyBeanList != null && tMoneyBeanList.size() > 0) {
            layoutTypedata.setVisibility(View.VISIBLE);
            for (int i = 0; i < tMoneyBeanList.size(); i++) {
                float scale =tMoneyBeanList.get(i).getMoney() / totalMoney;
                float value = (scale < 0.06f) ? 0.06f : scale;
                entries.add(new PieEntry(value, PieChartUtils.getDrawable(tMoneyBeanList.get(i).getSortImg())));
                colors.add(Color.parseColor(tMoneyBeanList.get(i).getBack_color()));
            }
            setNoteData(0,entries.get(0).getValue());
        } else {//无数据时的显示
            layoutTypedata.setVisibility(View.GONE);
            entries.add(new PieEntry(1f));
            colors.add(0xffAAAAAA);
        }

        PieChartUtils.setPieChartData(mChart, entries, colors);
    }

    /**
     * 点击饼状图上区域后相应的数据设置
     *
     * @param index
     */
    private void setNoteData(int index, float value) {
        if (null==tMoneyBeanList||tMoneyBeanList.size()==0)
            return;
        sort_image = tMoneyBeanList.get(index).getSortImg();
        sort_name = tMoneyBeanList.get(index).getSortName();
        back_color = tMoneyBeanList.get(index).getBack_color();
        if (TYPE) {
            money.setText("-" + tMoneyBeanList.get(index).getMoney());
        } else {
            money.setText("+" + tMoneyBeanList.get(index).getMoney());
        }
        DecimalFormat df = new DecimalFormat("0.00%");
        title.setText(sort_name+" : "+df.format(value));
        rankTitle.setText(sort_name + "排行榜");
        circleBg.setImageDrawable(new ColorDrawable(Color.parseColor(back_color)));
        circleImg.setImageDrawable(PieChartUtils.getDrawable(tMoneyBeanList.get(index).getSortImg()));

//        adapter.setSortName(sort_name);
        adapter.setItems(tMoneyBeanList.get(index).getList());
        adapter.notifyDataSetChanged();
    }

    /*****************************************************************************/
    @Override
    protected MonthChartContract.Presenter bindPresenter() {
        return new MonthChartPresenter();
    }

    @Override
    public void loadDataSuccess(MonthChartBean bean) {
        monthChartBean=bean;
        setReportData();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(Throwable e) {
        SnackbarUtils.show(mActivity,e.getMessage());
    }

}
