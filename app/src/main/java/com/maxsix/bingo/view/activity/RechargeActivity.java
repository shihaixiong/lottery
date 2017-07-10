package com.maxsix.bingo.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.PayResult;
import com.maxsix.bingo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.wxapi.WXPayEntryActivity;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

public class RechargeActivity extends com.maxsix.bingo.view.activity.BaseActivity implements View.OnClickListener {

    @com.maxsix.bingo.util.InjectView(R.id.et_rechargeCast)
    private EditText et_rechargeCast;
    @com.maxsix.bingo.util.InjectView(R.id.btn_recharge)
    private Button btn_recharge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        btn_recharge.setOnClickListener(this);
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
            case R.id.btn_recharge:
                    if(et_rechargeCast.getText() != null && !et_rechargeCast.getText().toString().trim().equals("")) {
                    int sumTZ = Integer.parseInt(et_rechargeCast.getText().toString().trim());
                    if(sumTZ >= 100) {
                        Intent intent = new Intent(context, WXPayEntryActivity.class);
                        intent.putExtra(com.maxsix.bingo.config.Constants.NAME, sumTZ + "");
                        startActivity(intent);
                    }else{
                        Utils.showLongToast(context, "亲，充值金额要大于等于100元哦");
                    }
                    }else{
                        Utils.showLongToast(context, "亲，请输入充值金额吧");
                    }
                break;
            default:
                break;
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
