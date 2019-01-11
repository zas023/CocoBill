package com.copasso.cocobill.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.copasso.cocobill.MyApplication;
import com.copasso.cocobill.R;
import com.copasso.cocobill.base.BaseMVPFragment;
import com.copasso.cocobill.model.bean.local.BBill;
import com.copasso.cocobill.model.bean.local.MonthListBean;
import com.copasso.cocobill.model.bean.remote.MyUser;
import com.copasso.cocobill.model.event.SyncEvent;
import com.copasso.cocobill.presenter.MonthListPresenter;
import com.copasso.cocobill.presenter.contract.MonthListContract;
import com.copasso.cocobill.ui.activity.BillAddActivity;
import com.copasso.cocobill.ui.adapter.MonthListAdapter;
import com.copasso.cocobill.utils.DateUtils;
import com.copasso.cocobill.utils.ProgressUtils;
import com.copasso.cocobill.utils.SnackbarUtils;
import com.copasso.cocobill.widget.stickyheader.StickyHeaderGridLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.BmobUser;

import static android.app.Activity.RESULT_OK;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_M;
import static com.copasso.cocobill.utils.DateUtils.FORMAT_Y;

/**
 * Created by Zhouas666 on 2019-01-08
 * Github: https://github.com/zas023
 */
public class MonthListFragment extends BaseMVPFragment<MonthListContract.Presenter>
        implements MonthListContract.View {

    private RecyclerView rvList;
    private FloatingActionButton floatBtn;

    int part, index;
    private static final int SPAN_SIZE = 1;
    private String setYear = DateUtils.getCurYear(FORMAT_Y);
    private String setMonth = DateUtils.getCurMonth(FORMAT_M);

    private StickyHeaderGridLayoutManager mLayoutManager;
    private MonthListAdapter adapter;
    private MonthListListener monthListListener;
    private List<MonthListBean.DaylistBean> list = null;

    /*****************************************************************************/

    public void changeDate(String year, String month) {
        setYear = year;
        setMonth = month;
        mPresenter.getMonthList(MyApplication.getCurrentUserId(), setYear, setMonth);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(SyncEvent event) {
        if (event.getState() == 100)
            mPresenter.getMonthList(MyApplication.getCurrentUserId(), setYear, setMonth);
    }

    /*****************************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_month_list;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //注册 EventBus
        EventBus.getDefault().register(this);

        mLayoutManager = new StickyHeaderGridLayoutManager(SPAN_SIZE);
        mLayoutManager.setHeaderBottomOverlapMargin(5);
        adapter = new MonthListAdapter(mContext, list);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        rvList = getViewById(R.id.rv_list);
        floatBtn = getViewById(R.id.float_btn);

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
        //fab点击事件
        floatBtn.setOnClickListener(v -> {
            if (BmobUser.getCurrentUser(MyUser.class) == null)
                SnackbarUtils.show(mContext, "请先登录");
            else {
                Intent intent = new Intent(getContext(), BillAddActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        //adapter的侧滑选项事件监听
        adapter.setOnStickyHeaderClickListener(new MonthListAdapter.OnStickyHeaderClickListener() {
            @Override
            public void OnDeleteClick(BBill item, int section, int offset) {
                item.setVersion(-1);
                //将删除的账单版本号设置为负，而非直接删除
                //便于同步删除服务器数据
                ProgressUtils.show(mContext, "正在删除...");
                mPresenter.updateBill(item);
                part = section;
                index = offset;
            }

            @Override
            public void OnEditClick(BBill item, int section, int offset) {
                Intent intent = new Intent(mContext, BillAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", item.getId());
                bundle.putString("rid", item.getRid());
                bundle.putString("sortName", item.getSortName());
                bundle.putString("payName", item.getPayName());
                bundle.putString("content", item.getContent());
                bundle.putDouble("cost", item.getCost());
                bundle.putLong("date", item.getCrdate());
                bundle.putBoolean("income", item.isIncome());
                bundle.putInt("version", item.getVersion());
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        //请求当月数据
        mPresenter.getMonthList(MyApplication.getCurrentUserId(), setYear, setMonth);
    }

    /*****************************************************************************/

    public void setMonthListListener(MonthListListener monthListListener) {
        this.monthListListener = monthListListener;
    }

    public interface MonthListListener {
        void OnDataChanged(String outcome, String income, String total);
    }

    /*****************************************************************************/

    @Override
    protected MonthListContract.Presenter bindPresenter() {
        return new MonthListPresenter();
    }

    @Override
    public void loadDataSuccess(MonthListBean monthListBean) {
        monthListListener.OnDataChanged(monthListBean.getT_outcome()
                , monthListBean.getT_income(), monthListBean.getT_total());
        list = monthListBean.getDaylist();
        adapter.setmDatas(list);
        adapter.notifyAllSectionsDataSetChanged();
    }

    @Override
    public void onSuccess() {
        ProgressUtils.dismiss();
//        adapter.remove(part, index);
        //从列表中移除后需要重新计算当月总计
        mPresenter.getMonthList(MyApplication.getCurrentUserId(), setYear, setMonth);
    }

    @Override
    public void onFailure(Throwable e) {
        ProgressUtils.dismiss();
        SnackbarUtils.show(mActivity, e.getMessage());
    }

    @Override
    protected void beforeDestroy() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * 监听Activity返回值
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    mPresenter.getMonthList(MyApplication.getCurrentUserId(), setYear, setMonth);
                    break;
            }
        }
    }
}
