package com.maxsix.bingo.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.config.Constants;
import com.maxsix.bingo.config.ViewHolder;
import com.maxsix.bingo.view.activity.MarxSixActivity;
import com.maxsix.bingo.vo.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by shihaixiong on 2016/6/2.
 */
public class MarxSixStagesAdpter extends BaseAdapter {
    private List<Stage> mData;
    private View v;
    private Context mContext;
    public MarxSixStagesAdpter(Context context, List<Stage> data) {
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
        TextView[] openNumbers = new TextView[7];
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_cardview, null);
        }
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy年MM月dd日 E");
            String[] dates= stage.getEnded().split("T");
            if(dates.length > 0) {
                String dateStr = dates[0]+" 21:33:00";
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = sdf.parse(dateStr);
                    TextView openTime = ViewHolder.get(convertView,
                            R.id.openTime);
                    openTime.setText(dateformat.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
        TextView txt_14 = ViewHolder.get(convertView,
                R.id.txt_14);
        TextView txt_15 = ViewHolder.get(convertView,
                R.id.txt_15);
        openNumbers[0] = txt_1;
        openNumbers[1] = txt_2;
        openNumbers[2] = txt_3;
        openNumbers[3] = txt_4;
        openNumbers[4] = txt_5;
        openNumbers[5] = txt_6;
        openNumbers[6] = txt_7;
        TextView[] sx = new TextView[7];
        sx[0] = txt_8;
        sx[1] = txt_9;
        sx[2] = txt_10;
        sx[3] = txt_11;
        sx[4] = txt_12;
        sx[5] = txt_13;
        sx[6] = txt_14;
        String[] openNumber = stage.getOpened().split(",");
        int sumopen=0;
        String dx = "";
        if(openNumber.length ==7) {
            for (int a = 0; a < 7; a++) {
                TextView tv = openNumbers[a];
                int number = Integer.parseInt(openNumber[a]);
                sumopen +=number;
                for(int i=0;i<12;i++){
                    if(Constants.sx[i].contains(openNumber[a])){
                        sx[a].setText(Constants.sxstr[i]);
                    }
                }
                tv.setText(openNumber[a]);
                if (Constants.redStr.contains(openNumber[a])) {
                    Drawable drawable = convertView.getResources().getDrawable(R.drawable.red);
                    tv.setBackground(drawable);
                } else if (Constants.blueStr.contains(openNumber[a])) {
                    Drawable drawable = convertView.getResources().getDrawable(R.drawable.blue);
                    tv.setBackground(drawable);
                } else if (Constants.greenStr.contains(openNumber[a])) {
                    Drawable drawable = convertView.getResources().getDrawable(R.drawable.green);
                    tv.setBackground(drawable);
                }
            }
            if(openNumber[6].length() ==2) {
                int shi = Integer.parseInt(openNumber[6].charAt(0) + "");
                int ge = Integer.parseInt(openNumber[6].charAt(1) + "");
                int he = shi+ge;
                dx +=sumopen+"|";
                if(Integer.parseInt(openNumber[6])%2==0){
                    dx+="双|";
                }else if(Integer.parseInt(openNumber[6]) == 49){
                    dx+="和|";
                }else{
                    dx+="单|";
                }
                if(Integer.parseInt(openNumber[6]) >24 && Integer.parseInt(openNumber[6]) <49){
                    dx+="大|";
                }else if(Integer.parseInt(openNumber[6]) ==49){
                    dx+="和|";
                }else{
                    dx+="小|";
                }
                if(he%2==0){
                    dx+="合双|";
                }else if(Integer.parseInt(openNumber[6]) ==49){
                    dx+="和|";
                }else{
                    dx+="合单|";
                }
                if(sumopen%2 == 0){
                    dx+="总双|";
                }else{
                    dx+="总单|";
                }
                if(sumopen  > 174){
                    dx+="总大";
                }else {
                    dx+="总小";
                }
                txt_15.setText(dx);
            }
        }
        return convertView;
    }
}
