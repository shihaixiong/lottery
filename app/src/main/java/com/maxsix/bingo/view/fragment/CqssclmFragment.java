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
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CqssclmFragment extends BaseFragment implements View.OnClickListener{

    private Context context;

    private int[] linearLayoutIds = new int[27];
    private String[] valuesStr ={"ZHD-0-0","ZHX-0-0","ZHDN-0-0","ZHS-0-0","LONG-0-0","HU-0-0","HE-0-0",
            "N-1-DA","N-1-XA","N-1-DN","N-1-SN",
            "N-2-DA","N-2-XA","N-2-DN","N-2-SN",
            "N-3-DA","N-3-XA","N-3-DN","N-3-SN",
            "N-4-DA","N-4-XA","N-4-DN","N-4-SN",
            "N-5-DA","N-5-XA","N-5-DN","N-5-SN"};
    private String[] keyStr = {"zhd","zhx","zhdn","zhs","lo","hu","he","wanda","wanxa","wandn","wans","qianda","qianxa","qiandn","qians","baida",
            "baixa","baidn","bais","shida","shixa","shidn","shis","geda","gexa","gedn","ges"};
    private String[] namesStr ={"总和大","总和小","总和单","总和双","龍","虎","和",
            "第一球@大","第一球@小","第一球@单","第一球@双",
            "第二球@大","第二球@小","第二球@单","第二球@双",
            "第三球@大","第三球@小","第三球@单","第三球@双",
            "第四球@大","第四球@小","第四球@单","第四球@双",
            "第五球@大","第五球@小","第五球@单","第五球@双"};
    private LinearLayout[] linearLayouts = new LinearLayout[27];

    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
//    private ArrayList<BettingInfo> infos = new ArrayList<BettingInfo>();
    private View rv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = inflater.inflate(
                R.layout.fragment_ssclm, container, false);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this, rv);
        context = rv.getContext();
        initView(rv);
        infos.clear();
        return rv;
    }
    public void initView(View _rv) {
        Resources resources = context.getResources();

        for(int i=0;i< keyStr.length;i++){
            linearLayoutIds[i]  = resources.getIdentifier(keyStr[i], "id", context.getPackageName());
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
            for (int k = 0; k < 27; k++) {
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
