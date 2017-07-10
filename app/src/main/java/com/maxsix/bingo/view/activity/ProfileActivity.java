package com.maxsix.bingo.view.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.config.AppManager;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.vo.User;

import java.util.PriorityQueue;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    @com.maxsix.bingo.util.InjectView(R.id.txt_realName)
    private TextView txt_realName;
    @com.maxsix.bingo.util.InjectView(R.id.txt_ali)
    private TextView txt_ali;
    @com.maxsix.bingo.util.InjectView(R.id.txt_wx)
    private TextView txt_wx;
    @com.maxsix.bingo.util.InjectView(R.id.txt_invitecode)
    private TextView txt_invitecode;
    @com.maxsix.bingo.util.InjectView(R.id.txt_modifyPassword)
    private TextView txt_modifyPassword;
    @com.maxsix.bingo.util.InjectView(R.id.btnexit)
    private Button btnexit;
    @InjectView(R.id.txt_version)
    private TextView txt_version;
    @InjectView(R.id.txt_phone)
    private  TextView txt_phone;
    @InjectView(R.id.rl_phone)
    private RelativeLayout rl_phone;
    @InjectView(R.id.txt_lock)
    private TextView txt_lock;

    User user = AppManager.getInstance().getUserSession();
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        txt_realName.setOnClickListener(this);
        txt_lock.setOnClickListener(this);
        txt_ali.setOnClickListener(this);
        txt_wx.setOnClickListener(this);
        txt_invitecode.setOnClickListener(this);
        txt_modifyPassword.setOnClickListener(this);
        btnexit.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        if(user != null){
            txt_phone.setText(user.getTel());
        }
        txt_version.setText(AppManager.getInstance().getVersion());
        sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        ed = sp.edit();
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
            case R.id.txt_realName:
                com.maxsix.bingo.util.Utils.start_Activity(this, RealNameSettingActivity.class);
                break;
            case R.id.txt_ali:
                com.maxsix.bingo.util.Utils.start_Activity(this, AlipaySettingActivity.class);
                break;
            case R.id.txt_wx:
                com.maxsix.bingo.util.Utils.start_Activity(this, WXSettingActivity.class);
                break;
            case R.id.txt_invitecode:
                com.maxsix.bingo.util.Utils.start_Activity(this, InvitecodeActivity.class);
                break;
            case R.id.txt_modifyPassword:
                com.maxsix.bingo.util.Utils.start_Activity(this, com.maxsix.bingo.view.activity.ModifyPasswordActivity.class);
                break;
            case R.id.rl_phone:
                com.maxsix.bingo.util.Utils.start_Activity(this, TelSettingActivity.class);
                break;
            case R.id.btnexit:
                com.maxsix.bingo.config.AppManager.getInstance().AppExit(this.context);
                ed.remove("username");
                ed.remove("password");
                ed.commit();
                if(user != null){
                    AppManager.getInstance().setUserSession(null);
                }
                com.maxsix.bingo.util.Utils.start_Activity(this, HomeActivity.class);
                break;
            case R.id.txt_lock:
                com.maxsix.bingo.util.Utils.start_Activity(this, SettingLockActivity.class);
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
