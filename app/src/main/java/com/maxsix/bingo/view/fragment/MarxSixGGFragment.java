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
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.view.activity.CheckActivity;
import com.maxsix.bingo.view.activity.CqsscActivity;
import com.maxsix.bingo.view.activity.MarxSixActivity;
import com.maxsix.bingo.vo.BettingInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MarxSixGGFragment extends BaseFragment implements View.OnClickListener{

    private RelativeLayout[] ggs= new RelativeLayout[42];
    private int selectBtnIndex = 1;
    private Context context;
    private int[] ggIds = new int[42];
    private TextView[] txts = new TextView[42];
    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
    private List<BettingInfo> infos = new ArrayList<BettingInfo>();
    private int sumTZ = 0;
    private View rv;
    private String[] codevalues = {"DAN","S","D","X","HB","LB","LVB",
            "DAN","S","D","X","HB","LB","LVB",
            "DAN","S","D","X","HB","LB","LVB",
            "DAN","S","D","X","HB","LB","LVB",
            "DAN","S","D","X","HB","LB","LVB",
            "DAN","S","D","X","HB","LB","LVB",
            "DAN","S","D","X","HB","LB","LVB"};
    private String[] names = {"单","双","大","小","红波","蓝波","绿波",
            "单","双","大","小","红波","蓝波","绿波",
            "单","双","大","小","红波","蓝波","绿波",
            "单","双","大","小","红波","蓝波","绿波",
            "单","双","大","小","红波","蓝波","绿波",
            "单","双","大","小","红波","蓝波","绿波",
            "单","双","大","小","红波","蓝波","绿波"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = inflater.inflate(
                R.layout.fragment_marxsixgg, container, false);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this, rv);
        context = rv.getContext();
        initView(rv);
        infos.clear();
        return rv;
    }
    public void initView(View _rv) {
        Resources resources = context.getResources();
        for (int k =1;k<43;k++){
            String key = "tm_"+k;
            String txtKey = "txt_"+k;
            ggIds[k-1] = resources.getIdentifier(key, "id", context.getPackageName());
            ggs[k-1] = (RelativeLayout)_rv.findViewById(ggIds[k-1]);
            ggs[k-1].setOnClickListener(new ImgClickListener());
            txts[k-1] = (TextView)_rv.findViewById(resources.getIdentifier(txtKey, "id", context.getPackageName()));
        }

    }
    @Override
    public void onClick(View v) {

    }
public void tz(){
    if(infos.size() == 0 && sumTZ ==0){
        com.maxsix.bingo.util.Utils.showLongToast(this.getActivity(), "请选择投注项！");
    }else
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("infos", (Serializable) infos);
        bundle.putString("title","六合彩");
        bundle.putString("stageName","过关");
        Intent intent = new Intent(context, CheckActivity.class);
        MarxSixActivity activity = (MarxSixActivity)getActivity();
        bundle.putSerializable("stage", activity.currentStage);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
    @Override
    public void resetValue() {
        infos.clear();
        for(RelativeLayout ll :ggs) {
            Resources resources = getContext().getResources();
            Drawable checkDrawable = resources.getDrawable(R.drawable.uncheck_bg);
            ll.setBackground(checkDrawable);
        }
    }

    class ImgClickListener implements View.OnClickListener {
        private int number =0;
        @Override
        public void onClick(final View v) {
            MarxSixActivity activity = (MarxSixActivity)getActivity();
            if(activity.currentStage ==null || activity.currentStage.getStatus() !=0){
                return;
            }
            for (int k = 0; k < 42; k++) {
                if (ggIds[k] == v.getId()) {
                    RelativeLayout rl =  ggs[k];
                    Resources resources = getContext().getResources();
                    Drawable checkDrawable = resources.getDrawable(R.drawable.check_bg);
                    Drawable.ConstantState icon = checkDrawable.getConstantState();
                    Drawable.ConstantState icon1 = rl.getBackground().getConstantState();
                    String code = codevalues[k];
                    String codeStr ="";
                    if(k<7){
                        selectBtnIndex = 1;
                    }else if(k >=7 && k<14){
                        selectBtnIndex = 2;
                    }else if(k >=14 && k<21){
                        selectBtnIndex = 3;
                    }else if(k >=21 && k<28){
                        selectBtnIndex = 4;
                    }else if(k >=28 && k<35){
                        selectBtnIndex = 5;
                    }else if(k >=35 && k<42){
                        selectBtnIndex = 6;
                    }
                    codeStr = "GG-" + selectBtnIndex + "-" + code;
                    if(icon.equals(icon1)){
                        checkDrawable = resources.getDrawable(R.drawable.uncheck_bg);
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
                        BettingInfo info = new BettingInfo();
                        info.setCode(codeStr);
                        info.setStage(activity.currentStage.getId());
                        info.setTimes(number);
                        info.setName("正码"+selectBtnIndex+"@" + names[k]);
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
