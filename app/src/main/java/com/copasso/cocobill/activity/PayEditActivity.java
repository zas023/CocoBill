package com.copasso.cocobill.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import com.copasso.cocobill.R;
import com.copasso.cocobill.adapter.PayEditAdapter;
import com.copasso.cocobill.adapter.SortEditAdapter;
import com.copasso.cocobill.bean.BPay;
import com.copasso.cocobill.bean.BSort;
import com.copasso.cocobill.bean.NoteBean;
import com.copasso.cocobill.utils.Constants;
import com.copasso.cocobill.utils.HttpUtils;
import com.copasso.cocobill.utils.OkHttpUtils;
import com.copasso.cocobill.utils.SharedPUtils;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouas666 on 2018/1/14.
 */
public class PayEditActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.type_layout)
    RelativeLayout typeLayout;

    private AlertDialog alertDialog;

    public boolean isOutcome = true;

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

        noteBean = SharedPUtils.getUserNoteBean(this);
        //本地获取失败后
        if (noteBean == null) {
            //同步获取分类、支付方式信息
            HttpUtils.getNote(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Gson gson = new Gson();
                    noteBean = gson.fromJson(msg.obj.toString(), NoteBean.class);
                    //status==100:处理成功！
                    if (noteBean.getStatus() == 100) {
                        //成功后加载布局
                        setTitleStatus();
                        //保存数据
                        SharedPUtils.setUserNoteBean(mContext, msg.obj.toString());
                    }
                }
            }, Constants.currentUserId);
        } else {
            //成功后加载布局
            setTitleStatus();
        }
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
                if (mDatas.get(viewHolder.getAdapterPosition()).getUid() > 0) {
                    showDeteteDialog(viewHolder.getAdapterPosition());
                } else {
                    Toast.makeText(mContext, "系统分类，不可删除", Toast.LENGTH_SHORT).show();
                }
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
     * 显示备注内容输入框
     */
    public void showContentDialog() {
        final EditText editText = new EditText(mContext);

        //弹出输入框
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("输入名称")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = editText.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getApplicationContext(), "内容不能为空！" + input,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, String> params = new HashMap<>();
                            params.put("uid", String.valueOf(Constants.currentUserId));
                            params.put("sortImg", "sort_tianjiade.png");
                            params.put("income", isOutcome ? "false" : "true");
                            try {
                                params.put("sortName", java.net.URLEncoder.encode(input, "utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            OkHttpUtils.getInstance().get(Constants.BASE_URL + Constants.NOTE_SORT_ADD, params,
                                    new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            SharedPUtils.setUserNoteBean(mContext, (NoteBean) null);
                                        }
                                    });
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

