package com.copasso.cocobill.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.copasso.cocobill.R;
import com.copasso.cocobill.base.BaseActivity;
import com.copasso.cocobill.model.bean.remote.MyUser;
import com.copasso.cocobill.model.repository.LocalRepository;
import com.copasso.cocobill.utils.GlideCacheUtil;
import com.copasso.cocobill.utils.ImageUtils;
import com.copasso.cocobill.utils.ProgressUtils;
import com.copasso.cocobill.utils.SnackbarUtils;
import com.copasso.cocobill.utils.ToastUtils;
import com.copasso.cocobill.widget.CommonItemLayout;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Zhouas666 on 2019-01-10
 * Github: https://github.com/zas023
 * <p>
 * 设置activity
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private CommonItemLayout changeCL;
    private CommonItemLayout forgetCL;
    private CommonItemLayout storeCL;
    private CommonItemLayout sortCL;
    private CommonItemLayout payCL;
    private CommonItemLayout exportCL;

    private MyUser currentUser;

    /************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        currentUser = BmobUser.getCurrentUser(MyUser.class);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        toolbar = findViewById(R.id.toolbar);
        changeCL = findViewById(R.id.cil_change);
        forgetCL = findViewById(R.id.cil_forget);
        storeCL = findViewById(R.id.cil_store);
        sortCL = findViewById(R.id.cil_sort);
        payCL = findViewById(R.id.cil_pay);
        exportCL = findViewById(R.id.cil_export);

        //初始化Toolbar
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        storeCL.setRightText(GlideCacheUtil.getInstance().getCacheSize(mContext));
    }

    @Override
    protected void initClick() {
        super.initClick();
        changeCL.setOnClickListener(this);
        forgetCL.setOnClickListener(this);
        storeCL.setOnClickListener(this);
        sortCL.setOnClickListener(this);
        payCL.setOnClickListener(this);
        exportCL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cil_change:  //修改密码
                showChangeDialog();
                break;
            case R.id.cil_forget:  //忘记密码
                showForgetPwDialog();
                break;
            case R.id.cil_store:  //缓存
                showCacheDialog();
                break;
            case R.id.cil_sort:  //账单分类管理
                startActivity(new Intent(mContext,BillSortActivity.class));
                break;
            case R.id.cil_pay:  //支付方式管理
//                startActivity(new Intent(this,PayEditActivity.class));
                break;
            case R.id.cil_export:
//                String filename=Environment.getExternalStorageDirectory().getAbsolutePath()+"/cocoBill.xls";
//                if (ExcelUtils.export(LocalRepository.getInstance().getBBills(), filename))
//                    SnackbarUtils.show(this,"导出成功,文件目录："+filename);
//                else
//                    SnackbarUtils.show(this,"导出失败");
                break;
            default:
                break;
        }
    }

    /**
     * 显示忘记密码对话框
     */
    public void showForgetPwDialog() {
        new MaterialDialog.Builder(this)
                .title("备注")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("请输入注册邮箱", null, (dialog, input) -> {
                    if (input.equals("")) {
                        SnackbarUtils.show(mContext, "内容不能为空！");

                    } else {
                        //找回密码
                        BmobUser.resetPasswordByEmail(input.toString(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastUtils.show(mContext, "重置密码请求成功，请到邮箱进行密码重置操作");
                                } else {
                                    ToastUtils.show(mContext, "失败:" + e.getMessage());
                                }
                            }
                        });
                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .show();
    }

    /**
     * 显示修改密码对话框
     */
    public void showChangeDialog() {

        new MaterialDialog.Builder(mContext)
                .title("修改密码")
                .customView(R.layout.dialog_change_password, false)
                .positiveText("确定")
                .onPositive((dialog, which) -> {
                    View view = dialog.getCustomView();
                    TextInputLayout til = view.findViewById(R.id.change_til_password);
                    TextInputLayout til1 = view.findViewById(R.id.change_til_repassword);
                    String passport = til.getEditText().getText().toString();
                    String repaspsort = til.getEditText().getText().toString();
                    if (passport.equals("") || repaspsort.equals("")) {
                        ToastUtils.show(mContext, "不能为空！");
                    } else if (passport.equals(repaspsort)) {
                        //修改密码
                        changePw(passport);
                    } else {
                        ToastUtils.show(mContext, "两次输入不一致！");
                    }
                })
                .negativeText("取消")
                .show();
    }

    /**
     * 显示清除缓存对话框
     */
    public void showCacheDialog() {

        new MaterialDialog.Builder(mContext)
                .title("清除缓存")
                .positiveText("确定")
                .onPositive((dialog, which) -> {
                    GlideCacheUtil.getInstance().clearImageDiskCache(mContext);
                    storeCL.setRightText("0.00 byte");
                })
                .negativeText("取消")
                .show();
    }

    /**
     * 更新用户密码
     */
    public void changePw(String password) {
        if (currentUser == null)
            return;

        ProgressUtils.show(mContext, "正在修改...");
        currentUser.setPassword(password);
        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                ProgressUtils.dismiss();
                if (e != null)
                    ToastUtils.show(mContext, "修改失败" + e.getMessage());
            }
        });
    }
}
