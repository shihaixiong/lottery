package com.maxsix.bingo.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.PayResult;
import com.maxsix.bingo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxsix.bingo.util.Utils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import com.squareup.okhttp.Callback;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import android.widget.Toast;

public class TZActivity extends com.maxsix.bingo.view.activity.BaseActivity implements View.OnClickListener {

    private static final int SDK_PAY_FLAG = 1;
    private TextView alipayTextView;
    private TextView txt_xunhai;
    private TextView txt_weixin;
    private Timer timer;
    private com.maxsix.bingo.circleprogress.DonutProgress donutProgress;
    private TextView sumCast;
    private ArrayList<com.maxsix.bingo.vo.BettingInfo> infos;
    private com.maxsix.bingo.vo.User user;
    private IWXAPI api;
    private Integer cast;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //Utils.showLongToast(context, "支付成功");
                        donutProgress.setVisibility(View.VISIBLE);
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        donutProgress.setProgress(donutProgress.getProgress() + 1);

                                    }
                                });
                            }
                        }, 1000, 100);
                        if(infos !=null && infos.size()>0) {
                            for (int a = 0; a < infos.size(); a++) {
                                postBettingserive(infos.get(a), a);
                            }
                        }
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            com.maxsix.bingo.util.Utils.showLongToast(context, "支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            com.maxsix.bingo.util.Utils.showLongToast(context, "支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tz);

        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        donutProgress = (com.maxsix.bingo.circleprogress.DonutProgress) findViewById(R.id.donut_progress);
        sumCast = (TextView) findViewById(R.id.sumCast);
        cast = Integer.parseInt(getIntent().getStringExtra(com.maxsix.bingo.config.Constants.NAME));
        infos = com.maxsix.bingo.config.AppManager.getInstance().getInfos();
        sumCast.setText("¥"+cast+".00");
        user = com.maxsix.bingo.config.AppManager.getInstance().getUserSession();
        alipayTextView = (TextView) findViewById(R.id.txt_alipay);
        alipayTextView.setOnClickListener(this);
        txt_weixin = (TextView) findViewById(R.id.txt_weixin);
        txt_weixin.setOnClickListener(this);
        txt_xunhai = (TextView) findViewById(R.id.txt_xunhai);
        txt_xunhai.setOnClickListener(this);

        api = WXAPIFactory.createWXAPI(this, com.maxsix.bingo.config.Constants.APP_ID, false);
        api.registerApp(com.maxsix.bingo.config.Constants.APP_ID);
    }
    public void alipay() {
        // 服务器签名的地址
        String url = com.maxsix.bingo.config.Constants.MAIN_ENGINE+"ali_unifiedorder/";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        // 格式与微信支付兼容
        // body 是对用户显示的支付内容，用户凭这个知道是支付充值或其他动作
        // moeny 是支付金额，单位分。1 = 1分钱
        // attach 是备注，用户看不到，用来给回调通知处理的。格式：RECHARGE:用户名
        com.maxsix.bingo.vo.PayInfo data = new com.maxsix.bingo.vo.PayInfo();
        data.setBody("资金托管");
        data.setMoney(cast * 100);
        data.setAttach("RECHARGE:" + user.getUsername());
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"),gson.toJson(data)))
                .build();

        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                Gson gson = new Gson();
                Map<String, String> jsOrder = gson.fromJson(response.body().string(), new TypeToken<Map<String, String>>() {
                }.getType());
                final String payInfo = jsOrder.get("request");
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(TZActivity.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(payInfo, true);

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
        });


    }
    public void wxpay() {
        String url = "http://cp.gtweixin.com/api/wx_unifiedorder/";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        com.maxsix.bingo.util.Utils.showLongToast(context, "获取订单中...");
        try {
            // 格式与微信支付兼容
            // body 是对用户显示的支付内容，用户凭这个知道是支付充值或其他动作
            // moeny 是支付金额，单位分。1 = 1分钱
            // attach 是备注，用户看不到，用来给回调通知处理的。格式：RECHARGE:用户名
            com.maxsix.bingo.vo.PayInfo data = new com.maxsix.bingo.vo.PayInfo();
            data.setBody("资金托管");
            data.setMoney(cast * 100);
            data.setTradetype("APP");
            data.setAttach("RECHARGE:" + user.getUsername());
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), gson.toJson(data)))
                    .build();

            Call call = mOkHttpClient.newCall(request);
            //请求加入调度
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }
                @Override
                public void onResponse(final Response response) throws IOException {
                    String content = response.body().string();
                    Log.e("get server pay params:", content);

                    Gson gson = new Gson();
                    ResultMsg jsOrder = gson.fromJson(content, ResultMsg.class);

                    PayReq req = new PayReq();
                    req.appId = jsOrder.getData().getAppid();
                    req.partnerId = jsOrder.getData().getPartnerid();
                    req.prepayId = jsOrder.getData().getPrepayid();
                    req.nonceStr = jsOrder.getData().getNoncestr();
                    req.timeStamp = jsOrder.getData().getTimestamp();
                    req.packageValue = "Sign=WXPay";
                    req.sign = jsOrder.getData().getSign();
                    req.extData			= "app data"; // optional
                    boolean ok = api.sendReq(req);
                    Log.e("pay :", Boolean.toString(ok));
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
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
        switch (v.getId()) {
            case R.id.txt_alipay:
                alipay();
                break;
            case R.id.txt_weixin:
                wxpay();
                break;
            case R.id.txt_xunhai:
                xunhai();
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
    private void postBettingserive(final com.maxsix.bingo.vo.BettingInfo _bettinginfo,final Integer _next) {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();

            @Override
            public Boolean call() throws Exception {
                String info = http.Post(com.maxsix.bingo.config.Constants.User_BaseURL + "0/bettings/", gson.toJson(_bettinginfo));
                com.maxsix.bingo.vo.DataResult result = gson.fromJson(info, com.maxsix.bingo.vo.DataResult.class);
                if (result != null && result.getError() == null) {
                    return true;
                } else {
                    return true;
                }
            }

        }, new com.maxsix.bingo.task.Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {
                if (pCallbackValue == true) {
                    if (_next == infos.size() - 1) {
                        getUserInfo();
                        Utils.showLongToast(context, "投注成功");
                        donutProgress.setVisibility(View.GONE);
                        Intent intent = new Intent(TZActivity.this,
                                BettingListActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_up_in,
                                R.anim.push_up_out);
                        finish();
                    }
                }
            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {

            }
        }, false, "");

        }
    public void xunhai(){

        if(cast < user.getRemain()){
        if(infos !=null && infos.size()>0) {
            donutProgress.setVisibility(View.VISIBLE);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            donutProgress.setProgress(donutProgress.getProgress() + 1);

                        }
                    });
                }
            }, 1000, 100);
            for (int a = 0; a < infos.size(); a++) {
                postBettingserive(infos.get(a), a);
            }
        }
        }else{
            com.maxsix.bingo.util.Utils.showLongToast(context, "余额不足");
        }
    }
    private void getUserInfo() {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();

            @Override
            public Boolean call() throws Exception {

                String usrInfo = http.Get(com.maxsix.bingo.config.Constants.User_BaseURL + "0/?"+System.currentTimeMillis());
                com.maxsix.bingo.vo.User user = gson.fromJson(usrInfo, com.maxsix.bingo.vo.User.class);
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
                    donutProgress.setVisibility(View.GONE);
                }

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                donutProgress.setVisibility(View.GONE);
            }
        }, false, "");

    }
    private class ResultMsg {
        private ResultValue data;

        public ResultValue getData() {
            return data;
        }

        public void setData(ResultValue data) {
            this.data = data;
        }
    }

    private class ResultValue {
        private String timestamp;
        private String sign;
        private String partnerid;
        private String appid;
        private String prepayid;
        private String noncestr;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }
    }
    @Override
    protected void findViewById() {

    }
}
