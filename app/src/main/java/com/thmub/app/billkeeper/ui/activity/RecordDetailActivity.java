package com.thmub.app.billkeeper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.base.BaseActivity;
import com.thmub.app.billkeeper.bean.entity.Record;
import com.thmub.app.billkeeper.bean.sql.LocalDatabase;
import com.thmub.app.billkeeper.constant.CategoryRes;
import com.thmub.app.billkeeper.constant.Constant;
import com.thmub.app.billkeeper.util.DateUtils;
import com.thmub.app.billkeeper.widget.MTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Enosh on 2020-03-22
 * Github: https://github.com/zas023
 * <p>
 * 账单详情
 */
public class RecordDetailActivity extends BaseActivity {

    public static final String EXTRA_RECORD_ID_KEY = "extra_record_id_key";


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_category_bg)
    FrameLayout ivCategoryBg;
    @BindView(R.id.iv_category)
    AppCompatImageView ivCategory;
    @BindView(R.id.tv_category_name)
    MTextView tvCategoryName;
    @BindView(R.id.tv_money)
    MTextView tvMoney;
    @BindView(R.id.tv_type)
    MTextView tvType;
    @BindView(R.id.tv_date)
    MTextView tvDate;
    @BindView(R.id.tv_comment)
    MTextView tvComment;

    private Record record;
    private long recordId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_detail;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        recordId = getIntent().getLongExtra(EXTRA_RECORD_ID_KEY, 0L);
        record = LocalDatabase.getInstance().recordDao().queryById(recordId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        if (record == null) {
            return;
        }
        // 账单分类
        ivCategoryBg.setBackgroundResource(record.getType() == Constant.TYPE_OUTCOME ?
                R.drawable.bg_category_circle_outcome : R.drawable.bg_category_circle_income);
        ivCategory.setImageResource(CategoryRes.resId(record.getCategoryRes()));
        tvCategoryName.setText(record.getCategoryName());
        // 账单金额
        tvMoney.setText("¥ " + record.getAmount());
        // 账单类型
        tvType.setText(record.getType() == Constant.TYPE_OUTCOME ? "支出" : "收入");
        // 账单日期
        tvDate.setText(DateUtils.getDateText(record.getRecordDate(), DateUtils.FORMAT_ALL));
        // 账单备注
        if (!record.getComment().isEmpty()) {
            tvComment.setText(record.getComment());
        }

    }

    @Override
    protected void initClick() {
        super.initClick();
        // 标题栏返回
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.tv_modify, R.id.action_delete})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_modify:  // 修改
                Intent intent = new Intent(mContext, RecordEditActivity.class);
                intent.putExtra(RecordEditActivity.EXTRA_RECORD_KEY, record);
                startActivity(intent);
                break;
            case R.id.action_delete:  //删除
                MaterialDialog dialog = new MaterialDialog(mContext, MaterialDialog.getDEFAULT_BEHAVIOR());
                dialog.setTitle(record.getCategoryName());
                dialog.message(R.string.message_delete, null, null);
                dialog.positiveButton(R.string.confirm, null, materialDialog -> {
                    LocalDatabase.getInstance().recordDao().deleteRecords(record);
                    finish();
                    return null;
                });
                dialog.negativeButton(R.string.cancel, null, null);
                dialog.show();
                break;
        }
    }


    /*******************Lifecycle*******************/
    @Override
    protected void onResume() {
        super.onResume();
        record = LocalDatabase.getInstance().recordDao().queryById(recordId);
        initWidget();
    }
}
