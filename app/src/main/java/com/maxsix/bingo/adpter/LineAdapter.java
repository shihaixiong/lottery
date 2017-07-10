package com.maxsix.bingo.adpter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.vo.BettingInfo;

import java.util.List;

/**
 * Created by aspsine on 15/10/11.
 */
public class LineAdapter extends BaseAdapter {

    private List<BettingInfo> lines;

    public LineAdapter(List<BettingInfo> lines) {
        this.lines = lines;
    }

    @Override
    public int getCount() {
        return lines.size();
    }

    @Override
    public BettingInfo getItem(int position) {
        return lines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
            holder.etLine = (EditText) convertView.findViewById(R.id.jine);
            holder.etLine.setHint("请输入投注金额");
            holder.name = (TextView)convertView.findViewById(R.id.letteyName);
            holder.btn_delete = (ImageView)convertView.findViewById(R.id.btn_Delete);
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lines.remove(position);
                    notifyDataSetChanged();
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BettingInfo line = lines.get(position);

        if (holder.etLine.getTag() instanceof TextWatcher) {
            holder.etLine.removeTextChangedListener((TextWatcher) (holder.etLine.getTag()));
        }

        holder.name.setText(line.getName());
        if (TextUtils.isEmpty(line.getTimes()+"")) {
            holder.etLine.setText(" ");
        } else {
            if(line.getTimes() !=0) {
                holder.etLine.setText(line.getTimes() + "");
            }else{
                holder.etLine.setText(" ");
            }
        }
        //holder.name.setText(line.getText());
        if (line.isFocus()) {
            if (!holder.etLine.isFocused()) {
                holder.etLine.requestFocus();
            }
            CharSequence text = line.getTimes()+"";
            holder.etLine.setSelection(TextUtils.isEmpty(text) ? 0 : text.length());
        } else {
            if (holder.etLine.isFocused()) {
                holder.etLine.clearFocus();
            }
        }

        holder.etLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    final boolean focus = line.isFocus();
                    check(position);
                    if (!focus && !holder.etLine.isFocused()) {
                        holder.etLine.requestFocus();
                        holder.etLine.onWindowFocusChanged(true);
                    }
                }
                return false;
            }
        });

        final TextWatcher watcher = new SimpeTextWather() {

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    line.setTimes(0);
                } else {
                    if(!String.valueOf(s).trim().equals("")) {
                        line.setTimes(Integer.parseInt(String.valueOf(s).trim()));
                    }
                }
            }
        };
        holder.etLine.addTextChangedListener(watcher);
        holder.etLine.setTag(watcher);

        return convertView;
    }

    private void check(int position) {
        for (BettingInfo l : lines) {
            l.setFocus(false);
        }
        lines.get(position).setFocus(true);
    }

    static class ViewHolder {
        EditText etLine;
        TextView name;
        ImageView btn_delete;
    }
}
