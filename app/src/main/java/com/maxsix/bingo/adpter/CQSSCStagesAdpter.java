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
public class CQSSCStagesAdpter extends BaseAdapter {
    private List<Stage> mData;
    private Context mContext;
    public CQSSCStagesAdpter(Context context, List<Stage> data) {
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
        Integer gewei = -1;
        Integer shiwei = -1;
        Integer baiwei = -1;
        Integer qianwei = -1;
        Integer wanwei = -1;
        Integer sum = 0;
        String lh = "";
        String danshuang = "";
        String daxiao = "";
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_ssccardview, null);
        }
        TextView sscNumber = ViewHolder.get(convertView,
                R.id.sscNumber);
        sscNumber.setText("第" + stage.getCurrent() + "期");
        if (stage != null) {
            String str = stage.getOpened();
            for (int i = 0; i < str.length(); i++) {
                int m = Integer.parseInt(str.charAt(i) + "");
                if (i == 0) {
                    wanwei = m;

                } else if (i == 1) {
                    qianwei = m;

                } else if (i == 2) {
                    baiwei = m;

                } else if (i == 3) {
                    shiwei = m;

                } else if (i == 4) {
                    gewei = m;

                }
            }
            sum = gewei + shiwei + baiwei + qianwei + wanwei;

            if (sum >= 23) {
                daxiao = "大";
            } else {
                daxiao = "小";
            }
            if (sum % 2 == 0) {
                danshuang = "双";
            } else {
                danshuang = "单";
            }
            if (wanwei > gewei) {
                lh = "龙";
            } else if (wanwei < gewei) {
                lh = "虎";
            } else if (wanwei == gewei) {
                lh = "和";
            }
            TextView txt_wan = ViewHolder.get(convertView,
                    R.id.txt_wan);
            txt_wan.setText(wanwei.toString());

            TextView txt_qian = ViewHolder.get(convertView,
                    R.id.txt_qian);
            txt_qian.setText(qianwei.toString());

            TextView txt_bai = ViewHolder.get(convertView,
                    R.id.txt_bai);
            txt_bai.setText(baiwei.toString());

            TextView txt_shi = ViewHolder.get(convertView,
                    R.id.txt_shi);
            txt_shi.setText(shiwei.toString());

            TextView txt_ge = ViewHolder.get(convertView,
                    R.id.txt_ge);
            txt_ge.setText(gewei.toString());

            TextView txt_sum = ViewHolder.get(convertView,
                    R.id.txt_sum);
            txt_sum.setText(sum.toString());

            TextView txt_dx = ViewHolder.get(convertView,
                    R.id.txt_dx);
            txt_dx.setText(daxiao);

            TextView txt_ds = ViewHolder.get(convertView,
                    R.id.txt_ds);
            txt_ds.setText(danshuang);

            TextView txt_lh = ViewHolder.get(convertView,
                    R.id.txt_lh);
            txt_lh.setText(lh);
        }
        return convertView;
    }
}
