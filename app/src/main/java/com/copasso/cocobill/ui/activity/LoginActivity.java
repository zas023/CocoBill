package com.copasso.cocobill.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import com.copasso.cocobill.R;
import com.copasso.cocobill.model.bean.remote.MyUser;
import com.copasso.cocobill.mvp.presenter.Imp.UserLogPresenterImp;
import com.copasso.cocobill.mvp.presenter.UserLogPresenter;
import com.copasso.cocobill.utils.*;
import com.copasso.cocobill.mvp.view.UserLogView;
import com.copasso.cocobill.widget.OwlView;

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
            SnackbarUtils.show(mContext, "用户名或密码不能为空");
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
            SnackbarUtils.show(mContext, "请填写必要信息");
            return;
        }
        if (!StringUtils.checkEmail(email)) {
            SnackbarUtils.show(mContext, "请输入正确的邮箱格式");
            return;
        }
        if (!password.equals(rpassword)) {
            SnackbarUtils.show(mContext, "两次密码不一致");
            return;
        }

        ProgressUtils.show(this, "正在注册...");

        userLogPresenter.signup(username,password,email);

    }

    @Override
    public void loadDataSuccess(MyUser tData) {
        ProgressUtils.dismiss();
        if (isLogin) {
            setResult(RESULT_OK, new Intent());
            finish();
        }else {
            SnackbarUtils.show(mContext, "注册成功");
        }

    }

    @Override
    public void loadDataError(Throwable throwable) {
        ProgressUtils.dismiss();
        SnackbarUtils.show(mContext, throwable.getMessage());
    }

}
