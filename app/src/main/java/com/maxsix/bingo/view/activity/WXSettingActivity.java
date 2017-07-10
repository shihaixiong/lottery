package com.maxsix.bingo.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maxsix.bingo.R;
import com.google.gson.Gson;
import com.maxsix.bingo.vo.DataResult;

import java.util.concurrent.Callable;

public class WXSettingActivity extends com.maxsix.bingo.view.activity.BaseActivity implements View.OnClickListener {

    @com.maxsix.bingo.util.InjectView(R.id.et_wxAccount)
    private EditText et_wxAccount;
    @com.maxsix.bingo.util.InjectView(R.id.btnModifyWXAccount)
    private Button btnModifyWXAccount;
    private com.maxsix.bingo.vo.User user;
    private com.maxsix.bingo.dialog.FlippingLoadingDialog loginLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixinpaysetting);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        user = com.maxsix.bingo.config.AppManager.getInstance().getUserSession();
        if(user != null){
            et_wxAccount.setText(user.getAlipay());
        }
        btnModifyWXAccount.setOnClickListener(this);
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
            case R.id.btnModifyWXAccount:
                modifyAliAccount();
                break;
        }
    }
    public void modifyAliAccount() {
        String wxAccount = et_wxAccount.getText().toString().trim();
        if(wxAccount !=null && !wxAccount.equals("")){
            loginLoadingDialog = getLoadingDialog("正在提交数据……");
            loginLoadingDialog.show();
            getModifySerive(wxAccount);
        }else
        {
            com.maxsix.bingo.util.Utils.showLongToast(WXSettingActivity.this, "请填写支付宝账户！");
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
    private void getModifySerive(final String _wxAccount) {

        doAsync(new Callable<DataResult>() {
            Gson gson = new Gson();

            @Override
            public DataResult call() throws Exception {
                com.maxsix.bingo.vo.ModifyInfo obj = new com.maxsix.bingo.vo.ModifyInfo();
                obj.setAlipay(_wxAccount);
                obj.setDeviceid("");
                obj.setWeixin(user.getWeixin());
                obj.setName(user.getName());
                obj.setTel(user.getTel());
                String resultJson = http.Put(com.maxsix.bingo.config.Constants.User_BaseURL + "0/", gson.toJson(obj), com.maxsix.bingo.config.Constants.CONTENT_TYPE_JSON);
                com.maxsix.bingo.vo.DataResult result = gson.fromJson(resultJson, com.maxsix.bingo.vo.DataResult.class);
                return result;
            }

        }, new com.maxsix.bingo.task.Callback<DataResult>() {
            @Override
            public void onCallback(DataResult pCallbackValue) {
                if (pCallbackValue != null && pCallbackValue.getId() > 0 ) {
                    loginLoadingDialog.dismiss();
                    com.maxsix.bingo.util.Utils.showLongToast(WXSettingActivity.this, "修改成功！");
                    getUserInfo();
                } else {
                    loginLoadingDialog.dismiss();
                    com.maxsix.bingo.util.Utils.showLongToast(WXSettingActivity.this, "修改失败！");
                }

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                loginLoadingDialog.dismiss();
                com.maxsix.bingo.util.Utils.showLongToast(WXSettingActivity.this, "修改失败!");
            }
        }, false, "");
    }
    private void getUserInfo() {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();
            @Override
            public Boolean call() throws Exception {

                String usrInfo = http.Get(com.maxsix.bingo.config.Constants.User_BaseURL + "0/?"+System.currentTimeMillis());
                com.maxsix.bingo.vo.User user = gson.fromJson(usrInfo, com.maxsix.bingo.vo.User.class);
                if(user !=null){
                    com.maxsix.bingo.config.AppManager.getInstance().setUserSession(user);
                    return true;
                }else{
                    return false;
                }

            }

        }, new com.maxsix.bingo.task.Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
            }
        }, false,"");

    }
    @Override
    protected void findViewById() {

    }
}
