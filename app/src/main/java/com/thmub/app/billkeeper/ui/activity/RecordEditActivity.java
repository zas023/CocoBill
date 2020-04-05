package com.thmub.app.billkeeper.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.base.BaseActivity;
import com.thmub.app.billkeeper.bean.entity.Category;
import com.thmub.app.billkeeper.bean.entity.Record;
import com.thmub.app.billkeeper.bean.sql.LocalDatabase;
import com.thmub.app.billkeeper.constant.Constant;
import com.thmub.app.billkeeper.ui.adapter.CategoryPageAdapter;
import com.thmub.app.billkeeper.ui.dailog.TextEditDialog;
import com.thmub.app.billkeeper.ui.fragment.CategoryPageFragment;
import com.thmub.app.billkeeper.util.DatePickUtils;
import com.thmub.app.billkeeper.util.DateUtils;
import com.thmub.app.billkeeper.util.UIUtils;
import com.thmub.app.billkeeper.widget.KeyboardView;
import com.thmub.app.billkeeper.widget.tablayout.MTabLayout;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Enosh on 2020-03-07
 * Github: https://github.com/zas023
 * <p>
 * 账单编辑
 */
public class RecordEditActivity extends BaseActivity {

    public static final String EXTRA_RECORD_KEY = "extra_record_key";

    @BindView(R.id.keyboard)
    KeyboardView keyboardView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    MTabLayout tabLayout;
    @BindView(R.id.vp_category)
    ViewPager vpCategory;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.tv_date)
    TextView tvDate;

    private CategoryPageAdapter categoryPageAdapter;

    private long date;

    private Record record;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_edit;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_bottom_in, 0);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        record = (Record) getIntent().getSerializableExtra(EXTRA_RECORD_KEY);
        // add
        if (record == null) {
            record = new Record();
            date = System.currentTimeMillis();
        } else {
            date = record.getRecordDate();
        }
        categoryPageAdapter = new CategoryPageAdapter(getSupportFragmentManager());
    }

    @Override
    protected void initWidget() {
        // 账单分类
        categoryPageAdapter.addFragment(CategoryPageFragment.newInstance(Constant.TYPE_OUTCOME, record.getCategoryName()), "支出");
        categoryPageAdapter.addFragment(CategoryPageFragment.newInstance(Constant.TYPE_INCOME, record.getCategoryName()), "收入");

        vpCategory.setAdapter(categoryPageAdapter);
        // 收入
        if (Constant.TYPE_INCOME == record.getType()) {
            vpCategory.setCurrentItem(Constant.TYPE_INCOME);
        }
        tabLayout.setupWithViewPager(vpCategory);

        // 账单信息
        tvDate.setText(DateUtils.getDateText(date, DateUtils.FORMAT_MONTH_DAY));
        tvComment.setText(record.getComment());
        keyboardView.setText(record.getAmount());
    }

    @Override
    protected void initClick() {
        // 标题栏返回
        toolbar.setNavigationOnClickListener(v -> finish());

        // 键盘确认
        keyboardView.setOnClickListener((KeyboardView.OnClickListener) text -> {
            if (text.isEmpty()) {
                Toast.makeText(mContext, "请输入金额", Toast.LENGTH_SHORT).show();
                return;
            }
            // 账单金额
            record.setAmount(text);
            // 补全信息
            setRecord();
            // 保存或更新记录
            if (LocalDatabase.getInstance().recordDao().insertRecords(record) != null) {
                finish();
            } else {
                Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 账单信息补全
     **/
    private void setRecord() {
        // 账单分类
        Category category = categoryPageAdapter.getItem(vpCategory.getCurrentItem()).getSelectedCategory();
        record.setCategoryName(category.getName());
        record.setCategoryRes(category.getIconRes());
        // 账单时间
        record.setRecordDate(date);
        // 账单类型
        record.setType(vpCategory.getCurrentItem());
        // 账单状态
        record.setStatus(1);
    }

    @OnClick({R.id.tv_comment, R.id.tv_date})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_comment:
                showCommentDialog();
                break;
            case R.id.tv_date:
                DatePickUtils.showDatePickDialog(mContext, date, new DatePickUtils.OnDatePickListener() {
                    @Override
                    public void onConfirmClick(DialogInterface dialog, long selectedDate) {
                        date = selectedDate;
                        tvDate.setText(DateUtils.getDateText(date, DateUtils.FORMAT_MONTH_DAY_HOUR_MINUTE));
                    }
                });
                break;
        }
    }

    // 弹出备注输入对话框
    private void showCommentDialog() {
        String desc = tvComment.getText().toString();
        TextEditDialog dialog = new TextEditDialog(mContext)
                .setTitle("添加备注")
                .setHint("随便写点")
                .setContent(desc)
                .setListener(new TextEditDialog.Listener() {
                    @Override
                    public void onPositiveClick(EditText editText, DialogInterface dialog, String text) {
                        tvComment.setText(text);
                        record.setComment(text);
                        UIUtils.hideSoftKeyboard(mContext, editText);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeClick(EditText editText, DialogInterface dialog) {
                        UIUtils.hideSoftKeyboard(mContext, editText);
                        dialog.dismiss();
                    }
                });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_bottom_silent, R.anim.slide_bottom_out);
    }
}
