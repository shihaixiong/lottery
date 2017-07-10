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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxsix.bingo.R;
import com.maxsix.bingo.config.AppManager;
import com.maxsix.bingo.config.UpdateInfo;
import com.maxsix.bingo.config.UpdateInfoService;
import com.maxsix.bingo.view.LockPatternView;
import com.maxsix.bingo.vo.DataResult;
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

public class LockLoginActivity extends BaseActivity implements UnlockAppView.OnPatternChangeListener{
    private TextView mLockPatternHint;
    private UnlockAppView mLockPatternView;
    private String lockPassword;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_login);
        mLockPatternHint = (TextView) findViewById(R.id.lock_pattern_hint);
        mLockPatternView = (UnlockAppView) findViewById(R.id.lock_pattern_view);
        mLockPatternView.setOnPatternChangeListener(this);
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
        if (patternPassword == "") {
            mLockPatternHint.setText("至少5个点");
        } else {
            count++;
            if(count == 2){
                if(lockPassword.equals( patternPassword)){
                    getModifySerive(patternPassword);
                }else {
                    Toast.makeText(LockLoginActivity.this, "绘制图案不一致，请重新绘制",
                            Toast.LENGTH_SHORT).show();
                    count = 0;
                }
                finish();
            }else{
                lockPassword = patternPassword;
                Toast.makeText(LockLoginActivity.this, "请再绘制一遍",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPatternStarted(boolean isStarted) {
        if (isStarted) {
            //mLockPatternHint.setText("请绘制登录图案");
        }
    }
    private void getModifySerive(final String imid) {

        doAsync(new Callable<DataResult>() {
            Gson gson = new Gson();

            @Override
            public DataResult call() throws Exception {
                String jsonvalue = "{\n" +
                        "  \"imid\": \""+imid+"\"\n" +
                        "}";
                String resultJson = http.Put(com.maxsix.bingo.config.Constants.User_BaseURL + "0/", jsonvalue, com.maxsix.bingo.config.Constants.CONTENT_TYPE_JSON);
                com.maxsix.bingo.vo.DataResult result = gson.fromJson(resultJson, com.maxsix.bingo.vo.DataResult.class);
                return result;
            }

        }, new com.maxsix.bingo.task.Callback<DataResult>() {
            @Override
            public void onCallback(DataResult pCallbackValue) {
                if (pCallbackValue != null && pCallbackValue.getId() > 0 ) {
                    Toast.makeText(LockLoginActivity.this, "绘制成功！",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LockLoginActivity.this,
                            HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LockLoginActivity.this, "绘制失败！",
                            Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                Toast.makeText(LockLoginActivity.this, "绘制失败！",
                        Toast.LENGTH_SHORT).show();
            }
        }, false, "");
    }

}
