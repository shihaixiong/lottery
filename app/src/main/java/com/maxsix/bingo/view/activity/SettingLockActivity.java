package com.maxsix.bingo.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxsix.bingo.R;
import com.maxsix.bingo.view.LockPatternView;
import com.maxsix.bingo.vo.DataResult;
import com.maxsix.bingo.widget.UnlockAppView;

import java.util.concurrent.Callable;

public class SettingLockActivity extends BaseActivity implements UnlockAppView.OnPatternChangeListener{
    private TextView mLockPatternHint;
    private UnlockAppView mLockPatternView;
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private String lockPassword;
    private int count = 0;
    private String letteyType;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_login);
        title = (TextView)findViewById(R.id.title);
        title.setText("设置新的手势密码");
        mLockPatternHint = (TextView) findViewById(R.id.lock_pattern_hint);
        mLockPatternView = (UnlockAppView) findViewById(R.id.lock_pattern_view);
        mLockPatternView.setOnPatternChangeListener(this);
        letteyType = getIntent().getStringExtra("letteyType");
        sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        ed = sp.edit();
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
        if (patternPassword.length()<5) {
            mLockPatternHint.setText("至少5个点");
        } else {
            count++;
            if(count == 2){
                if(lockPassword.equals( patternPassword)){
                    getModifySerive(patternPassword);
                }else {
                    Toast.makeText(SettingLockActivity.this, "绘制图案不一致，请重新绘制",
                            Toast.LENGTH_SHORT).show();
                    count = 0;
                }
                finish();
            }else{
                lockPassword = patternPassword;
                Toast.makeText(SettingLockActivity.this, "请再绘制一遍",
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
                DataResult result = gson.fromJson(resultJson, DataResult.class);
                return result;
            }

        }, new com.maxsix.bingo.task.Callback<DataResult>() {
            @Override
            public void onCallback(DataResult pCallbackValue) {
                if (pCallbackValue != null && pCallbackValue.getId() > 0 ) {
                    Toast.makeText(SettingLockActivity.this, "绘制成功！",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingLockActivity.this,
                            LockLoginToHomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SettingLockActivity.this, "绘制失败！",
                            Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                Toast.makeText(SettingLockActivity.this, "绘制失败！",
                        Toast.LENGTH_SHORT).show();
            }
        }, false, "");
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
