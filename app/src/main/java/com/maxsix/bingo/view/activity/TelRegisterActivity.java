package com.maxsix.bingo.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.maxsix.bingo.R;
import com.maxsix.bingo.config.Constants;
import com.maxsix.bingo.dialog.FlippingLoadingDialog;
import com.maxsix.bingo.task.Callback;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.vo.DataResult;
import com.maxsix.bingo.vo.ErroeDataResult;
import com.maxsix.bingo.vo.RegisterDataResult;

import java.util.concurrent.Callable;

//注册
public class TelRegisterActivity extends BaseActivity implements OnClickListener {

	private Button btn_register, btn_send;
	private EditText et_usertel, et_password, et_code,et_inviteCod;
	private MyCount mc;
	private FlippingLoadingDialog registerLoadingDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_telregister);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initControl() {
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_register = (Button) findViewById(R.id.btn_register);
		et_usertel = (EditText) findViewById(R.id.et_usertel);
		et_password = (EditText) findViewById(R.id.et_password);
		et_code = (EditText) findViewById(R.id.et_code);
		et_inviteCod = (EditText) findViewById(R.id.et_inviteCod);
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
	}

	@Override
	protected void setListener() {
		btn_send.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		et_usertel.addTextChangedListener(new TelTextChange());
		et_password.addTextChangedListener(new TextChange());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			sendCode();
			break;
		case R.id.btn_register:
			getRegister();
			break;
		default:
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
	private void getRegister() {
		final String name = et_usertel.getText().toString();
		final String pwd = et_password.getText().toString();
		String code = et_code.getText().toString();
		String inviteCode = et_inviteCod.getText().toString().trim();
		if (!Utils.isMobileNO(name) && name.length() != 11) {
			Utils.showLongToast(TelRegisterActivity.this, "请使用手机号码注册账户！ ");
			return;
		}
		if (TextUtils.isEmpty(code)) {
			Utils.showLongToast(TelRegisterActivity.this, "请填写手机号码，并获取验证码！");
			return;
		}
		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)
				|| TextUtils.isEmpty(code) || TextUtils.isEmpty(inviteCode)) {
			Utils.showLongToast(TelRegisterActivity.this, "请填写信息！");
			return;
		}
		registerLoadingDialog = getLoadingDialog("正在注册...  ");
		registerLoadingDialog.show();
		getRigisterSerive(name, pwd, code, inviteCode);
		//btn_register.setEnabled(false);
		//btn_send.setEnabled(false);

	}

	private void getRigisterSerive(final String tel,final String password,final String vcode,final String invitecode) {

		doAsync(new Callable<DataResult>() {
			Gson gson = new Gson();

			@Override
			public DataResult call() throws Exception {
				com.maxsix.bingo.vo.RegisterInfo info = new com.maxsix.bingo.vo.RegisterInfo();
				info.setUsername(tel);
				info.setPassword(password);
				info.setInvitecode(invitecode);
				info.setVcode(vcode);
				String resultJson = http.Post(Constants.Register_URL,gson.toJson(info));
				try {
					RegisterDataResult result = gson.fromJson(resultJson, RegisterDataResult.class);
					return result;
				}catch (Exception e){
					ErroeDataResult result = gson.fromJson(resultJson, ErroeDataResult.class);
					return result;
				}
			}

		}, new Callback<DataResult>() {
			@Override
			public void onCallback(DataResult result) {
				if (result != null && result.getId() > 0) {
					//http.setAuthorization(result.getToken());
					com.maxsix.bingo.util.Utils.showLongToast(TelRegisterActivity.this, "注册成功!，正在返回登录界面");
					Intent intent = new Intent(TelRegisterActivity.this,
							TelLoginActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_up_in,
							R.anim.push_up_out);
					finish();
				} else {
					ErroeDataResult error = (ErroeDataResult)result;
					if (error != null && error.getNon_field_errors() != null && error.getNon_field_errors().length > 0) {
						registerLoadingDialog.dismiss();
						Utils.showLongToast(TelRegisterActivity.this, error.getNon_field_errors()[0]);
					} else if (error != null && error.getUsername() != null && error.getUsername().length > 0) {
						registerLoadingDialog.dismiss();
						Utils.showLongToast(TelRegisterActivity.this, "注册失败!该手机号或是账户已经注册过");
					} else if (result != null && result.getError() != null) {
						registerLoadingDialog.dismiss();
						Utils.showLongToast(TelRegisterActivity.this, "注册失败!" + result.getError());
					} else {
						registerLoadingDialog.dismiss();
						Utils.showLongToast(TelRegisterActivity.this, "注册失败!请检查数据完整性");
					}
				}
			}
		}, new Callback<Exception>() {

			@Override
			public void onCallback(Exception pCallbackValue) {
				registerLoadingDialog.hide();
				Utils.showLongToast(TelRegisterActivity.this,"注册失败!请检查网络是否能正常上网");
			}
		}, true, "");
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
					com.maxsix.bingo.util.Utils.showLongToast(TelRegisterActivity.this, "验证码发送成功，请查收");

				} else {
						com.maxsix.bingo.util.Utils.showLongToast(TelRegisterActivity.this, "验证码发送失败");
				}

			}
		}, new Callback<Exception>() {

			@Override
			public void onCallback(Exception pCallbackValue) {
				registerLoadingDialog.hide();
				Utils.showLongToast(TelRegisterActivity.this, "注册失败!请检查网络是否能正常上网");
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
			boolean Sign1 = et_code.getText().length() ==6;
			boolean Sign3 = et_password.getText().length() > 5;
			if (phone.length() == 11) {
				if (!Utils.isMobileNO(phone)) {
					Utils.showLongToast(context, "请输入正确的手机号码！");
//					btn_register.setBackgroundDrawable(getResources().getDrawable(
//							R.drawable.btn_unenable_green));
//					btn_register.setEnabled(false);
					btn_send.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.btn_unenable_green));
					btn_send.setEnabled(false);
				}else if(Utils.isMobileNO(phone) && Sign1 && Sign3){
//					btn_register.setBackgroundDrawable(getResources().getDrawable(
//							R.drawable.btn_enable_green));
//					btn_register.setEnabled(true);
//					btn_send.setBackgroundDrawable(getResources().getDrawable(
//							R.drawable.btn_enable_green));
//					btn_send.setEnabled(true);
				}
			}
		}
	}

	// EditText监听器
	class TextChange implements TextWatcher {

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
			boolean Sign1 = et_code.getText().length() ==6;
			boolean Sign2 = et_usertel.getText().length() == 11;
			boolean Sign3 = et_password.getText().length() > 4;

			if (Sign1 & Sign2 & Sign3) {
				btn_register.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_bg_green));
				btn_register.setTextColor(0xFFFFFFFF);
				btn_register.setEnabled(true);
			} else {
				btn_register.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_unenable_green));
				btn_register.setEnabled(false);
				//btn_register.setTextColor(Color.parseColor("#aaaaaa"));

			}
		}
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

	/**
	 * 返回
	 *
	 * @param view
	 */
	public void back(View view) {
		finish();
	}
}
