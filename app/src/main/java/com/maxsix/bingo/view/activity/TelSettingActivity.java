package com.maxsix.bingo.view.activity;

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
import com.maxsix.bingo.vo.DataResult;

import java.util.concurrent.Callable;

public class TelSettingActivity extends BaseActivity implements View.OnClickListener {

    @com.maxsix.bingo.util.InjectView(R.id.btnModifyWXAccount)
    private Button btnModifyWXAccount;
    @com.maxsix.bingo.util.InjectView(R.id.btn_send)
    private Button btn_send;
    private com.maxsix.bingo.vo.User user;
    @com.maxsix.bingo.util.InjectView(R.id.et_usertel)
    private EditText et_usertel;
    @com.maxsix.bingo.util.InjectView(R.id.et_code)
    private EditText et_code;
    private com.maxsix.bingo.dialog.FlippingLoadingDialog loginLoadingDialog;
    private MyCount mc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telsetting);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        user = com.maxsix.bingo.config.AppManager.getInstance().getUserSession();
        if(user != null){
            et_usertel.setText(user.getTel());
        }
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
        switch (v.getId()){
            case R.id.btn_send:
                sendCode();
                break;
            case R.id.btnModifyWXAccount:
                modifyAliAccount();
                break;
        }
    }
    private void sendCode(){
        String phone = et_usertel.getText().toString();
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
                    com.maxsix.bingo.util.Utils.showLongToast(TelSettingActivity.this, "验证码发送成功，请查收");
                    btnModifyWXAccount.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.btn_bg_green));
                    btnModifyWXAccount.setEnabled(true);
                    btnModifyWXAccount.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    com.maxsix.bingo.util.Utils.showLongToast(TelSettingActivity.this, "验证码发送失败");
                }

            }
        }, new Callback<Exception>() {
            @Override
            public void onCallback(Exception pCallbackValue) {
                loginLoadingDialog.dismiss();
                Utils.showLongToast(TelSettingActivity.this, "验证码发送失败!");
            }
        }, true, "");
    }
    private void ValidationCodeSerive(final String tel,final String vcode) {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();

            @Override
            public Boolean call() throws Exception {
                String info = "{\"tel\":\""+tel+"\",\"vcode\":\""+vcode+"\"}";
                String resultJson = http.Post(Constants.Validatione_URL,info);
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
                    getModifySerive(tel);

                } else {
                    com.maxsix.bingo.util.Utils.showLongToast(TelSettingActivity.this, "验证码验证失败");

                }

            }
        }, new Callback<Exception>() {
            @Override
            public void onCallback(Exception pCallbackValue) {
                Utils.showLongToast(TelSettingActivity.this, "注册失败!请检查网络");
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
    public void modifyAliAccount() {
        String phone = et_usertel.getText().toString().trim();
        String code = et_code.getText().toString().trim();
        boolean Sign1 = et_code.getText().length() ==6;
        if (phone.length() == 11) {
            if (!Utils.isMobileNO(phone)) {
                Utils.showLongToast(context, "请输入正确的手机号码！");
            }else if(Utils.isMobileNO(phone) && Sign1){
                loginLoadingDialog = getLoadingDialog("正在提交数据……");
                loginLoadingDialog.show();
                ValidationCodeSerive(phone,code);
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
    private void getModifySerive(final String tel) {

        doAsync(new Callable<DataResult>() {
            Gson gson = new Gson();

            @Override
            public DataResult call() throws Exception {
                com.maxsix.bingo.vo.ModifyInfo obj = new com.maxsix.bingo.vo.ModifyInfo();
                obj.setAlipay(user.getAlipay());
                obj.setDeviceid("");
                obj.setWeixin(user.getWeixin());
                obj.setName(user.getName());
                obj.setTel(tel);
                String resultJson = http.Put(com.maxsix.bingo.config.Constants.User_BaseURL + "0/", gson.toJson(obj), com.maxsix.bingo.config.Constants.CONTENT_TYPE_JSON);
                com.maxsix.bingo.vo.DataResult result = gson.fromJson(resultJson, com.maxsix.bingo.vo.DataResult.class);
                return result;
            }

        }, new com.maxsix.bingo.task.Callback<DataResult>() {
            @Override
            public void onCallback(DataResult pCallbackValue) {
                if (pCallbackValue != null && pCallbackValue.getId() > 0 ) {
                    loginLoadingDialog.dismiss();
                    com.maxsix.bingo.util.Utils.showLongToast(TelSettingActivity.this, "绑定成功！");
                    getUserInfo();
                } else if(pCallbackValue != null && pCallbackValue.getError() != null && pCallbackValue.getError().length > 0){
                    loginLoadingDialog.dismiss();
                    com.maxsix.bingo.util.Utils.showLongToast(TelSettingActivity.this, pCallbackValue.getError()[0]);
                }else{
                    loginLoadingDialog.dismiss();
                    com.maxsix.bingo.util.Utils.showLongToast(TelSettingActivity.this, "绑定失败！请稍候再试");
                }

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                loginLoadingDialog.dismiss();
                com.maxsix.bingo.util.Utils.showLongToast(TelSettingActivity.this, "绑定失败!，请检查网络");
            }
        }, false, "");
    }
    private void getUserInfo() {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();
            @Override
            public Boolean call() throws Exception {

                String usrInfo = http.Get(com.maxsix.bingo.config.Constants.User_BaseURL + "0/?"+System.currentTimeMillis());
                com.maxsix.bingo.vo.User user = gson.fromJson(usrInfo, com.maxsix.bingo.vo.User.class);
                if(user !=null){
                    com.maxsix.bingo.config.AppManager.getInstance().setUserSession(user);
                    return true;
                }else{
                    return false;
                }

            }

        }, new com.maxsix.bingo.task.Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
            }
        }, false,"");

    }
    @Override
    protected void findViewById() {

    }
}
