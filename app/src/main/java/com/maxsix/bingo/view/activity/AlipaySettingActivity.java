package com.maxsix.bingo.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.google.gson.Gson;
import com.maxsix.bingo.config.Constants;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.vo.DataResult;

import java.util.concurrent.Callable;

public class AlipaySettingActivity extends com.maxsix.bingo.view.activity.BaseActivity implements View.OnClickListener {

    @com.maxsix.bingo.util.InjectView(R.id.alipayAccount)
    private EditText alipayAccount;
    @com.maxsix.bingo.util.InjectView(R.id.btnModifyAliAccount)
    private Button btnModifyAliAccount;
    @InjectView(R.id.bankType)
    private Spinner bankType;
    private com.maxsix.bingo.vo.User user;
    private ArrayAdapter<String> adapter = null;
    private String bankName = "";
    private com.maxsix.bingo.dialog.FlippingLoadingDialog loginLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipaysetting);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        user = com.maxsix.bingo.config.AppManager.getInstance().getUserSession();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Constants.bankTypes);
        //设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        bankType.setAdapter(adapter);
        bankType.setVisibility(View.VISIBLE);//设置默认显示
        bankType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                bankName = ((TextView)arg1).getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        if(user != null){
            if(user.getWeixin() != null) {
                String[] strs = user.getWeixin().split(":");
                if(strs.length ==2){
                    SpinnerAdapter apsAdapter= bankType.getAdapter(); //得到SpinnerAdapter对象
                    int k= apsAdapter.getCount();
                    for(int i=0;i<k;i++){
                        if(strs[0].equals(apsAdapter.getItem(i).toString())){
                            bankType.setSelection(i,true);// 默认选中项
                            break;
                        }
                    }
                    alipayAccount.setText(strs[1]);
                }
            }else {

            }
        }
        btnModifyAliAccount.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnModifyAliAccount:
                modifyAliAccount();
                break;
        }
    }
    public void modifyAliAccount() {
        String aliAccount = alipayAccount.getText().toString().trim();
        if(bankName.equals("")){
            Utils.showLongToast(AlipaySettingActivity.this, "请选择银行卡开户行！");
        }else if(aliAccount == null && aliAccount.equals("")){
            Utils.showLongToast(AlipaySettingActivity.this, "请输入银行卡账号！");
        }else if(!bankName.equals("")&&aliAccount != null && !aliAccount.equals("")){

            loginLoadingDialog = getLoadingDialog("正在提交数据……");
            loginLoadingDialog.show();
            getModifySerive(bankName+":"+aliAccount);
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

    private void getModifySerive(final String aliAccount) {

        doAsync(new Callable<DataResult>() {
            Gson gson = new Gson();

            @Override
            public DataResult call() throws Exception {
                com.maxsix.bingo.vo.ModifyInfo obj = new com.maxsix.bingo.vo.ModifyInfo();
                obj.setWeixin(aliAccount);
                obj.setDeviceid("");
                obj.setAlipay(user.getAlipay());
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
                    com.maxsix.bingo.util.Utils.showLongToast(AlipaySettingActivity.this, "修改成功！");
                    getUserInfo();
                } else {
                    loginLoadingDialog.dismiss();
                    com.maxsix.bingo.util.Utils.showLongToast(AlipaySettingActivity.this, "修改失败！");
                }

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                loginLoadingDialog.dismiss();
                com.maxsix.bingo.util.Utils.showLongToast(AlipaySettingActivity.this, "修改失败!");
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
}
