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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.view.activity.CheckActivity;
import com.maxsix.bingo.view.activity.CqsscActivity;
import com.maxsix.bingo.view.activity.GdklcActivity;
import com.maxsix.bingo.vo.BettingInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class GgkllmFragment extends BaseFragment implements View.OnClickListener{

    private Context context;

    private int[] linearLayoutIds = new int[78];
    private LinearLayout[] linearLayouts = new LinearLayout[78];
    private TextView[] txts = new TextView[78];
    private String[] valuesStr ={"ZHD-0-0","ZHX-0-0","ZHDN-0-0","ZHS-0-0","ZWD-0-0","ZWX-0-0",
            "N-1-DA","N-1-XA","N-1-DN","N-1-SN","N-1-WD","N-1-WX","N-1-HSD","N-1-HSS","N-1-LONG","N-1-HU",
            "N-2-DA","N-2-XA","N-2-DN","N-2-SN","N-2-WD","N-2-WX","N-2-HSD","N-2-HSS","N-2-LONG","N-2-HU",
            "N-3-DA","N-3-XA","N-3-DN","N-3-SN","N-3-WD","N-3-WX","N-3-HSD","N-3-HSS","N-3-LONG","N-3-HU",
            "N-4-DA","N-4-XA","N-4-DN","N-4-SN","N-4-WD","N-4-WX","N-4-HSD","N-4-HSS","N-4-LONG","N-4-HU",
            "N-5-DA","N-5-XA","N-5-DN","N-5-SN","N-5-WD","N-5-WX","N-5-HSD","N-5-HSS",
            "N-6-DA","N-6-XA","N-6-DN","N-6-SN","N-6-WD","N-6-WX","N-6-HSD","N-6-HSS",
            "N-7-DA","N-7-XA","N-7-DN","N-7-SN","N-7-WD","N-7-WX","N-7-HSD","N-7-HSS",
            "N-8-DA","N-8-XA","N-8-DN","N-8-SN","N-8-WD","N-8-WX","N-8-HSD","N-8-HSS"};
    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
    private View rv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = inflater.inflate(
                R.layout.fragment_gdlm, container, false);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this, rv);
        context = rv.getContext();
        initView(rv);
        infos.clear();
        return rv;
    }
    public void initView(View _rv) {
        Resources resources = context.getResources();


        for(int i=0;i< 78;i++){
            String key = "ll_"+(i+1);
            String txtkey ="txt_"+(i+1);
            linearLayoutIds[i] = resources.getIdentifier(key, "id", context.getPackageName());
            linearLayouts[i] = (LinearLayout)_rv.findViewById( resources.getIdentifier(key, "id", context.getPackageName()));
            linearLayouts[i].setOnClickListener(new LinearLayoutClickListener());
            txts[i] = (TextView)_rv.findViewById( resources.getIdentifier(txtkey, "id", context.getPackageName()));
        }
    }
    @Override
    public void onClick(View v) {

    }


    @Override
    public void resetValue() {
        infos.clear();
        for(LinearLayout ll :linearLayouts) {
            Resources resources = getContext().getResources();
            Drawable checkDrawable = resources.getDrawable(R.drawable.uncheck_bg);
            ll.setBackground(checkDrawable);
        }
    }

    class LinearLayoutClickListener implements View.OnClickListener {
        private int number =0;
        @Override
        public void onClick(final View v) {
            GdklcActivity activity = (GdklcActivity)getActivity();
            if(activity.currentStage ==null || activity.currentStage.getStatus() !=0){
                return;
            }
            for (int k = 0; k < 78; k++) {
                if (linearLayoutIds[k] == v.getId()) {
                    LinearLayout rl =  linearLayouts[k];
                    Resources resources = getContext().getResources();
                    Drawable checkDrawable = resources.getDrawable(R.drawable.check_bg);
                    Drawable.ConstantState icon = checkDrawable.getConstantState();
                    Drawable.ConstantState icon1 = rl.getBackground().getConstantState();
                    String codeStr = valuesStr[k];
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
                        if(k <6) {
                            info.setName(""+txts[k].getText().toString());
                        }else if(k>=6 && k<16) {
                            info.setName("第一球@"+txts[k].getText().toString());
                        }else if(k>=16 && k<26) {
                            info.setName("第二球@"+txts[k].getText().toString());
                        }else if(k>=26 && k<36) {
                            info.setName("第三球 "+txts[k].getText().toString());
                        }else if(k>=36 && k<46) {
                            info.setName("第四球@"+txts[k].getText().toString());
                        }else if(k>=46 && k<54) {
                            info.setName("第五球@"+txts[k].getText().toString());
                        }else if(k>=54 && k<62) {
                            info.setName("第六球@"+txts[k].getText().toString());
                        }else if(k>=62 && k<70) {
                            info.setName("第七球@"+txts[k].getText().toString());
                        }else if(k>=70 && k<78) {
                            info.setName("第八球@"+txts[k].getText().toString());
                        }
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
