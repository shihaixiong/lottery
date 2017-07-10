package com.maxsix.bingo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maxsix.bingo.R;
import com.maxsix.bingo.config.AppManager;

import java.text.DecimalFormat;
import java.util.concurrent.Callable;

public class BalanceActivity extends com.maxsix.bingo.view.activity.BaseActivity implements View.OnClickListener {

    @com.maxsix.bingo.util.InjectView(R.id.tv_sumMoney)
    private TextView tv_sumMoney;
    private com.maxsix.bingo.vo.User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        getUserInfo();
    }

    @Override
    protected void findViewById() {

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

    }
    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        startActivity(new Intent(BalanceActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    /**
     * 充值
     * @param view
     */
    public void recharge(View view){
        String payWay = AppManager.getInstance().getPayWay();
        if(payWay.equals("YES")) {
            startActivity(new Intent(BalanceActivity.this, RechargeActivity.class));
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        }else if(payWay.equals("NO")){
            startActivity(new Intent(BalanceActivity.this, WeixinArtificialActivity.class));
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        }
    }

    /**
     * 提现
     * @param view
     */
    public void withdraw(View view){
        if(user.getName() == null || user.getName().equals("")){
            com.maxsix.bingo.util.Utils.showLongToast(context, "请先在个人中心绑定您的真实姓名");
            return;
        }
        if(user.getWeixin() ==null && user.getAlipay() ==null){
            com.maxsix.bingo.util.Utils.showLongToast(context, "请先在个人中心绑定您支付宝账号或是银行卡信息");
            return;
        }
        if(user.getWeixin().equals("") && user.getAlipay().equals("")){
            com.maxsix.bingo.util.Utils.showLongToast(context, "请先在个人中心绑定您支付宝账号或是银行卡信息");
            return;
        }
        startActivity(new Intent(BalanceActivity.this, WithdrawActivity.class));
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }
    private void getUserInfo() {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();

            @Override
            public Boolean call() throws Exception {

                String usrInfo = http.Get(com.maxsix.bingo.config.Constants.User_BaseURL + "0/?" + System.currentTimeMillis());
                user = gson.fromJson(usrInfo, com.maxsix.bingo.vo.User.class);
                if (user != null) {
                    com.maxsix.bingo.config.AppManager.getInstance().setUserSession(user);
                    return true;
                } else {
                    return false;
                }

            }

        }, new com.maxsix.bingo.task.Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {
                if (pCallbackValue == true) {
                    if(user != null) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        double sumwind = user.getRemain()/100.0;
                        tv_sumMoney.setText("¥"+Double.parseDouble(df.format(sumwind)));
                    }
                } else {
                    com.maxsix.bingo.util.Utils.showLongToast(context, "拉取用户信息失败，尝试退出重新登录");
                }

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {

            }
        }, false, "");

    }

    @Override
    public void onPause() {
        super.onPause();
        getUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }
}
