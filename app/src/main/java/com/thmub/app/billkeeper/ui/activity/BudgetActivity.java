package com.thmub.app.billkeeper.ui.activity;

import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.thmub.app.billkeeper.R;
import com.thmub.app.billkeeper.base.BaseActivity;
import com.thmub.app.billkeeper.constant.Constant;
import com.thmub.app.billkeeper.util.SharePreUtils;
import com.thmub.app.billkeeper.util.UIUtils;
import com.thmub.app.billkeeper.util.ValidationUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Enosh on 2020-04-05
 * Github: https://github.com/zas023
 * <p>
 * 预算
 */
public class BudgetActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_total_budget)
    EditText etTotalBudget;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_budget;
    }


    @Override
    protected void initWidget() {
        // 标题
        toolbar.setTitle(UIUtils.getString(mContext, R.string.activity_budget));

        // 预算
        etTotalBudget.setText(SharePreUtils.getString(mContext, Constant.SP_KEY_BUDGET, ""));
    }

    @Override
    protected void initClick() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.tv_add})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                String budget = etTotalBudget.getText().toString();
                if (ValidationUtils.isEmpty(budget)) {
                    budget = "";
                }
                SharePreUtils.putString(mContext, Constant.SP_KEY_BUDGET, budget);
                finish();
                break;
        }
    }
}
