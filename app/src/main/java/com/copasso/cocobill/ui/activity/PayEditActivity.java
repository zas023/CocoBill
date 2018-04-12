package com.copasso.cocobill.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.copasso.cocobill.R;
import com.copasso.cocobill.model.bean.local.BSort;
import com.copasso.cocobill.ui.adapter.PayEditAdapter;
import com.copasso.cocobill.model.bean.local.BPay;
import com.copasso.cocobill.model.bean.local.NoteBean;
import com.copasso.cocobill.mvp.presenter.Imp.NotePresenterImp;
import com.copasso.cocobill.mvp.presenter.NotePresenter;
import com.copasso.cocobill.utils.*;
import com.copasso.cocobill.mvp.view.NoteView;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhouas666 on 2018/1/14.
 */
public class PayEditActivity extends BaseActivity implements NoteView{

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.type_layout)
    RelativeLayout typeLayout;

    private AlertDialog alertDialog;

    private NotePresenter presenter;

    private PayEditAdapter payEditAdapter;

    private NoteBean noteBean;
    private List<BPay> mDatas;

    @Override
    protected int getLayout() {
        return R.layout.activity_base_list;
    }

    @Override
    protected void initEventAndData() {
        //隐藏中间支付类型选择按钮
        typeLayout.setVisibility(View.GONE);
        //初始化
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        presenter=new NotePresenterImp(this);

        //本地获取失败后
        if (noteBean == null) {
            //同步获取分类、支付方式信息
            presenter.getNote();
        } else {
            //成功后加载布局
            setTitleStatus();
        }
    }

    @Override
    public void loadDataSuccess(BSort tData) {

    }

    @Override
    public void loadDataSuccess(BPay tData) {
        SharedPUtils.setUserNoteBean(mContext, (NoteBean) null);
        ProgressUtils.dismiss();
        initEventAndData();
    }

    @Override
    public void loadDataSuccess(NoteBean tData) {
        noteBean=tData;
        //成功后加载布局
        setTitleStatus();
        //保存数据
        SharedPUtils.setUserNoteBean(mContext, tData);
    }

    @Override
    public void loadDataError(Throwable throwable) {
        ProgressUtils.dismiss();
        SnackbarUtils.show(mContext,throwable.getMessage());
    }

    /**
     * 设置状态
     */
    private void setTitleStatus() {
        mDatas=noteBean.getPayinfo();
        initView();
    }

    /**
     * 加载数据
     */
    private void initView() {
        payEditAdapter = new PayEditAdapter(this, mDatas);
        mRecyclerView.setAdapter(payEditAdapter);

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
                payEditAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                saveEdit();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //侧滑事件
//                if (mDatas.get(viewHolder.getAdapterPosition()).getUid() > 0) {
//                    showDeteteDialog(viewHolder.getAdapterPosition());
//                } else {
//                    Toast.makeText(mContext, "系统分类，不可删除", Toast.LENGTH_SHORT).show();
//                }
                payEditAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean isLongPressDragEnabled() {
                //是否可拖拽
                return true;
            }
        });

        helper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 保存修改
     */
    public void saveEdit() {
        noteBean.setPayinfo(mDatas);
        SharedPUtils.setUserNoteBean(mContext, noteBean);
    }

    /**
     * 监听点击事件
     *
     * @param view
     */
    @OnClick({R.id.tb_note_income, R.id.tb_note_outcome, R.id.back_btn, R.id.add_btn})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:  //返回
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.add_btn: //添加
                showContentDialog();
                break;
        }
    }

    /**
     * 添加支付方式对话框
     */
    public void showContentDialog() {

        ProgressUtils.show(mContext,"正在添加");

        final LinearLayout layout=new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText editText = new EditText(mContext);
        editText.setHint("名称");
//        final EditText editText1 = new EditText(mContext);
//        editText1.setHint("备注");
        layout.addView(editText);
//        layout.addView(editText1);
        //弹出输入框
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("支付方式")
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = editText.getText().toString();
                        if (input.equals("")) {
                            SnackbarUtils.show(mContext, "内容不能为空！");
                        } else {
//                            ProgressUtils.show(mContext);
//                            presenter.addPay(currentUser.getId(),input,"card_bank.png",editText1.getText().toString());
                            BPay pay=new BPay(null,input,"card_bank.png",0,0);
                            mDatas.add(pay);
                            presenter.addPay(pay);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


    /**
     * 显示删除确认框
     */
    public void showDeteteDialog(final int index) {
        //弹出输入框
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("确定删除此分类")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mDatas.remove(index);
                        payEditAdapter.notifyItemRemoved(index);
                        saveEdit();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
            setResult(RESULT_OK, new Intent());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

