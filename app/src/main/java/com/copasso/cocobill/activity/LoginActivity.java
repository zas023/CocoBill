package com.copasso.cocobill.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import com.copasso.cocobill.R;
import com.copasso.cocobill.bean.UserBean;
import com.copasso.cocobill.common.Constants;
import com.copasso.cocobill.presenter.Imp.UserLogPresenterImp;
import com.copasso.cocobill.presenter.UserLogPresenter;
import com.copasso.cocobill.utils.*;
import com.copasso.cocobill.view.UserLogView;
import com.copasso.cocobill.widget.OwlView;
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
public class LoginActivity extends BaseActivity implements UserLogView {

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

    private UserLogPresenter userLogPresenter;

    @Override
    protected int getLayout() {
        return R.layout.activity_user_login;
    }

    @Override
    protected void initEventAndData() {

        userLogPresenter = new UserLogPresenterImp(this);

    }

    //监听密码输入框的聚焦事件
    @OnFocusChange({R.id.login_et_password,R.id.login_et_rpassword})
    public void onFocusChange(View view, boolean b) {
        if (b) {
            mOwlView.open();
        } else {
            mOwlView.close();
        }
    }

    /**
     * 监听点击事件
     *
     * @param view
     */
    @OnClick({R.id.login_tv_sign, R.id.login_btn_login, R.id.login_tv_forget})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:  //button
                if (isLogin) {
                    login();  //登陆
                } else {
                    sign();  //注册
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
    public void login() {
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        if (username.length() == 0 || password.length() == 0) {
            Snackbar.make(getWindow().getDecorView(), "用户名或密码不能为空", Snackbar.LENGTH_SHORT).show();
            return;
        }

        ProgressUtils.show(this, "正在登陆...");

        userLogPresenter.login(username, password);
    }

    /**
     * 执行注册动作
     */
    public void sign() {
        String email = emailET.getText().toString();
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String rpassword = rpasswordET.getText().toString();
        if (email.length() == 0 || username.length() == 0 || password.length() == 0 || rpassword.length() == 0) {
            SnackbarUtils.show(getWindow().getDecorView(), "请填写必要信息");
            return;
        }
        if (!StringUtils.checkEmail(email)) {
            SnackbarUtils.show(getWindow().getDecorView(), "请输入正确的邮箱格式");
            return;
        }
        if (!password.equals(rpassword)) {
            SnackbarUtils.show(getWindow().getDecorView(), "两次密码不一致");
            return;
        }

        ProgressUtils.show(this, "正在注册...");

        userLogPresenter.signup(username,password,email);

    }

    @Override
    public void loadDataSuccess(UserBean tData) {
        ProgressUtils.dismiss();
        if (isLogin) {
            if (tData.getState() == 1) {
                SharedPUtils.setCurrentUser(mContext, tData);
                setResult(RESULT_OK, new Intent());
                finish();
            } else {
                SnackbarUtils.show(getWindow().getDecorView(), "请先登陆邮箱激活账号");
            }
        }else {
            SnackbarUtils.show(getWindow().getDecorView(), "注册成功，请先登陆邮箱验证后登陆");
        }

    }

    @Override
    public void loadDataError(Throwable throwable) {
        ProgressUtils.dismiss();
        SnackbarUtils.show(getWindow().getDecorView(), throwable.getMessage());
    }

}
