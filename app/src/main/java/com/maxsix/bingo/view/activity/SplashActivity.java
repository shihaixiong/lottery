package com.maxsix.bingo.view.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.maxsix.bingo.R;
import com.maxsix.bingo.config.Constants;
import com.maxsix.bingo.config.UpdateInfo;
import com.maxsix.bingo.config.UpdateInfoService;

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


public class SplashActivity extends BaseActivity {
    protected Handler mHandler = null;
    public static final String TAG = SplashActivity.class.getSimpleName();
    private ImageView mSplashItem_iv = null;
    private Intent intent;
    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            intent = new Intent(Intent.ACTION_MAIN);
            intent.setClass(getApplication(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    };
    @Override
    protected void findViewById() {

    }

    @Override
    protected void initControl() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mMainHandler.sendEmptyMessageDelayed(0, 2500);
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        Constants.SCREEN_DENSITY = metrics.density;
//        Constants.SCREEN_HEIGHT = metrics.heightPixels;
//        Constants.SCREEN_WIDTH = metrics.widthPixels;
//        mHandler = new Handler(getMainLooper());
//        findViewById();
//        initView();
    }

    @Override
    protected void initView() {
        Animation translate = AnimationUtils.loadAnimation(this,
                R.anim.splash_loading);
        mSplashItem_iv = (ImageView) findViewById(R.id.splash_loading_item);
        translate.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this,
                        LockLoginToHomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                SplashActivity.this.finish();
            }
        });
        if(mSplashItem_iv !=null)
        mSplashItem_iv.setAnimation(translate);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }
}
