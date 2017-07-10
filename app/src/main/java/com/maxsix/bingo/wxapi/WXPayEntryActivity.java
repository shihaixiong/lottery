package com.maxsix.bingo.wxapi;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.PayResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxsix.bingo.R;
import com.maxsix.bingo.config.AppManager;
import com.maxsix.bingo.config.Constants;
import com.maxsix.bingo.dialog.FlippingLoadingDialog;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.view.activity.BalanceActivity;
import com.maxsix.bingo.view.activity.BaseActivity;
import com.maxsix.bingo.view.activity.BettingListActivity;
import com.maxsix.bingo.vo.BettingInfo;
import com.maxsix.bingo.vo.User;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.Timer;
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler,View.OnClickListener{

	private static final int SDK_PAY_FLAG = 1;
	private ArrayList<BettingInfo> infos;
	private int oldMoney;
	private int times;
	private int newMoney;
	private User user;
	private IWXAPI api;
	private Integer cast;
	private TextView sumCast;
	private TextView alipayTextView;
	private TextView txt_weixin;
	private Timer timer;
	private com.maxsix.bingo.circleprogress.DonutProgress donutProgress;

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
						getUserInfo();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
		com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.registerApp(Constants.APP_ID);
        api.handleIntent(getIntent(), this);
		donutProgress = (com.maxsix.bingo.circleprogress.DonutProgress) findViewById(R.id.donut_progress);
		sumCast = (TextView) findViewById(R.id.sumCast);
		cast = Integer.parseInt(getIntent().getStringExtra(com.maxsix.bingo.config.Constants.NAME));
		sumCast.setText("¥"+cast+".00");
		user = com.maxsix.bingo.config.AppManager.getInstance().getUserSession();
		alipayTextView = (TextView) findViewById(R.id.txt_alipay);
		alipayTextView.setOnClickListener(this);
		txt_weixin = (TextView) findViewById(R.id.txt_weixin);
		txt_weixin.setOnClickListener(this);
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
		switch (v.getId()) {
			case R.id.txt_alipay:
				//com.maxsix.bingo.util.Utils.showLongToast(context, "支付宝支付维护中，请选择微信支付");
				alipay();
				break;
			case R.id.txt_weixin:
				//wxpay();
				break;
			default:
				break;
		}
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
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		com.maxsix.bingo.vo.PayInfo data = new com.maxsix.bingo.vo.PayInfo();
		data.setBody(year+month+date+hour+minute+second+Utils.genRandomNum(4)+"");
		data.setMoney(cast * 100);
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
				Gson gson = new Gson();
				Map<String, String> jsOrder = gson.fromJson(response.body().string(), new TypeToken<Map<String, String>>() {
				}.getType());
				final String payInfo = jsOrder.get("request");
				Runnable payRunnable = new Runnable() {

					@Override
					public void run() {
						// 构造PayTask 对象
						PayTask alipay = new PayTask(WXPayEntryActivity.this);
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
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		try {
			com.maxsix.bingo.vo.PayInfo data = new com.maxsix.bingo.vo.PayInfo();
			data.setBody(year+month+date+hour+minute+second+Utils.genRandomNum(4)+"");
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
					req.extData = "app data"; // optional
					api.sendReq(req);
				}
			});
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
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
			handler.postDelayed(runnable,2000);
		} else{
			Utils.showLongToast(context, "支付失败");
		}
	}
	private void getUserInfo() {

		doAsync(new Callable<Boolean>() {
			Gson gson = new Gson();
			@Override
			public Boolean call() throws Exception {

				String usrInfo = http.Get(com.maxsix.bingo.config.Constants.User_BaseURL + "0/?" + System.currentTimeMillis());
				user = gson.fromJson(usrInfo, com.maxsix.bingo.vo.User.class);
				if(user !=null){
					oldMoney = AppManager.getInstance().getUserSession().getRemain();
					newMoney = user.getRemain();
					AppManager.getInstance().setUserSession(user);
					return true;
				}else{
					return false;
				}

			}

		}, new com.maxsix.bingo.task.Callback<Boolean>() {
			@Override
			public void onCallback(Boolean pCallbackValue) {
				if (pCallbackValue == true) {
						int sumTZMoney = cast * 100;
						if((newMoney-oldMoney) == sumTZMoney){
							handler.removeCallbacks(runnable);
							donutProgress.setVisibility(View.GONE);
							com.maxsix.bingo.util.Utils.start_Activity(context,BalanceActivity.class);
						}else if(times == 3){
							handler.removeCallbacks(runnable);
							donutProgress.setVisibility(View.GONE);
							Utils.showLongToast(context, "网络原因暂时获取不了充值结果，请稍候查看余额");
							finish();
						}
						else {
							times++;
						}
				}else {
					handler.removeCallbacks(runnable);
					Utils.showLongToast(context, "获取用户信息失败");
					donutProgress.setVisibility(View.GONE);
				}

			}
		}, new com.maxsix.bingo.task.Callback<Exception>() {

			@Override
			public void onCallback(Exception pCallbackValue) {
				donutProgress.setVisibility(View.GONE);
			}
		}, false,"");

	}
	Handler handler=new Handler();
	Runnable runnable=new Runnable() {
		@Override
		public void run() {
			getUserInfo();
			handler.postDelayed(this, 2000);
		}
	};
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
	/**
	 * 返回
	 *
	 * @param view
	 */
	public void back(View view) {
		finish();
	}
}