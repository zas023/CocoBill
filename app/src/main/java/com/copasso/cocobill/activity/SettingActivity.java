package com.copasso.cocobill.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;
import com.copasso.cocobill.R;
import com.copasso.cocobill.utils.ThemeManager;
import com.copasso.cocobill.view.CommonItemLayout;

/**
 * Created by zhouas666 on 2017/12/30.
 */
public class SettingActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cil_store)
    CommonItemLayout storeCL;
    @BindView(R.id.cil_sort)
    CommonItemLayout sortCL;
    @BindView(R.id.cil_pay)
    CommonItemLayout payCL;

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initEventAndData() {

        //初始化Toolbar
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回消息更新上个Activity数据
                //setResult(RESULT_OK, new Intent());
                finish();
            }
        });
    }


    /**
     * 监听点击事件
     * @param view
     */
    @OnClick({R.id.cil_store, R.id.cil_sort,R.id.cil_pay})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.cil_store:  //缓存

                break;
            case R.id.cil_sort:  //账单分类管理
                startActivity(new Intent(this,SortEditActivity.class));
                break;
            case R.id.cil_pay:  //支付方式管理

                break;
            default:
                break;
        }
    }
}
