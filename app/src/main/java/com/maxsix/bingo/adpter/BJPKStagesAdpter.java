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
import com.maxsix.bingo.config.Constants;
import com.maxsix.bingo.config.ViewHolder;
import com.maxsix.bingo.vo.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by shihaixiong on 2016/6/2.
 */
public class BJPKStagesAdpter extends BaseAdapter {
    private List<Stage> mData;
    private View v;
    private Context mContext;
    public BJPKStagesAdpter(Context context, List<Stage> data) {
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
        Stage stage= mData.get(position);
        TextView[] openNumbers = new TextView[10];
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_bjpkcardview, null);
        }
            String[] dates= stage.getEnded().split("T");
            if(dates.length > 0) {
                TextView openTime = ViewHolder.get(convertView,
                        R.id.openTime);
                openTime.setText(dates[0]+" "+dates[1]);
            }
        TextView marksixNumber = ViewHolder.get(convertView,
                R.id.marksixNumber);
        marksixNumber.setText("第" + stage.getCurrent() + "期");
        TextView txt_1 = ViewHolder.get(convertView,
                R.id.txt_1);
        TextView txt_2 = ViewHolder.get(convertView,
                R.id.txt_2);
        TextView txt_3 = ViewHolder.get(convertView,
                R.id.txt_3);
        TextView txt_4 = ViewHolder.get(convertView,
                R.id.txt_4);
        TextView txt_5 = ViewHolder.get(convertView,
                R.id.txt_5);
        TextView txt_6 = ViewHolder.get(convertView,
                R.id.txt_6);
        TextView txt_7 = ViewHolder.get(convertView,
                R.id.txt_7);
        TextView txt_8 = ViewHolder.get(convertView,
                R.id.txt_8);
        TextView txt_9 = ViewHolder.get(convertView,
                R.id.txt_9);
        TextView txt_10 = ViewHolder.get(convertView,
                R.id.txt_10);
        TextView txt_11 = ViewHolder.get(convertView,
                R.id.txt_11);
        TextView txt_12 = ViewHolder.get(convertView,
                R.id.txt_12);
        TextView txt_13 = ViewHolder.get(convertView,
                R.id.txt_13);
        openNumbers[0] = txt_1;
        openNumbers[1] = txt_2;
        openNumbers[2] = txt_3;
        openNumbers[3] = txt_4;
        openNumbers[4] = txt_5;
        openNumbers[5] = txt_6;
        openNumbers[6] = txt_7;
        openNumbers[7] = txt_8;
        openNumbers[8] = txt_9;
        openNumbers[9] = txt_10;
        String[] openNumber = stage.getOpened().split(",");
        Drawable drawable = null;

        if(openNumber.length ==10) {
            int sumgy = Integer.parseInt(openNumber[0])+Integer.parseInt(openNumber[1]);
            txt_11.setText("冠亚和："+sumgy);
            if(sumgy >11){
                txt_12.setText("大");
            }else if (sumgy == 11) {
                txt_12.setText("和");
                txt_12.setTextColor(Color.parseColor("#1f8f16"));
            }else{
                txt_12.setText("小");
            }
            if(sumgy % 2 == 0){
                txt_13.setText("双");
            }else{
                txt_13.setText("单");
            }
            for (int i = 0; i <openNumber.length; i++) {
                int a = Integer.parseInt(openNumber[i]);
                if (a == 1) {
                   drawable = convertView.getResources().getDrawable(R.drawable.one);
                } else if (a == 2) {
                    drawable = convertView.getResources().getDrawable(R.drawable.two);
                } else if (a == 3) {
                    drawable = convertView.getResources().getDrawable(R.drawable.three);
                } else if (a == 4) {
                    drawable = convertView.getResources().getDrawable(R.drawable.four);
                } else if (a == 5) {
                    drawable = convertView.getResources().getDrawable(R.drawable.fine);
                } else if (a == 6) {
                    drawable = convertView.getResources().getDrawable(R.drawable.six);
                } else if (a == 7) {
                    drawable = convertView.getResources().getDrawable(R.drawable.seven);
                } else if (a == 8) {
                    drawable = convertView.getResources().getDrawable(R.drawable.eight);
                } else if (a == 9) {
                    drawable = convertView.getResources().getDrawable(R.drawable.nine);
                } else if (a == 10) {
                    drawable = convertView.getResources().getDrawable(R.drawable.ten);
                }
                openNumbers[i].setBackground(drawable);
            }
        }
        return convertView;
    }
}
