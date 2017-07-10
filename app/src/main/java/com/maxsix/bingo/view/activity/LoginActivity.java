package com.maxsix.bingo.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maxsix.bingo.R;
import com.maxsix.bingo.config.AppManager;
import com.maxsix.bingo.config.Constants;
import com.maxsix.bingo.dialog.FlippingLoadingDialog;
import com.maxsix.bingo.task.Callback;
import com.maxsix.bingo.util.InjectUtil;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.vo.DataResult;
import com.maxsix.bingo.vo.LoginInfo;
import com.maxsix.bingo.vo.User;
import com.google.gson.Gson;
import java.util.concurrent.Callable;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.username)
    private EditText username;

    @InjectView(R.id.password)
    private EditText password;

    @InjectView(R.id.login)
    private Button btn_login;

    @InjectView(R.id.register)
    private Button btn_register;
    private FlippingLoadingDialog loginLoadingDialog;
    private String letteyType;
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppManager.getInstance().addActivity(this);
        letteyType = getIntent().getStringExtra("letteyType");
        InjectUtil.autoInjectView(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        ed = sp.edit();
    }

    @Override
    protected void initControl() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                getLogin();
                break;
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, TelRegisterActivity.class));
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
            default:
                break;
        }
    }
    private void getLogin() {
        String userName = username.getText().toString().trim();
        String psw = password.getText().toString().trim();
        loginLoadingDialog = getLoadingDialog("正在登录……");
        loginLoadingDialog.show();
        getLogin(userName, psw);
    }

    private void getLogin(final String userName, final String password) {
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            getLoginserive(userName, password);
        } else {
            loginLoadingDialog.dismiss();
            Utils.showLongToast(LoginActivity.this, "请填写账号或密码！");
        }
    }
    private void getUserInfo() {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();
            @Override
            public Boolean call() throws Exception {

                String usrInfo = http.Get(Constants.User_BaseURL + "0/?"+System.currentTimeMillis());
                User user = gson.fromJson(usrInfo, User.class);
                if(user !=null){
                    AppManager.getInstance().setUserSession(user);
                    return true;
                }else{
                    return false;
                }

            }

        }, new Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {
                if (pCallbackValue == true) {
                    ed.putString("username",  username.getText().toString().trim());
                    ed.putString("password",  password.getText().toString().trim());
                    ed.commit();
                    Intent intent = new Intent(LoginActivity.this,
                            LockLoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    loginLoadingDialog.dismiss();
                    Utils.showLongToast(LoginActivity.this, "获取用户信息失败！");
                }

            }
        }, new Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                loginLoadingDialog.dismiss();
                Utils.showLongToast(LoginActivity.this, "获取用户信息失败!");
            }
        }, false,"");

    }
    private void getLoginserive(final String userName, final String psw) {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();
            @Override
            public Boolean call() throws Exception {
                LoginInfo obj = new LoginInfo();
                obj.setUsername(userName);
                obj.setPassword(psw);
                String resultJson = http.Post(Constants.Login_URL, gson.toJson(obj));
                DataResult result = gson.fromJson(resultJson, DataResult.class);
                if (result !=null&&result.getToken() !=null && !result.getToken().equals("")) {
                    http.setAuthorization("token " + result.getToken());
                    return true;
                } else {
                    return false;
                }
            }

        }, new Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {
                if (pCallbackValue == true) {
                    getUserInfo();
                } else {
                    loginLoadingDialog.dismiss();
                    Utils.showLongToast(LoginActivity.this,"登录失败！");
                }

            }
        }, new Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                loginLoadingDialog.dismiss();
                Utils.showLongToast(LoginActivity.this,"登录失败!");
            }
        }, false,"");

    }
    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        Intent intent = new Intent(LoginActivity.this,
                HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in,
                R.anim.push_up_out);
        finish();
    }
    @Override
    protected void findViewById() {

    }
}
