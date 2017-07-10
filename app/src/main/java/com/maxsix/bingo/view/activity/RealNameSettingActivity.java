package com.maxsix.bingo.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maxsix.bingo.R;
import com.google.gson.Gson;
import com.maxsix.bingo.vo.DataResult;

import java.util.concurrent.Callable;

public class RealNameSettingActivity extends com.maxsix.bingo.view.activity.BaseActivity implements View.OnClickListener {

    @com.maxsix.bingo.util.InjectView(R.id.et_realNme)
    private EditText et_realNme;
    @com.maxsix.bingo.util.InjectView(R.id.btnModifyName)
    private Button btnModifyName;
    private com.maxsix.bingo.vo.User user;
    private com.maxsix.bingo.dialog.FlippingLoadingDialog loginLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realnamesetting);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        user = com.maxsix.bingo.config.AppManager.getInstance().getUserSession();
        if(user != null){
            et_realNme.setText(user.getName());
        }
        btnModifyName.setOnClickListener(this);
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
            case R.id.btnModifyName:
                modifyAliAccount();
                break;
        }
    }
    public void modifyAliAccount() {
        String realName = et_realNme.getText().toString().trim();
        if(realName !=null && !realName.equals("")){
            loginLoadingDialog = getLoadingDialog("正在提交数据……");
            loginLoadingDialog.show();
            getModifySerive(realName);
        }else
        {
            com.maxsix.bingo.util.Utils.showLongToast(RealNameSettingActivity.this, "请填写真实姓名！");
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
    private void getModifySerive(final String realName) {

        doAsync(new Callable<DataResult>() {
            Gson gson = new Gson();

            @Override
            public DataResult call() throws Exception {
                com.maxsix.bingo.vo.ModifyInfo obj = new com.maxsix.bingo.vo.ModifyInfo();
                obj.setAlipay(user.getAlipay());
                obj.setDeviceid("");
                obj.setName(realName);
                obj.setWeixin(user.getWeixin());
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
                    com.maxsix.bingo.util.Utils.showLongToast(RealNameSettingActivity.this, "修改成功！");
                    getUserInfo();
                } else {
                    loginLoadingDialog.dismiss();
                    com.maxsix.bingo.util.Utils.showLongToast(RealNameSettingActivity.this, "修改失败！");
                }

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                loginLoadingDialog.dismiss();
                com.maxsix.bingo.util.Utils.showLongToast(RealNameSettingActivity.this, "修改失败!");
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
