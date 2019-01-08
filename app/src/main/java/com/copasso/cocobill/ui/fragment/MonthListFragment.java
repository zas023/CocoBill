package com.copasso.cocobill.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.copasso.cocobill.R;
import com.copasso.cocobill.base.BaseMVPFragment;
import com.copasso.cocobill.common.Constants;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.local.MonthListBean;
import com.copasso.cocobill.presenter.MonthListPresenter;
import com.copasso.cocobill.presenter.contract.MonthListContract;
import com.copasso.cocobill.ui.adapter.MonthListAdapter;
import com.copasso.cocobill.utils.DateUtils;
import com.copasso.cocobill.utils.SnackbarUtils;
import com.copasso.cocobill.widget.stickyheader.StickyHeaderGridLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import static com.copasso.cocobill.utils.DateUtils.FORMAT_M;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_Y;

/**
 * Created by Zhouas666 on 2019-01-08
 * Github: https://github.com/zas023
 */
public class MonthListFragment extends BaseMVPFragment<MonthListContract.Presenter>
        implements MonthListContract.View {

    //    @BindView(R.id.data_year)
    //    SwipeRefreshLayout swipe;
//    @BindView(R.id.swipe)
//    TextView dataYear;
//    @BindView(R.id.data_month)
//    TextView dataMonth;
//    @BindView(R.id.layout_data)
//    LinearLayout layoutData;
//    @BindView(R.id.top_ll_out)
//    LinearLayout layoutOutin;
    private TextView tOutcome;
    private TextView tIncome;
    private TextView tTotal;
    private RecyclerView rvList;
    private FloatingActionButton floatBtn;

    private static final int SPAN_SIZE = 1;
    private StickyHeaderGridLayoutManager mLayoutManager;
    private MonthListAdapter adapter;
    private List<MonthListBean.DaylistBean> list=null;

    int part, index;

    private String setYear = DateUtils.getCurYear(FORMAT_Y);
    private String setMonth = DateUtils.getCurMonth(FORMAT_M);

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_month_list;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mLayoutManager = new StickyHeaderGridLayoutManager(SPAN_SIZE);
        mLayoutManager.setHeaderBottomOverlapMargin(5);
        adapter = new MonthListAdapter(mContext, list);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        rvList = getViewById(R.id.rv_list);
        floatBtn = getViewById(R.id.float_btn);
        tOutcome = getViewById(R.id.t_outcome);
        tIncome = getViewById(R.id.t_income);
        tTotal = getViewById(R.id.t_total);

        rvList.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                dispatchRemoveFinished(holder);
                return false;
            }
        });
        rvList.setLayoutManager(mLayoutManager);
        rvList.setAdapter(adapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        //adapter的侧滑选项事件监听
        adapter.setOnStickyHeaderClickListener(new MonthListAdapter.OnStickyHeaderClickListener(){
            @Override
            public void OnDeleteClick(BBill item, int section, int offset) {
                item.setVersion(-1);
                //将删除的账单版本号设置为负，而非直接删除
                //便于同步删除服务器数据
                mPresenter.updateBill(item);
                part = section;
                index = offset;
            }

            @Override
            public void OnEditClick(BBill item, int section, int offset) {

            }
        });
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        //请求当月数据
        mPresenter.getMonthList(Constants.currentUserId, setYear, setMonth);
    }

    /*****************************************************************************/

    @Override
    protected MonthListContract.Presenter bindPresenter() {
        return new MonthListPresenter();
    }

    @Override
    public void loadDataSuccess(MonthListBean monthListBean) {
        tOutcome.setText(monthListBean.getT_outcome());
        tIncome.setText(monthListBean.getT_income());
        tTotal.setText(monthListBean.getT_total());
        list = monthListBean.getDaylist();
        adapter.setmDatas(list);
        adapter.notifyAllSectionsDataSetChanged();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(Throwable e) {
        SnackbarUtils.show(mActivity, e.getMessage());
    }
}
