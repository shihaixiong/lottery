package com.maxsix.bingo.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.maxsix.bingo.R;

public class InvitecodeActivity extends com.maxsix.bingo.view.activity.BaseActivity implements View.OnClickListener {

    @com.maxsix.bingo.util.InjectView(R.id.invitecode)
    private TextView invitecode;
    private com.maxsix.bingo.vo.User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitecode);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        user = com.maxsix.bingo.config.AppManager.getInstance().getUserSession();
        if(user != null){
            invitecode.setText(user.getInvitecode());
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
