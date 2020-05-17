package com.copasso.cocobill.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.copasso.cocobill.R;
import com.copasso.cocobill.base.BaseMVPActivity;
import com.copasso.cocobill.model.bean.local.BSort;
import com.copasso.cocobill.model.bean.local.NoteBean;
import com.copasso.cocobill.presenter.BillNotePresenter;
import com.copasso.cocobill.presenter.contract.BillNoteContract;
import com.copasso.cocobill.ui.adapter.BillSortAdapter;
import com.copasso.cocobill.utils.SnackbarUtils;
import com.copasso.cocobill.utils.ToastUtils;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Zhouas666 on 2019-01-11
 * Github: https://github.com/zas023
 * <p>
 * 账单分类编辑activity
 */
public class BillSortActivity extends BaseMVPActivity<BillNoteContract.Presenter>
        implements BillNoteContract.View, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private TextView incomeTv;    //收入按钮
    private TextView outcomeTv;   //支出按钮

    public boolean isOutcome = true;

    private BillSortAdapter billSortAdapter;

    private NoteBean noteBean;
    private List<BSort> mDatas;

    /**************************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_list;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        Intent intent = getIntent();
        isOutcome = intent.getBooleanExtra("type", true);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mRecyclerView = findViewById(R.id.recyclerView);
        incomeTv = findViewById(R.id.tb_note_income);
        outcomeTv = findViewById(R.id.tb_note_outcome);

        billSortAdapter = new BillSortAdapter(mContext, mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(billSortAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();

        incomeTv.setOnClickListener(this);
        outcomeTv.setOnClickListener(this);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //首先回调的方法 返回int表示是否监听该方向
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//拖拽
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;//侧滑删除
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //滑动事件
                Collections.swap(mDatas, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                billSortAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                //更新账单分类排序
                saveBSorts();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int index = viewHolder.getAdapterPosition()-1;
                //侧滑事件
                new MaterialDialog.Builder(mContext)
                        .title("确定删除此分类")
                        .content("删除后该分类下的账单会继续保留")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(((dialog, which) -> {
                            mDatas.remove(index);
                            billSortAdapter.notifyItemRemoved(index);
                            //删除账单分类
                            mPresenter.deleteBSortByID(mDatas.get(index).getId());
                            //更新排序
                            saveBSorts();
                        }))
                        .show();
            }

            @Override
            public boolean isLongPressDragEnabled() {
                //是否可拖拽
                return true;
            }
        });

        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.getBillNote();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:  //返回
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.add_btn: //添加
                showContentDialog();
                break;
            case R.id.tb_note_income://收入
                isOutcome = false;
                setTitleStatus();
                break;
            case R.id.tb_note_outcome://支出
                isOutcome = true;
                setTitleStatus();
                break;
        }
    }

    /**
     * 监听返回按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
            setResult(RESULT_OK, new Intent());
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置状态栏
     */
    private void setTitleStatus() {

        if (isOutcome) {
            //设置支付状态
            outcomeTv.setSelected(true);
            incomeTv.setSelected(false);
            mDatas = noteBean.getOutSortlis();
        } else {
            //设置收入状态
            incomeTv.setSelected(true);
            outcomeTv.setSelected(false);
            mDatas = noteBean.getInSortlis();
        }

        billSortAdapter.setItems(mDatas);
        billSortAdapter.notifyDataSetChanged();
    }

    /**
     * 显示备注内容输入框
     */
    public void showContentDialog() {

        new MaterialDialog.Builder(this)
                .title("添加分类")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(0, 200, R.color.textRed)
                .input("分类名称", null, (dialog, input) -> {
                    if (input.equals("")) {
                        ToastUtils.show(mContext,"内容不能为空！");
                    } else {
                        BSort sort = new BSort(null, input.toString(), "sort_tianjiade.png",mDatas.size(),0, !isOutcome);
                        mPresenter.addBSort(sort);
                        mDatas.add(sort);
                    }
                })
                .positiveText("确定")
                .show();
    }

    /**
     * 保存修改
     */
    private void saveBSorts() {
        //更新账单分类排序
        for (int i = 0; i < mDatas.size(); i++)
            mDatas.get(i).setPriority(i);
        mPresenter.updateBBsorts(mDatas);
    }


    /**************************************************************************/
    @Override
    protected BillNoteContract.Presenter bindPresenter() {
        return new BillNotePresenter();
    }

    @Override
    public void loadDataSuccess(NoteBean bean) {
        noteBean = bean;
        setTitleStatus();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(Throwable e) {
        SnackbarUtils.show(mContext, e.getMessage());
    }
}
