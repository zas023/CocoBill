package com.copasso.cocobill.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.copasso.cocobill.R;
import com.copasso.cocobill.base.BaseMVPActivity;
import com.copasso.cocobill.model.bean.remote.MyUser;
import com.copasso.cocobill.presenter.LandPresenter;
import com.copasso.cocobill.presenter.contract.LandContract;
import com.copasso.cocobill.utils.ProgressUtils;
import com.copasso.cocobill.utils.SnackbarUtils;
import com.copasso.cocobill.utils.StringUtils;
import com.copasso.cocobill.widget.OwlView;
/**
 * Created by Zhouas666 on 2019-01-10
 * Github: https://github.com/zas023
 * <p>
 * 用户登录、注册activity
 */
public class LandActivity extends BaseMVPActivity<LandContract.Presenter>
        implements LandContract.View, View.OnFocusChangeListener, View.OnClickListener {

    private OwlView mOwlView;
    private EditText emailET;
    private EditText usernameET;
    private EditText passwordET;
    private EditText rpasswordET;
    private TextView signTV;
    private TextView forgetTV;
    private Button loginBtn;

    //是否是登陆操作
    private boolean isLogin = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_land;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mOwlView=findViewById(R.id.land_owl_view);
        emailET=findViewById(R.id.login_et_email);
        usernameET=findViewById(R.id.login_et_username);
        passwordET=findViewById(R.id.login_et_password);
        rpasswordET=findViewById(R.id.login_et_rpassword);
        signTV=findViewById(R.id.login_tv_sign);
        forgetTV=findViewById(R.id.login_tv_forget);
        loginBtn=findViewById(R.id.login_btn_login);
    }

    @Override
    protected void initClick() {
        super.initClick();
        passwordET.setOnFocusChangeListener(this);
        rpasswordET.setOnFocusChangeListener(this);
        signTV.setOnClickListener(this);
        forgetTV.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mOwlView.open();
        } else {
            mOwlView.close();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

        mPresenter.login(username, password);
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

        mPresenter.signup(username,password,email);

    }

    /***********************************************************************/

    @Override
    protected LandContract.Presenter bindPresenter() {
        return new LandPresenter();
    }

    @Override
    public void landSuccess(MyUser user) {
        ProgressUtils.dismiss();
        if (isLogin) {
            setResult(RESULT_OK, new Intent());
            finish();
        }else {
            SnackbarUtils.show(mContext, "注册成功");
        }
        Log.i(TAG,user.toString());
    }

    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure(Throwable e) {
        ProgressUtils.dismiss();
        SnackbarUtils.show(mContext, e.getMessage());
        Log.e(TAG,e.getMessage());
    }
}
