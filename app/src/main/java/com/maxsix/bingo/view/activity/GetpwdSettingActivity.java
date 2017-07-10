package com.maxsix.bingo.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.maxsix.bingo.R;
import com.maxsix.bingo.config.Constants;
import com.maxsix.bingo.task.Callback;
import com.maxsix.bingo.util.Utils;

import java.util.concurrent.Callable;

public class GetpwdSettingActivity extends BaseActivity implements View.OnClickListener {

    @com.maxsix.bingo.util.InjectView(R.id.btnModifyWXAccount)
    private Button btnModifyWXAccount;
    @com.maxsix.bingo.util.InjectView(R.id.btn_send)
    private Button btn_send;
    private com.maxsix.bingo.vo.User user;
    @com.maxsix.bingo.util.InjectView(R.id.et_usertel)
    private EditText et_usertel;
    @com.maxsix.bingo.util.InjectView(R.id.et_code)
    private EditText et_code;
    private MyCount mc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getpassword);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        et_usertel.addTextChangedListener(new TelTextChange());
        btn_send.setOnClickListener(this);
        btnModifyWXAccount.setOnClickListener(this);
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
    /* 定义一个倒计时的内部类 */
    private class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_send.setBackgroundColor(Color.parseColor("#00736c"));
            btn_send.setEnabled(true);
            btn_send.setText("发送验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_send.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.btn_unenable_green));
            btn_send.setEnabled(false);
            btn_send.setText("(" + millisUntilFinished / 1000 + ")秒");
        }
    }
    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {
        String tel = et_usertel.getText().toString().trim();
        String code = et_code.getText().toString().trim();
        switch (v.getId()){
            case R.id.btn_send:
                sendCode(tel);
                break;
            case R.id.btnModifyWXAccount:
                if(code != null && code.length() == 6) {
                    getpwdSerive(tel, code);
                }else{
                    Utils.showLongToast(context, "请输入验证码！");
                }
                break;
        }
    }
    private void sendCode(String phone){
        if (phone.length() == 11) {
            if (!Utils.isMobileNO(phone)) {
                Utils.showLongToast(context, "请输入正确的手机号码！");
            }else {
                if (mc == null) {
                    mc = new MyCount(60000, 1000); // 第一参数是总的时间，第二个是间隔时间
                }
                mc.start();
                sendCodeSerive(phone);
            }
        }else{
            Utils.showLongToast(context, "请输入正确的手机号码！");
        }

    }
    private void sendCodeSerive(final String tel) {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();

            @Override
            public Boolean call() throws Exception {
                String info = "{\"tel\":\""+tel+"\"}";
                String resultJson = http.Post(Constants.SendCode_URL,info);
                com.maxsix.bingo.vo.VDataResult result = gson.fromJson(resultJson, com.maxsix.bingo.vo.VDataResult.class);
                if (result != null && result.getStatus() == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }, new Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {
                if (pCallbackValue == true) {
                    com.maxsix.bingo.util.Utils.showLongToast(GetpwdSettingActivity.this, "验证码发送成功，请查收");
                    btnModifyWXAccount.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.btn_bg_green));
                    btnModifyWXAccount.setEnabled(true);
                    btnModifyWXAccount.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    com.maxsix.bingo.util.Utils.showLongToast(GetpwdSettingActivity.this, "验证码发送失败");
                }

            }
        }, new Callback<Exception>() {
            @Override
            public void onCallback(Exception pCallbackValue) {
                Utils.showLongToast(GetpwdSettingActivity.this, "验证码发送失败!");
            }
        }, true, "");
    }
    private void getpwdSerive(final String tel,final String vcode) {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();
            @Override
            public Boolean call() throws Exception {
                String info = "{\"tel\":\"" + tel + "\",\"vcode\":\"" + vcode + "\"}";
                String resultJson = http.Post(Constants.Getpassword_URL, info);
                com.maxsix.bingo.vo.VDataResult result = gson.fromJson(resultJson, com.maxsix.bingo.vo.VDataResult.class);
                if (result != null && result.getStatus() == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }, new Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {
                if (pCallbackValue == true) {
                    Utils.showLongToast(GetpwdSettingActivity.this, "已经发送临时密码到您的手机，正为您返回登录界面");
                    Intent intent = new Intent(GetpwdSettingActivity.this,
                            TelLoginActivity.class);
                    startActivity(intent);
                } else {
                    Utils.showLongToast(GetpwdSettingActivity.this, "发送失败,请检查验证码或是该手机号码是否一致");
                }

            }
        }, new Callback<Exception>() {
            @Override
            public void onCallback(Exception pCallbackValue) {
                Utils.showLongToast(GetpwdSettingActivity.this, "发送失败!");
            }
        }, true, "");
    }
    // 手机号 EditText监听器
    class TelTextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before,
                                  int count) {
            String phone = et_usertel.getText().toString();

            if (phone.length() == 11) {
                if (!Utils.isMobileNO(phone)) {
                    Utils.showLongToast(context, "请输入正确的手机号码！");
                    btn_send.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.btn_unenable_green));
                    btn_send.setEnabled(false);
                }else{
                    btn_send.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.btn_bg_green));
                    btn_send.setEnabled(true);
                }
            }
        }
    }
    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }
    @Override
    protected void findViewById() {

    }
}
