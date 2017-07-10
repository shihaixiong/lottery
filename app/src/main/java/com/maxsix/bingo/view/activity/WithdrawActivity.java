package com.maxsix.bingo.view.activity;

import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maxsix.bingo.R;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;

import java.util.concurrent.Callable;

public class WithdrawActivity extends com.maxsix.bingo.view.activity.BaseActivity implements View.OnClickListener {

    @InjectView(R.id.radiogroup1)
    private RadioGroup radiogroup1;
    @com.maxsix.bingo.util.InjectView(R.id.radiobuttonAli)
    private RadioButton radiobuttonAli;
    @com.maxsix.bingo.util.InjectView(R.id.radiobuttonWX)
    private RadioButton radiobuttonWX;
    @com.maxsix.bingo.util.InjectView(R.id.withdrawMoney)
    private EditText withdrawMoney;
    @com.maxsix.bingo.util.InjectView(R.id.tv_canwithrawMoney)
    private TextView tv_canwithrawMoney;
    @com.maxsix.bingo.util.InjectView(R.id.btnWithdrawMoney)
    private Button btnWithdrawMoney;

    private com.maxsix.bingo.vo.User user;
    private com.maxsix.bingo.dialog.FlippingLoadingDialog loginLoadingDialog;
    private String payWay="";
    private int getMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        user = com.maxsix.bingo.config.AppManager.getInstance().getUserSession();
        if(user.getAlipay() !=null && !user.getAlipay().equals("")){
            radiobuttonAli.setChecked(true);
            radiobuttonAli.setText("支付宝账号("+user.getAlipay()+")");
        }
        if(user.getWeixin() !=null && !user.getWeixin().equals("")){
            radiobuttonWX.setChecked(true);
            radiobuttonWX.setText("银行账号("+user.getWeixin()+")");
        }
        if(user !=null){
            tv_canwithrawMoney.setText("余额可提现额度¥"+user.getRemain()/100);
        }
        btnWithdrawMoney.setOnClickListener(this);
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
            case R.id.btnWithdrawMoney:
                try{
                    if(withdrawMoney.getText().toString() != "" && Utils.isInteger(withdrawMoney.getText().toString()) && Integer.parseInt(withdrawMoney.getText().toString())>0){
                        getMoney = Integer.parseInt(withdrawMoney.getText().toString());
                        if(getMoney > 100) {
                            switch (radiogroup1.getCheckedRadioButtonId()) {
                                case R.id.radiobuttonAli:
                                    if (user.getAlipay() != null && !user.getAlipay().equals("")) {
                                        payWay = "ali:" + user.getAlipay();
                                        getUserInfo();
                                    } else {
                                        Utils.showLongToast(WithdrawActivity.this, "亲，请先在个人设置那里完善收款账号哦！");
                                    }
                                    break;
                                case R.id.radiobuttonWX:
                                    if (user.getWeixin() != null && !user.getWeixin().equals("")) {
                                        payWay = "bank:" + user.getWeixin();
                                        getUserInfo();
                                    } else {
                                        Utils.showLongToast(WithdrawActivity.this, "亲，请先在个人设置那里完善银行卡账号哦！");
                                    }
                                    break;
                                default:
                                    break;
                            }
                            ;
                        }else{
                            com.maxsix.bingo.util.Utils.showLongToast(WithdrawActivity.this, "亲，提现金额必须大于100元哦！");
                        }
                    }else{
                        com.maxsix.bingo.util.Utils.showLongToast(WithdrawActivity.this, "亲，请输入要提现金额哦！");
                    }
                }catch (Exception e){
                    com.maxsix.bingo.util.Utils.showLongToast(WithdrawActivity.this, "亲，输入错误！");
                }
                break;
            default:
                break;
        }
    }
    private void getwithdrawMoney(final String _account) {
        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();

            @Override
            public Boolean call() throws Exception {
                String json = "{\"money\":"+getMoney*100+", \"bank\": \""+_account+"\"}";
                String resultJson = http.Post(com.maxsix.bingo.config.Constants.User_BaseURL + "0/payments/", json, com.maxsix.bingo.config.Constants.CONTENT_TYPE_JSON);
                com.maxsix.bingo.vo.DataResult result = gson.fromJson(resultJson, com.maxsix.bingo.vo.DataResult.class);
                if(result != null && result.getError() == null){
                    return true;
                }else
                {
                    return false;
                }

            }

        }, new com.maxsix.bingo.task.Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {
                if (pCallbackValue == true) {
                    loginLoadingDialog.dismiss();
                    com.maxsix.bingo.util.Utils.showLongToast(WithdrawActivity.this, "提现申请已提交！");
                    finish();
                } else {
                    loginLoadingDialog.dismiss();
                    com.maxsix.bingo.util.Utils.showLongToast(WithdrawActivity.this, "申请失败!请重新提交！");
                }

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                loginLoadingDialog.dismiss();
                com.maxsix.bingo.util.Utils.showLongToast(WithdrawActivity.this, "申请失败!请重新提交");
            }
        }, false, "");
    }
    private void getUserInfo() {
        loginLoadingDialog = getLoadingDialog("正在提交申请……");
        loginLoadingDialog.show();
        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();
            @Override
            public Boolean call() throws Exception {
                String usrInfo = http.Get(com.maxsix.bingo.config.Constants.User_BaseURL + "0/?" + System.currentTimeMillis());
                user = gson.fromJson(usrInfo, com.maxsix.bingo.vo.User.class);
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
                if (pCallbackValue == true) {
                    if(user.getRemain() < getMoney){
                        loginLoadingDialog.dismiss();
                        Utils.showLongToast(WithdrawActivity.this, "亲，余额不足哦!");
                    }else {
                        getwithdrawMoney(payWay);
                    }

                }
            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
            }
        }, false,"");

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
