package com.copasso.cocobill.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import com.copasso.cocobill.R;
import com.copasso.cocobill.bean.UserBean;
import com.copasso.cocobill.utils.HttpUtils;
import com.copasso.cocobill.utils.StringUtils;
import com.copasso.cocobill.view.OwlView;
import com.google.gson.Gson;

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
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.login_tv_sign, R.id.login_btn_login})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:  //button
                if (isLogin){
                    //登陆
                    String username = usernameET.getText().toString();
                    String password = passwordET.getText().toString();
                    if (username.length()==0||password.length()==0){
                        Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT);
                        break;
                    }
                    HttpUtils.userLogin(new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            Gson gson = new Gson();
                            UserBean userBean = gson.fromJson(msg.obj.toString(), UserBean.class);
                            if (userBean.getStatus() == 100) {
                                SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("username", userBean.getUsername());
                                editor.putString("email", userBean.getMail());
                                editor.putInt("id", userBean.getId());
                                editor.putString("json", msg.obj.toString());
                                editor.commit();
                                setResult(RESULT_OK, new Intent());
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, userBean.getMessage(), Toast.LENGTH_SHORT);
                            }
                        }
                    }, username, password);
                }else {
                    //注册
                    String email = emailET.getText().toString();
                    String username = emailET.getText().toString();
                    String password = passwordET.getText().toString();
                    String rpassword = rpasswordET.getText().toString();
                    if (email.length()==0||username.length()==0||password.length()==0||rpassword.length()==0){
                        Toast.makeText(LoginActivity.this, "请填写必要信息", Toast.LENGTH_SHORT);
                        break;
                    }
                    if (StringUtils.checkEmail(email)){
                        Toast.makeText(LoginActivity.this, "请输入正确的邮箱格式", Toast.LENGTH_SHORT);
                        break;
                    }
                    if (!password.equals(rpassword)){
                        Toast.makeText(LoginActivity.this, "两次密码不一致", Toast.LENGTH_SHORT);
                        break;
                    }
                    HttpUtils.userSign(new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            Gson gson = new Gson();
                            UserBean userBean = gson.fromJson(msg.obj.toString(), UserBean.class);
                            if (userBean.getStatus() == 100) {
                                Toast.makeText(LoginActivity.this, "注册成功，请先登陆邮箱验证后登陆", Toast.LENGTH_SHORT);
                            } else {
                                Toast.makeText(LoginActivity.this, userBean.getMessage(), Toast.LENGTH_SHORT);
                            }
                        }
                    }, username, password,email);
                }
                break;
            case R.id.login_tv_sign:  //sign
                if(isLogin){
                    //置换注册界面
                    signTV.setText("Login");
                    loginBtn.setText("Login");
                    isLogin=!isLogin;
                    rpasswordET.setVisibility(View.VISIBLE);
                    emailET.setVisibility(View.VISIBLE);
                }else {
                    //置换登陆界面
                    signTV.setText("Sign Up");
                    loginBtn.setText("Sign Up");
                    isLogin=!isLogin;
                    rpasswordET.setVisibility(View.GONE);
                    emailET.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }
}
