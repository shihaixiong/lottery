package com.maxsix.bingo.adpter;

import android.content.Context;
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
import java.util.UnknownFormatConversionException;

/**
 * Created by shihaixiong on 2016/6/2.
 */
public class JSSBStagesAdpter extends BaseAdapter {
    private List<Stage> mData;
    private View v;
    private Context mContext;
    public JSSBStagesAdpter(Context context, List<Stage> data) {
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
        Integer baiwei = -1;
        Integer qianwei = -1;
        Integer wanwei = -1;
        Stage stage= mData.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.itemjssb_cardview, null);
        }
            String[] dates= stage.getEnded().split("T");
            if(dates.length > 1) {
                String dateStr = dates[0]+" "+dates[1];
                TextView openTime = ViewHolder.get(convertView,
                        R.id.openTime);
                openTime.setText(dateStr);
            }
        TextView marksixNumber = ViewHolder.get(convertView,
                R.id.marksixNumber);
        marksixNumber.setText("第" + stage.getCurrent() + "期");
        TextView txt_1 = ViewHolder.get(convertView,
                R.id.txtfirst);
        TextView txt_2 = ViewHolder.get(convertView,
                R.id.txtsecond);
        TextView txt_3 = ViewHolder.get(convertView,
                R.id.txtthree);
        String open = stage.getOpened();
        String imgName = "";
        int resId = 0;
        for (int i = 0; i < open.length(); i++) {
            int m = Integer.parseInt(open.charAt(i) + "");
            imgName ="ico_dice"+m;
            try {
                resId = (Integer) R.drawable.class.getField(imgName).get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            Drawable nav_up=convertView.getResources().getDrawable(resId);
            if(i == 0){
                txt_1.setBackground(nav_up);
            }else if(i==1){
                txt_2.setBackground(nav_up);
            }else if(i == 2){
                txt_3.setBackground(nav_up);
            }
        }

        return convertView;
    }
}
