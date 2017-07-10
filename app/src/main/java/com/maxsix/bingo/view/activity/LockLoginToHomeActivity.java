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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
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
import com.maxsix.bingo.view.LockPatternView;
import com.maxsix.bingo.vo.ErroeDataResult;
import com.maxsix.bingo.vo.LoginInfo;
import com.maxsix.bingo.vo.User;
import com.maxsix.bingo.widget.UnlockAppView;

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

public class LockLoginToHomeActivity extends BaseActivity implements UnlockAppView.OnPatternChangeListener, View.OnClickListener{
    private TextView mLockPatternHint;
    private TextView forgetPassword;
    private UnlockAppView mLockPatternView;
    private SharedPreferences sp;
    private String username;
    private String password;
    private String lockPassword;
    private String letteyType;
    private FlippingLoadingDialog loadingDialog;
    private LinearLayout lllabel;
    private TextView usernamPassword;
    private UpdateInfo info;
    private ProgressDialog pBar;
    private TextView title;
    private User user;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_login);
        mLockPatternHint = (TextView) findViewById(R.id.lock_pattern_hint);
        title = (TextView)findViewById(R.id.title);
        title.setText("登    录");
        mLockPatternView = (UnlockAppView) findViewById(R.id.lock_pattern_view);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        usernamPassword = (TextView) findViewById(R.id.usernamPassword);
        lllabel = (LinearLayout)findViewById(R.id.lllabel);
        lllabel.setVisibility(View.VISIBLE);
        mLockPatternView.setOnPatternChangeListener(this);
        sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        username = sp.getString("username", "");
        password = sp.getString("password", "");
        Toast.makeText(LockLoginToHomeActivity.this, "正在检查版本..", Toast.LENGTH_SHORT).show();
        // 自动检查有没有新版本 如果有新版本就提示更新
        new Thread() {
            public void run() {
                try {
                    UpdateInfoService updateInfoService = new UpdateInfoService(
                            LockLoginToHomeActivity.this);
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
        if(username.equals("") || password.equals("")){
            Intent intent = new Intent(LockLoginToHomeActivity.this,
                    TelLoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            letteyType = getIntent().getStringExtra("letteyType");
                if (Utils.isMobileNO(username)) {
                    mLockPatternHint.setText(username.substring(0, 3) + "****" + username.substring(7, 11));
                } else {
                    mLockPatternHint.setText(username);
                }
            forgetPassword.setOnClickListener(this);
            usernamPassword.setOnClickListener(this);
            forgetPassword.setVisibility(View.VISIBLE);
            loadingDialog = getLoadingDialog("正在初始化...");
            loadingDialog.show();
            getLoginserive(username, password);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
    public void onPatternChange(String patternPassword) {
        if (patternPassword.length() <5) {
            Toast.makeText(LockLoginToHomeActivity.this, "手势密码错误！",
                    Toast.LENGTH_SHORT).show();
           //mLockPatternView.mSelectedPointViewList.clear();
        } else {
            if(patternPassword.equals(lockPassword)){
                AppManager.getInstance().setUserSession(user);
                Intent intent = new Intent(LockLoginToHomeActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(LockLoginToHomeActivity.this, "手势密码错误！",
                        Toast.LENGTH_SHORT).show();
                //mLockPatternView.mSelectedPointViewList.clear();
            }
        }
    }

    @Override
    public void onPatternStarted(boolean isStarted) {
//        if (isStarted) {
//            mLockPatternHint.setText("请绘制登录图案");
//        }
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
                } else if(result != null && result.getNon_field_errors() != null && result.getNon_field_errors().length >0){
                    loadingDialog.dismiss();
                    Toast.makeText(LockLoginToHomeActivity.this, "手势登录异常，请用账户密码登录！",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LockLoginToHomeActivity.this,
                            TelLoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    loadingDialog.dismiss();
                    Toast.makeText(LockLoginToHomeActivity.this, "网络异常！",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LockLoginToHomeActivity.this,
                            TelLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, new Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                loadingDialog.dismiss();
                Toast.makeText(LockLoginToHomeActivity.this, "网络异常！",
                        Toast.LENGTH_SHORT).show();
            }
        }, false,"");

    }
    private void getUserInfo() {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();
            @Override
            public Boolean call() throws Exception {
                loadingDialog.dismiss();
                String usrInfo = http.Get(Constants.User_BaseURL + "0/?" + System.currentTimeMillis());
                user = gson.fromJson(usrInfo, User.class);
                if(user !=null){
                    lockPassword = user.getImid();
                    return true;
                }else{
                    return false;
                }
            }
        }, new Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {
                if(pCallbackValue){
                    //loadingDialog.dismiss();
                }
            }
        }, new Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                loadingDialog.dismiss();
            }
        }, false,"");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.usernamPassword:
            case R.id.forgetPassword:
                Intent intent = new Intent(LockLoginToHomeActivity.this,
                        TelLoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
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
                    Toast.makeText(LockLoginToHomeActivity.this, "SD卡不可用，请插入SD卡",
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
        pBar = new ProgressDialog(LockLoginToHomeActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度

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
