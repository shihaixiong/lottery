package com.maxsix.bingo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maxsix.bingo.R;
import com.google.gson.Gson;

import java.util.concurrent.Callable;

public class ModifyPasswordActivity extends com.maxsix.bingo.view.activity.BaseActivity implements View.OnClickListener {

    @com.maxsix.bingo.util.InjectView(R.id.oldPassword)
    private EditText oldPassword;

    @com.maxsix.bingo.util.InjectView(R.id.newPassword)
    private EditText newPassword;

    @com.maxsix.bingo.util.InjectView(R.id.confirm_newpassword)
    private EditText confirm_newpassword;

    @com.maxsix.bingo.util.InjectView(R.id.btnModifyPsw)
    private Button btnModifyPsw;
    private com.maxsix.bingo.dialog.FlippingLoadingDialog loginLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypassword);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        btnModifyPsw.setOnClickListener(this);
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
            case R.id.btnModifyPsw:
                modifyPassword();
                break;
            default:
                break;
        }
    }
    public void modifyPassword(){
        String oldpsw = oldPassword.getText().toString().trim();
        String newpsw = newPassword.getText().toString().trim();
        String confirmPsw = confirm_newpassword.getText().toString().trim();
        if(!oldpsw.equals("") && !newpsw.equals("") && !confirmPsw.equals("") && newpsw.equals(confirmPsw)){
            loginLoadingDialog = getLoadingDialog("正在提交数据……");
            loginLoadingDialog.show();
            getModifySerive(newpsw,oldpsw);
        }else{
            com.maxsix.bingo.util.Utils.showLongToast(ModifyPasswordActivity.this, "请填写密码或是密码不一致!");
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
    private void getModifySerive(final String newpsw,final String oldpsw) {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();

            @Override
            public Boolean call() throws Exception {
                com.maxsix.bingo.vo.Passwordinfo obj = new com.maxsix.bingo.vo.Passwordinfo();
                obj.setPassword(newpsw);
                obj.setOldpassword(oldpsw);
                String resultJson = http.Put(com.maxsix.bingo.config.Constants.User_BaseURL + "0/passwords/", gson.toJson(obj), com.maxsix.bingo.config.Constants.CONTENT_TYPE_JSON);
                com.maxsix.bingo.vo.VDataResult result = gson.fromJson(resultJson, com.maxsix.bingo.vo.VDataResult.class);
                if(result != null && result.getStatus() == 1){
                    return true;
                }else {
                    return false;
                }

            }

        }, new com.maxsix.bingo.task.Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {
                if (pCallbackValue == true) {
                    loginLoadingDialog.dismiss();
                    com.maxsix.bingo.util.Utils.showLongToast(ModifyPasswordActivity.this, "修改成功！，请重新登录");
                    Intent intent = new Intent(ModifyPasswordActivity.this,
                            com.maxsix.bingo.view.activity.TelLoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_up_in,
                            R.anim.push_up_out);
                    finish();
                } else {
                    loginLoadingDialog.dismiss();
                    com.maxsix.bingo.util.Utils.showLongToast(ModifyPasswordActivity.this, "修改失败！");
                }

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                loginLoadingDialog.dismiss();
                com.maxsix.bingo.util.Utils.showLongToast(ModifyPasswordActivity.this, "修改失败!");
            }
        }, false, "");
    }
    @Override
    protected void findViewById() {

    }
}
