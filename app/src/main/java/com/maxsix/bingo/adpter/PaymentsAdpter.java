package com.maxsix.bingo.adpter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maxsix.bingo.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by shihaixiong on 2016/6/2.
 */
public class PaymentsAdpter extends BaseAdapter {
    private List<com.maxsix.bingo.vo.PaymentListInfo> mData;
    private View v;
    private Context mContext;
    public PaymentsAdpter(Context context, List<com.maxsix.bingo.vo.PaymentListInfo> data) {
        this.mContext = context;
        this.mData = data;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        com.maxsix.bingo.vo.PaymentListInfo info= mData.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.lotterypayment_cardview, null);
        }
        TextView openTime = com.maxsix.bingo.config.ViewHolder.get(convertView,
                R.id.cztime);
        openTime.setText(info.getCreated().replace("T"," ").substring(0,info.getCreated().length()-7));

        TextView status = com.maxsix.bingo.config.ViewHolder.get(convertView,
                R.id.status);
        if(info.getStatus() == 0){
            status.setText("等待处理");
        }else if(info.getStatus() == 1) {
            status.setText("已处理");
        }else if(info.getStatus() == 2){
                status.setText("支付中");
        }else if(info.getStatus() == 3){
            status.setText("已驳回");
        }
        TextView castPay = com.maxsix.bingo.config.ViewHolder.get(convertView,
                R.id.castPay);
        TextView beizhu = com.maxsix.bingo.config.ViewHolder.get(convertView,
                R.id.beizhu);
        TextView payway = com.maxsix.bingo.config.ViewHolder.get(convertView,
                R.id.payway);
        beizhu.setText(info.getRemark());
        DecimalFormat df = new DecimalFormat("#.##");
        double money = Double.parseDouble(df.format(info.getMoney() / 100.00));
        if(info.getMethod() == 1){
            castPay.setText(money+"");
            castPay.setTextColor(Color.RED);
            payway.setText("充值");
        }else if(info.getMethod() == 2){
            castPay.setText("-"+money+"");
            castPay.setTextColor(Color.GREEN);
            payway.setText("取现");
        }else if(info.getMethod() == 3){
            castPay.setText(money+"");
            castPay.setTextColor(Color.RED);
            payway.setText("佣金");
        }else if(info.getMethod() == 4){
            castPay.setText("-" + money + "");
            castPay.setTextColor(Color.GREEN);
            payway.setText("投注");
        }else if(info.getMethod() == 5){
            castPay.setText(money+"");
            castPay.setTextColor(Color.GREEN);
            payway.setText("中奖");
        }
        return convertView;
    }
}
