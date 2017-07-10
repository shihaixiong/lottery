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
import android.widget.RelativeLayout;
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
import com.maxsix.bingo.view.activity.MarxSixActivity;
import com.maxsix.bingo.vo.BettingInfo;
import com.maxsix.bingo.vo.Stage;
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

public class CqsscfiveFragment extends BaseFragment implements View.OnClickListener{


    private Context context;
    public Stage stage;

    @InjectView(R.id.tv_selectNumber)
    private TextView tv_selectNumber;

    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
//    private ArrayList<BettingInfo> infos = new ArrayList<BettingInfo>();
    private View rv;
    private int[] relativeLayoutIds = new int[10];
    private RelativeLayout[] relativeLayouts = new RelativeLayout[10];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = inflater.inflate(
                R.layout.fragment_sscfive, container, false);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this, rv);
        tv_selectNumber.setText("第五球");
        context = rv.getContext();
        initView(rv);
        infos.clear();

        return rv;
    }
    public void initView(View _rv) {
        Resources resources = context.getResources();

        for(int i=0;i< 10;i++){
            String key = "ge_"+i;
            relativeLayoutIds[i]  = resources.getIdentifier(key, "id", context.getPackageName());
            relativeLayouts[i] = (RelativeLayout)_rv.findViewById(relativeLayoutIds[i]);
            relativeLayouts[i].setOnClickListener(new RelativeLayoutClickListener());
        }
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void resetValue() {
        infos.clear();
        for(RelativeLayout ll :relativeLayouts) {
            Resources resources = getContext().getResources();
            Drawable checkDrawable = resources.getDrawable(R.drawable.uncheck_bg);
            ll.setBackground(checkDrawable);
        }
    }

    class RelativeLayoutClickListener implements View.OnClickListener {
        private int number =0;
        @Override
        public void onClick(final View v) {
            CqsscActivity activity = (CqsscActivity)getActivity();
            if(activity.currentStage ==null || activity.currentStage.getStatus() !=0){
                return;
            }
            for (int k = 0; k < 10; k++) {
                if (relativeLayoutIds[k] == v.getId()) {
                    RelativeLayout rl =  relativeLayouts[k];
                    Resources resources = getContext().getResources();
                    Drawable checkDrawable = resources.getDrawable(R.drawable.check_bg);
                    Drawable.ConstantState icon = checkDrawable.getConstantState();
                    Drawable.ConstantState icon1 = rl.getBackground().getConstantState();
                    String codeStr = "N-5-" + k;
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
                        if(k==10) {
                            info.setName("第五球 " + k);
                        }else{
                            info.setName("第五球 0" + k);
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
