package com.copasso.cocobill.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import com.copasso.cocobill.R;
import com.copasso.cocobill.bean.UserBean;
import com.copasso.cocobill.utils.*;
import com.copasso.cocobill.view.OwlView;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouas666 on 2017/12/8.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.owl_view)
    OwlView mOwlView;
    @BindView(R.id.login_et_email)
    EditText emailET;
    @BindView(R.id.login_et_username)
    EditText usernameET;
    @BindView(R.id.login_et_password)
    EditText passwordET;
    @BindView(R.id.login_et_rpassword)
    EditText rpasswordET;
    @BindView(R.id.login_tv_sign)
    TextView signTV;
    @BindView(R.id.login_btn_login)
    Button loginBtn;

    //是否是登陆操作
    private boolean isLogin = true;

    @Override
    protected int getLayout() {
        return R.layout.activity_user_login;
    }

    @Override
    protected void initEventAndData() {

        //监听密码输入框的聚焦事件
        passwordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mOwlView.open();
                } else {
                    mOwlView.close();
                }
            }
        });
        rpasswordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mOwlView.open();
                } else {
                    mOwlView.close();
                }
            }
        });

    }

    /**
     * 监听点击事件
     *
     * @param view
     */
    @OnClick({R.id.login_tv_sign, R.id.login_btn_login,R.id.login_tv_forget})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:  //button
                if (isLogin) {
                    //登陆
                    login(view);
                } else {
                    //注册
                    sign(view);
                }
                break;
            case R.id.login_tv_sign:  //sign
                if (isLogin) {
                    //置换注册界面
                    signTV.setText("Login");
                    loginBtn.setText("Sign Up");
                    rpasswordET.setVisibility(View.VISIBLE);
                    emailET.setVisibility(View.VISIBLE);
                } else {
                    //置换登陆界面
                    signTV.setText("Sign Up");
                    loginBtn.setText("Login");
                    rpasswordET.setVisibility(View.GONE);
                    emailET.setVisibility(View.GONE);
                }
                isLogin = !isLogin;
                break;

            case R.id.login_tv_forget:  //忘记密码
                startActivity(new Intent(mContext, ForgetPasswordActivity.class));
                break;

            default:
                break;
        }
    }

    /**
     * 执行登陆动作
     */
    public void login(final View view) {
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        if (username.length() == 0 || password.length() == 0) {
            Snackbar.make(view, "用户名或密码不能为空", Snackbar.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        ProgressUtils.show(this, "正在登陆...");

        OkHttpUtils.getInstance().get(Constants.BASE_URL + Constants.USER_LOGIN, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ProgressUtils.dismiss();
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //注：
                //response.body().string()只能调用一次
                //否则报错
                String result = response.body().string();
                ProgressUtils.dismiss();
                Gson gson = new Gson();
                UserBean userBean = gson.fromJson(result, UserBean.class);
                if (userBean.getStatus() == 100) {
                    if (userBean.getState() == 1) {
                        SharedPUtils.setCurrentUser(LoginActivity.this, result);
                        setResult(RESULT_OK, new Intent());
                        finish();
                    } else {
                        Snackbar.make(view, "请先登陆邮箱激活账号", Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    Snackbar.make(view, userBean.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 执行注册动作
     */
    public void sign(final View view) {
        String email = emailET.getText().toString();
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String rpassword = rpasswordET.getText().toString();
        if (email.length() == 0 || username.length() == 0 || password.length() == 0 || rpassword.length() == 0) {
            Snackbar.make(view, "请填写必要信息", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (!StringUtils.checkEmail(email)) {
            Snackbar.make(view, "请输入正确的邮箱格式", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(rpassword)) {
            Snackbar.make(view, "两次密码不一致", Snackbar.LENGTH_LONG).show();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("mail", email);

        ProgressUtils.show(this, "正在注册...");

        OkHttpUtils.getInstance().get(Constants.BASE_URL + Constants.USER_SIGN, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ProgressUtils.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                ProgressUtils.dismiss();
                Gson gson = new Gson();
                UserBean userBean = gson.fromJson(result, UserBean.class);
                if (userBean.getStatus() == 100) {
                    Snackbar.make(view, "注册成功，请先登陆邮箱验证后登陆", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, userBean.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
