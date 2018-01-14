package com.copasso.cocobill.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import com.copasso.cocobill.R;
import com.copasso.cocobill.adapter.PayEditAdapter;
import com.copasso.cocobill.bean.UserBean;
import com.copasso.cocobill.utils.*;
import com.copasso.cocobill.view.CommonItemLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouas666 on 2017/12/30.
 */
public class SettingActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cil_change)
    CommonItemLayout changeCL;
    @BindView(R.id.cil_forget)
    CommonItemLayout forgetCL;
    @BindView(R.id.cil_store)
    CommonItemLayout storeCL;
    @BindView(R.id.cil_sort)
    CommonItemLayout sortCL;
    @BindView(R.id.cil_pay)
    CommonItemLayout payCL;

    private AlertDialog dialog;

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
    @OnClick({R.id.cil_forget,R.id.cil_change,R.id.cil_store, R.id.cil_sort,R.id.cil_pay})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.cil_change:  //修改密码
                showMailDialog();
                break;
            case R.id.cil_forget:  //忘记密码

                break;
            case R.id.cil_store:  //缓存

                break;
            case R.id.cil_sort:  //账单分类管理
                startActivity(new Intent(this,SortEditActivity.class));
                break;
            case R.id.cil_pay:  //支付方式管理
                startActivity(new Intent(this,PayEditActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 显示修改密码对话框
     */
    public void showMailDialog() {
        final LinearLayout layout=new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText editText = new EditText(mContext);
        editText.setHint("请输入密码");
        final EditText editText1 = new EditText(mContext);
        editText1.setHint("请重复密码");
        layout.addView(editText);
        layout.addView(editText1);

        if (dialog == null) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle("修改密码")
                    .setView(layout)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String input = editText.getText().toString();
                            String input1 = editText1.getText().toString();
                            if (input.equals("")||input1.equals("")) {
                                Toast.makeText(getApplicationContext(), "不能为空！" + input,
                                        Toast.LENGTH_SHORT).show();
                            }else if(input.equals(input1)){
                                //修改密码
                                changePw();
                            } else {
                                Toast.makeText(mContext,
                                        "两次输入不一致", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 更新用户密码
     */
    public void changePw() {
        if (currentUser == null)
            return;

        ProgressUtils.show(mContext, "正在修改...");
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(currentUser.getId()));
        params.put("username", currentUser.getUsername());
        params.put("password", currentUser.getPassword());
        params.put("gender", currentUser.getGender());
        params.put("phone", currentUser.getPhone());
        params.put("mail", currentUser.getMail());
        OkHttpUtils.getInstance().get(Constants.BASE_URL + Constants.USER_UPDATE, params,
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ProgressUtils.dismiss();
                        Toast.makeText(mContext, "修改失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ProgressUtils.dismiss();
                        SharedPUtils.setCurrentUser(mContext, currentUser);
                    }
                });
    }

}
