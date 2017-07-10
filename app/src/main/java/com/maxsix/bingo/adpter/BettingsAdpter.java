package com.maxsix.bingo.adpter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
public class BettingsAdpter extends BaseAdapter {
    private List<com.maxsix.bingo.vo.BettingListInfo> mData;
    private View v;
    private Context mContext;
    public BettingsAdpter(Context context, List<com.maxsix.bingo.vo.BettingListInfo> data) {
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
        com.maxsix.bingo.vo.BettingListInfo info= mData.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.lotterybetting_cardview, null);
        }
        TextView kaijiangshijian = com.maxsix.bingo.config.ViewHolder.get(convertView,
                        R.id.kaijiangshijian);
        TextView openTime = com.maxsix.bingo.config.ViewHolder.get(convertView,
                R.id.tzTime);
        openTime.setText("投注时间："+info.getCreated().replace("T"," "));
        TextView qishu = com.maxsix.bingo.config.ViewHolder.get(convertView,
                R.id.qishu);
        qishu.setText("第" + info.getStage().getCurrent() + "期");
        TextView bettingMoney = com.maxsix.bingo.config.ViewHolder.get(convertView,
                R.id.bettingMoney);
        if(info.getStatus() == 0){
            kaijiangshijian.setText("等待开奖");
            bettingMoney.setTextColor(Color.parseColor("#a3e9a4"));
        }else if(info.getStatus() ==1){
            double sumwind = info.getTimes()*info.getOdds();
            DecimalFormat df = new DecimalFormat("#.##");
            kaijiangshijian.setText("￥"+Double.parseDouble(df.format(sumwind)));
//            kaijiangshijian.setTextColor(Color.parseColor("#1f8f16"));
//            bettingMoney.setTextColor(Color.parseColor("#a3e9a4"));
        }else if(info.getStatus() ==2) {
            kaijiangshijian.setText("0.00");
//            bettingMoney.setTextColor(Color.RED);
        }
//        }else if(info.getStatus() ==3){
//            kaijiangshijian.setText();
//        }
        TextView jiaoyNumber = com.maxsix.bingo.config.ViewHolder.get(convertView,
                R.id.jiaoyNumber);
        String tempValue = info.getName();
        jiaoyNumber.setText(tempValue);
        bettingMoney.setText("¥" + info.getTimes());
        return convertView;
    }
}
