package com.maxsix.bingo.view.activity;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.util.InjectView;

public class WeixinArtificialActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.btnCopy)
    private Button btnCopy;
    @InjectView(R.id.tv_tximg)
    private TextView tv_tximg;

    private com.maxsix.bingo.vo.User user;
    private com.maxsix.bingo.dialog.FlippingLoadingDialog loginLoadingDialog;
    private String payWay="";
    private int getMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixinartificial);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        btnCopy.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.btnCopy:
                if (getSDKVersionNumber() >= 11) {
                    android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(tv_tximg.getText());
                } else {
                    // 得到剪贴板管理器
                    android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, tv_tximg.getText()));
                }
                com.maxsix.bingo.util.Utils.showLongToast(WeixinArtificialActivity.this, "已复制到粘贴板！");
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
    /**
     * 获取手机操作系统版本
     * @return
     * @author SHANHY
     * @date   2015年12月4日
     */
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }
}
