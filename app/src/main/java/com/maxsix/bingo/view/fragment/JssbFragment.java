/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maxsix.bingo.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.view.activity.CheckActivity;
import com.maxsix.bingo.view.activity.JSSBActivity;
import com.maxsix.bingo.vo.BettingInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class JssbFragment extends BaseFragment implements View.OnClickListener{


    private Context context;
    private int[] relativeLayoutIds = new int[46];
    private RelativeLayout[] relativeLayouts = new RelativeLayout[46];
    private TextView[] txts = new TextView[46];
    private TextView[] tvs = new TextView[46];
    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
//    private ArrayList<BettingInfo> infos = new ArrayList<BettingInfo>();
    private View rv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = inflater.inflate(
                R.layout.fragment_jssb, container, false);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this, rv);
        context = rv.getContext();
        initView(rv);
        infos.clear();
        return rv;
    }
    public void initView(View _rv) {
        Resources resources = context.getResources();

        for(int i=0;i< relativeLayoutIds.length;i++){
            int k = i+1;
            String rl = "rl_"+k;
            String txt = "txt_"+k;
            String tv = "tv_"+k;
            relativeLayoutIds[i]  = resources.getIdentifier(rl, "id", context.getPackageName());
            relativeLayouts[i] = (RelativeLayout)_rv.findViewById(relativeLayoutIds[i]);
            relativeLayouts[i].setOnClickListener(new LinearLayoutClickListener());

            txts[i] = (TextView)_rv.findViewById( resources.getIdentifier(txt, "id", context.getPackageName()));
            tvs[i] = (TextView)_rv.findViewById( resources.getIdentifier(tv, "id", context.getPackageName()));
        }
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void resetValue() {
        infos.clear();
        for(int i=0;i<relativeLayoutIds.length;i++) {
            Resources resources = getContext().getResources();
            Drawable checkDrawable = resources.getDrawable(R.drawable.uncheck_bg);
            relativeLayouts[i].setBackground(checkDrawable);
            tvs[i].setTextColor(Color.parseColor("#ffffff"));
            txts[i].setTextColor(Color.parseColor("#ffffff"));
        }
    }

    class LinearLayoutClickListener implements View.OnClickListener {
        private int number =0;
        @Override
        public void onClick(final View v) {
            JSSBActivity activity = (JSSBActivity)getActivity();
            if(activity.currentStage ==null || activity.currentStage.getStatus() !=0){
                return;
            }
            for (int k = 0; k < 46; k++) {
                if (relativeLayoutIds[k] == v.getId()) {
                    RelativeLayout rl =  relativeLayouts[k];
                    Resources resources = getContext().getResources();
                    Drawable checkDrawable = resources.getDrawable(R.drawable.check_bg);
                    Drawable.ConstantState icon = checkDrawable.getConstantState();
                    Drawable.ConstantState icon1 = rl.getBackground().getConstantState();
                    TextView txt = (TextView)txts[k];
                    TextView tv = (TextView)tvs[k];
                    String codeStr = "";
                    String selectName = "";
                    if(k < 15) {
                        codeStr = "N-" + txt.getText().toString()+"-0";
                        selectName = "点数和";
                    }else if(k ==16){
                        codeStr = "ZHD-0-0";
                        selectName = "点数大小";
                    }else if(k ==17){
                        codeStr = "ZHX-0-0";
                        selectName = "点数大小";
                    } else if(k >17 && k<24){
                        codeStr = "STH-" + txt.getText().toString().substring(0,1)+"-0";
                        selectName = "三同号";
                    }else if(k==24){
                        codeStr = "STH-TX-0";
                        selectName = "三同号通选";
                    }else if(k >24 && k<40){
                        codeStr = "NN-"+txt.getText().toString().toString().substring(0,1)+"-"+txt.getText().toString().toString().substring(1,2);
                        selectName = "二不同号";
                    }else if(k>39){
                        codeStr = "NN-0-"+txt.getText().toString().substring(0,1);
                        selectName = "二同号";
                    }
                    if(icon.equals(icon1)){
                        checkDrawable = resources.getDrawable(R.drawable.uncheck_bg);
                        tv.setTextColor(Color.parseColor("#ffffff"));
                        txt.setTextColor(Color.parseColor("#ffffff"));
                        BettingInfo info = null;
                        for( BettingInfo tempinfo:infos){
                            if(tempinfo.getCode().equals(codeStr)){
                                info = tempinfo;
                            }
                        }
                        infos.remove(info);
                        selectNumberValue = selectNumberValue-1;
                        activity.setNumber(selectNumberValue);
                    }else{
                        tv.setTextColor(Color.parseColor("#ffff7d27"));
                        txt.setTextColor(Color.parseColor("#ffff7d27"));
                        BettingInfo info = new BettingInfo();
                        info.setCode(codeStr);
                        info.setStage(activity.currentStage.getId());
                        info.setTimes(number);
                        info.setName(selectName+"@"+txt.getText());
                        infos.add(info);
                        selectNumberValue = selectNumberValue+1;
                        activity.setNumber(selectNumberValue);
                    }
                    rl.setBackground(checkDrawable);
                }
            }
        }
    };
}
