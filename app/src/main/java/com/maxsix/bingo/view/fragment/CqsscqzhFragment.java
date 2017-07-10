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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxsix.bingo.R;
import com.maxsix.bingo.circleprogress.DonutProgress;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.view.activity.BalanceActivity;
import com.maxsix.bingo.view.activity.BettingListActivity;
import com.maxsix.bingo.view.activity.CheckActivity;
import com.maxsix.bingo.view.activity.CqsscActivity;
import com.maxsix.bingo.vo.BettingInfo;
import com.maxsix.bingo.vo.User;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class CqsscqzhFragment extends BaseFragment implements View.OnClickListener{


    private int[] linearLayoutIds = new int[15];
    private String[] valuesStr ={"f_b","f_s","f_d","f_bs","f_z","m_b","m_s","m_d","m_bs","m_z","b_b","b_s","b_d","b_bs","b_z"};
    private String[] keyStr = {"F-B-0","F-S-0","F-D-0","F-BS-0","F-Z-0","M-B-0","M-S-0","M-D-0","M-BS-0","M-Z-0","B-B-0","B-S-0","B-D-0","B-BS-0","B-Z-0"};
    private String[] namesStr ={"前三@豹子","前三@顺子","前三@对子","前三@半顺","前三@杂六","中三@豹子","中三@顺子",
            "中三@对子","中三@半顺","中三@杂六","后三@豹子",
            "后三@顺子","后三@对子","后三@半顺","后三@杂六"};
    private LinearLayout[] linearLayouts = new LinearLayout[15];
    private Context context;
    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
//    private ArrayList<com.maxsix.bingo.vo.BettingInfo> infos = new ArrayList<com.maxsix.bingo.vo.BettingInfo>();
    private View rv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = inflater.inflate(
                R.layout.fragment_sscqzh, container, false);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this, rv);
        context = rv.getContext();
        initView(rv);
        infos.clear();

        return rv;
    }
    public void initView(View _rv) {
        Resources resources = context.getResources();

        for(int i=0;i< valuesStr.length;i++){
            linearLayoutIds[i]  = resources.getIdentifier(valuesStr[i], "id", context.getPackageName());
            linearLayouts[i] = (LinearLayout)_rv.findViewById(linearLayoutIds[i]);
            linearLayouts[i].setOnClickListener(new LinearLayoutClickListener());
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
            CqsscActivity activity = (CqsscActivity)getActivity();
            if(activity.currentStage ==null || activity.currentStage.getStatus() !=0){
                return;
            }
            for (int k = 0; k < 15; k++) {
                if (linearLayoutIds[k] == v.getId()) {
                    LinearLayout rl =  linearLayouts[k];
                    Resources resources = getContext().getResources();
                    Drawable checkDrawable = resources.getDrawable(R.drawable.check_bg);
                    Drawable.ConstantState icon = checkDrawable.getConstantState();
                    Drawable.ConstantState icon1 = rl.getBackground().getConstantState();
                    String codeStr = keyStr[k];
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
                        info.setName(namesStr[k]);
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
