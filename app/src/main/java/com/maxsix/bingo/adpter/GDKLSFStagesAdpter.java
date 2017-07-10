package com.maxsix.bingo.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.config.ViewHolder;
import com.maxsix.bingo.vo.Stage;

import java.util.List;

/**
 * Created by shihaixiong on 2016/6/2.
 */
public class GDKLSFStagesAdpter extends BaseAdapter {
    private List<Stage> mData;
    private Context mContext;
    public GDKLSFStagesAdpter(Context context, List<Stage> data) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_gdklsfcardview, null);
        }
        TextView sscNumber = ViewHolder.get(convertView,
                R.id.sscNumber);
        sscNumber.setText("第" + stage.getCurrent() + "期");
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
        if (stage != null) {
            String str = stage.getOpened();
            String[] opens = str.split(",");
            for (int i = 0; i <opens.length; i++) {
                if(i==0){
                    txt_1.setText(opens[i]);
                }else if(i==1){
                    txt_2.setText(opens[i]);
                }else if(i==2){
                    txt_3.setText(opens[i]);
                }else if(i==3){
                    txt_4.setText(opens[i]);
                }else if(i==4){
                    txt_5.setText(opens[i]);
                }else if(i==5){
                    txt_6.setText(opens[i]);
                }else if(i==6){
                    txt_7.setText(opens[i]);
                }else if(i==7){
                    txt_8.setText(opens[i]);
                }
            }
        }
        return convertView;
    }
}
