package com.maxsix.bingo.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxsix.bingo.R;
import com.maxsix.bingo.config.AppManager;
import com.maxsix.bingo.config.Constants;
import com.maxsix.bingo.config.UpdateInfo;
import com.maxsix.bingo.config.UpdateInfoService;
import com.maxsix.bingo.dialog.FlippingLoadingDialog;
import com.maxsix.bingo.task.Callback;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.vo.DataResult;
import com.maxsix.bingo.vo.ErroeDataResult;
import com.maxsix.bingo.vo.LoginInfo;
import com.maxsix.bingo.vo.RegisterDataResult;
import com.maxsix.bingo.vo.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

//登陆
public class TelLoginActivity extends BaseActivity implements OnClickListener {
	private String letteyType;
	private Button btn_login, btn_register;
	private EditText et_usertel, et_password;
	private FlippingLoadingDialog loginLoadingDialog;
	private TextView tv_other,tv_wenti;
	private SharedPreferences sp;
	private SharedPreferences.Editor ed;
	private User user;
	private UpdateInfo info;
	private ProgressDialog pBar;

	private TextView title;
	@SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			// 如果有更新就提示
			if (isNeedUpdate()) {   //在下面的代码段
				showUpdateDialog();  //下面的代码段
			}else{

			}
		};
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_tellogin);
		AppManager.getInstance().addActivity(this);
		letteyType = getIntent().getStringExtra("letteyType");
		title = (TextView)findViewById(R.id.title);
		title.setText("账号登录");
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
		ed = sp.edit();
		Toast.makeText(TelLoginActivity.this, "正在检查版本..", Toast.LENGTH_SHORT).show();
		new Thread() {
			public void run() {
				try {
					UpdateInfoService updateInfoService = new UpdateInfoService(
							TelLoginActivity.this);
					info = updateInfoService.getUpDateInfo();
					AppManager.getInstance().setPayWay(info.getPayWay());
					AppManager.getInstance().setMulitiple(info.getMultiple());
					AppManager.getInstance().setLimitPay(Integer.parseInt(info.getLimitPay()));
					handler1.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	@Override
	protected void findViewById() {

	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	@Override
	protected void initControl() {
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_qtlogin);
		et_usertel = (EditText) findViewById(R.id.et_usertel);
		et_password = (EditText) findViewById(R.id.et_password);
		tv_other = (TextView) findViewById(R.id.tv_other);
		tv_wenti = (TextView) findViewById(R.id.tv_wenti);
		tv_other.setOnClickListener(this);
		tv_wenti.setOnClickListener(this);
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
	}

	@Override
	protected void setListener() {
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		findViewById(R.id.tv_wenti).setOnClickListener(this);
		et_usertel.addTextChangedListener(new TextChange());
		et_password.addTextChangedListener(new TextChange());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_other:
				Intent intent = new Intent(TelLoginActivity.this,
						LoginActivity.class);
				intent.putExtra("letteyType", letteyType);
				startActivity(intent);
				break;
			case R.id.tv_wenti:
				startActivity(new Intent(TelLoginActivity.this, GetpwdSettingActivity.class));
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				break;
			case R.id.btn_qtlogin:
				startActivity(new Intent(TelLoginActivity.this, TelRegisterActivity.class));
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				break;
			case R.id.btn_login:
				getLogin();
				break;
			default:
				break;
		}
	}

	private void getLogin() {
		String userName = et_usertel.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		loginLoadingDialog = getLoadingDialog("正在登录...");
		loginLoadingDialog.show();
		getLogin(userName, password);
	}

	private void getLogin(final String userName, final String password) {
		if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
			getLoginserive(userName, password);
		} else {
			Utils.showLongToast(TelLoginActivity.this, "请填写账号或密码！");
		}
	}

	private void getLoginserive(final String userName, final String psw) {

		doAsync(new Callable<ErroeDataResult>() {
			Gson gson = new Gson();
			@Override
			public ErroeDataResult call() throws Exception {
				LoginInfo obj = new LoginInfo();
				obj.setUsername(userName);
				obj.setPassword(psw);
				http.setAuthorization("");
				String resultJson = http.Post(Constants.Login_URL+"?" + System.currentTimeMillis(), gson.toJson(obj));
				ErroeDataResult result = gson.fromJson(resultJson, ErroeDataResult.class);
				return result;
			}

		}, new Callback<ErroeDataResult>() {
			@Override
			public void onCallback(ErroeDataResult result) {
				if (result !=null&&result.getToken() !=null && !result.getToken().equals("")) {
					http.setAuthorization("token " + result.getToken());
					getUserInfo();
					ed.putString("username", et_usertel.getText().toString().trim());
					ed.putString("password", et_password.getText().toString().trim());
					ed.commit();
				} else if(result != null && result.getNon_field_errors() != null && result.getNon_field_errors().length >0){
					loginLoadingDialog.dismiss();
					Utils.showLongToast(TelLoginActivity.this,result.getNon_field_errors()[0]);
				}else{
					loginLoadingDialog.dismiss();
					Utils.showLongToast(TelLoginActivity.this,"登录失败!请检查网络是否能正常上网");
				}
			}
		}, new Callback<Exception>() {

			@Override
			public void onCallback(Exception pCallbackValue) {
				loginLoadingDialog.dismiss();
				Utils.showLongToast(TelLoginActivity.this,"登录失败!请检查网络是否能正常上网");
			}
		}, false,"");

	}
	private void getUserInfo() {

		doAsync(new Callable<Boolean>() {
			Gson gson = new Gson();
			@Override
			public Boolean call() throws Exception {

				String usrInfo = http.Get(Constants.User_BaseURL + "0/?" + System.currentTimeMillis());
				user = gson.fromJson(usrInfo, User.class);
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
					if(user.getImid().equals("")) {
						Intent intent = new Intent(TelLoginActivity.this,
								LockLoginActivity.class);
						startActivity(intent);
						finish();
					}else{
						Intent intent = new Intent(TelLoginActivity.this,
								HomeActivity.class);
						startActivity(intent);
						finish();
					}
				} else {
					loginLoadingDialog.dismiss();
				}

			}
		}, new Callback<Exception>() {

			@Override
			public void onCallback(Exception pCallbackValue) {
				loginLoadingDialog.dismiss();
			}
		}, false,"");

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
			boolean Sign2 = et_usertel.getText().length() > 0;
			boolean Sign3 = et_password.getText().length() > 0;
			if (Sign2 & Sign3) {
				btn_login.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_bg_green));
				btn_login.setEnabled(true);
				btn_login.setTextColor(Color.parseColor("#ffffff"));
			} else {
				btn_login.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_unenable_green));
				btn_login.setTextColor(Color.parseColor("#aaaaaa"));
				btn_login.setEnabled(false);
			}
		}
	}
	/**
	 * 返回
	 *
	 * @param view
	 */
	public void back(View view) {
		Intent intent = new Intent(TelLoginActivity.this,
				HomeActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.push_up_in,
				R.anim.push_up_out);
		finish();
	}
	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("请升级APP至版本" + info.getVersion());
		builder.setMessage(info.getDescription());
		builder.setCancelable(false);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					downFile(info.getUrl());     //在下面的代码段
				} else {
					Toast.makeText(TelLoginActivity.this, "SD卡不可用，请插入SD卡",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.create().show();
	}

	private boolean isNeedUpdate() {

		String v = info.getVersion(); // 最新版本的版本号
		Log.i("update", v);
		if (v.equals(getVersion())) {
			return false;
		} else {
			return true;
		}
	}

	// 获取当前版本的版本号
	private String getVersion() {
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			AppManager.getInstance().setVersion(packageInfo.versionName);
			return packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}
	void downFile(final String url) {
		pBar = new ProgressDialog(TelLoginActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度

		pBar.setOnKeyListener(new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
				if (i == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
		pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pBar.setTitle("正在下载");
		pBar.setMessage("请稍候...");
		pBar.setProgress(0);
		pBar.setCanceledOnTouchOutside(false);
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					int length = (int) entity.getContentLength();   //获取文件大小
					pBar.setMax(length);                            //设置进度条的总长度
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"ttdj.apk");
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[10];   //这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一 下就下载完了，看不出progressbar的效果。
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							process += ch;
							pBar.setProgress(process);       //这里就是关键的实时更新进度了！
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	void down() {
		handler1.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});
	}
	//安装文件，一般固定写法
	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), "ttdj.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
}
