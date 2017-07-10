package com.maxsix.bingo.adpter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.config.ViewHolder;
import com.maxsix.bingo.vo.BettingInfo;

import java.util.HashMap;
import java.util.List;

public class BettingscheckAdpter extends BaseAdapter {
        private List<BettingInfo> mData;
        public BettingscheckAdpter(List<BettingInfo> lines) {
                this.mData = lines;
        }

        @Override
        public int getCount() {
                return mData.size();
        }

        @Override
        public BettingInfo getItem(int position) {
                return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
                return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
                if (convertView == null) {
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
                        //convertView.setTag(holder);
                }
                TextView jiaoyNumber = (TextView)convertView.findViewById(R.id.letteyName);
                final EditText jine = (EditText)convertView.findViewById(R.id.jine);
                final BettingInfo info = mData.get(position);

                if (jine.getTag() instanceof TextWatcher) {
                        jine.removeTextChangedListener((TextWatcher) (jine.getTag()));
                }

                //jine.setHint(position + ".");

                if (TextUtils.isEmpty(jine.getText())) {
                        jine.setText("");
                } else {
                        jine.setText(jine.getText());
                }

                if (info.isFocus()) {
                        if (!jine.isFocused()) {
                                jine.requestFocus();
                        }
                        CharSequence text = jine.getText();
                        jine.setSelection(TextUtils.isEmpty(text) ? 0 : text.length());
                } else {
                        if (jine.isFocused()) {
                                jine.clearFocus();
                        }
                }

               jine.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(final View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_UP) {
                                        final boolean focus = info.isFocus();
                                        check(position);
                                        if (!focus && !jine.isFocused()) {
                                                jine.requestFocus();
                                                jine.onWindowFocusChanged(true);
                                        }
                                }
                                return false;
                        }
                });

                final TextWatcher watcher = new SimpeTextWather() {

                        @Override
                        public void afterTextChanged(Editable s) {
                                if (TextUtils.isEmpty(s)) {
                                        //jine.setText(null);
                                } else {
                                        //jine.setText(s.toString());
                                        info.setTimes(Integer.parseInt(s.toString()));
                                }
                        }
                };
                jine.addTextChangedListener(watcher);
                jine.setTag(watcher);
                jiaoyNumber.setText(mData.get(position).getName());
                return convertView;
        }

        private void check(int position) {
                for (BettingInfo l : mData) {
                        l.setFocus(false);
                }
                mData.get(position).setFocus(true);
        }
}