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
import com.maxsix.bingo.view.activity.BJPKActivity;
import com.maxsix.bingo.view.activity.CheckActivity;
import com.maxsix.bingo.view.activity.GdklcActivity;
import com.maxsix.bingo.vo.BettingInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class BJPKGYFragment extends BaseFragment implements View.OnClickListener{

    private Context context;

    private int[] linearLayoutIds = new int[17];
    private LinearLayout[] linearLayouts = new LinearLayout[17];
    private TextView[] names = new TextView[17];
    private int[] namesID = new int[17];
    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
    private View rv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = inflater.inflate(
                R.layout.fragment_bjgy, container, false);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this, rv);
        context = rv.getContext();
        initView(rv);
        infos.clear();
        return rv;
    }
    public void initView(View _rv) {
        Resources resources = context.getResources();

        for(int i=0;i< 17;i++){
            String key = "ll_"+(i+1);
            String namekey = "txt_"+(i+1);
            linearLayoutIds[i] = resources.getIdentifier(key, "id", context.getPackageName());
            linearLayouts[i] = (LinearLayout)_rv.findViewById( resources.getIdentifier(key, "id", context.getPackageName()));
            linearLayouts[i].setOnClickListener(new LinearLayoutClickListener());
            namesID[i] =  resources.getIdentifier(namekey, "id", context.getPackageName());
            names[i] = (TextView)_rv.findViewById( resources.getIdentifier(namekey, "id", context.getPackageName()));
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
            BJPKActivity activity = (BJPKActivity)getActivity();
            if(activity.currentStage ==null || activity.currentStage.getStatus() !=0){
                return;
            }
            for (int k = 0; k < 17; k++) {
                if (linearLayoutIds[k] == v.getId()) {
                    LinearLayout rl =  linearLayouts[k];
                    Resources resources = getContext().getResources();
                    Drawable checkDrawable = resources.getDrawable(R.drawable.check_bg);
                    Drawable.ConstantState icon = checkDrawable.getConstantState();
                    Drawable.ConstantState icon1 = rl.getBackground().getConstantState();
                    String codeStr = "GYH-"+names[k].getText().toString()+"-0";
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
                        info.setName("冠亚和@"+names[k].getText().toString());
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
