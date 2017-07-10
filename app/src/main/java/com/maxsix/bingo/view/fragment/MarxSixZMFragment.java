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
import com.maxsix.bingo.view.activity.MarxSixActivity;
import com.maxsix.bingo.vo.BettingInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MarxSixZMFragment extends BaseFragment implements View.OnClickListener{

    private RelativeLayout[] imgs= new RelativeLayout[49];

    private Context context;
    private int[] imgIds = new int[49];

    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
    private List<BettingInfo> infos = new ArrayList<BettingInfo>();
    private int sumTZ = 0;
    private View rv;
    private String[] names = {"总单","总双","总大","总小"};
    private String[] codevalues = {"Z-ZHDN-0","Z-ZHS-0","Z-ZHD-0","Z-ZHX-0"};
    private RelativeLayout[] tms= new RelativeLayout[11];
    private int tmids[] = new int[11];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = inflater.inflate(
                R.layout.fragment_marxsixzm, container, false);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this, rv);
        context = rv.getContext();
        initView(rv);
        infos.clear();
        return rv;
    }
    public void initView(View _rv) {
        Resources resources = context.getResources();
        for (int k =1;k<50;k++){
            String key = "";
            if(k < 10){
                key="img_0"+k;
            }else
            {
                key = "img_"+k;
            }
            imgIds[k-1] = resources.getIdentifier(key, "id", context.getPackageName());
            imgs[k-1] = (RelativeLayout)_rv.findViewById(imgIds[k-1]);
            imgs[k-1].setOnClickListener(new ImgClickListener());
        }
        for(int a=1;a<5;a++){
            String key = "";
            if(a< 10){
                key="tm_0"+a;
            }else
            {
                key = "tm_"+a;
            }
            tmids[a-1] = resources.getIdentifier(key, "id", context.getPackageName());
            tms[a-1] = (RelativeLayout)_rv.findViewById(tmids[a-1]);
            tms[a-1].setOnClickListener(new TMClickListener());
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
        bundle.putString("stageName","正码");
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
        for(RelativeLayout ll :imgs) {
            Resources resources = getContext().getResources();
            Drawable checkDrawable = resources.getDrawable(R.drawable.uncheck_bg);
            ll.setBackground(checkDrawable);
        }
        for(RelativeLayout rl:tms){
            Resources resources = getContext().getResources();
            Drawable checkDrawable = resources.getDrawable(R.drawable.uncheck_bg);
            rl.setBackground(checkDrawable);
        }
    }

    class ImgClickListener implements View.OnClickListener {
        private int number =0;
        @Override
        public void onClick(final View v) {
            int selectNumberValue = 0;
            MarxSixActivity activity = (MarxSixActivity)getActivity();
            if(activity.currentStage ==null || activity.currentStage.getStatus() !=0){
                return;
            }
            for (int k = 0; k < 49; k++) {
                if (imgIds[k] == v.getId()) {
                    RelativeLayout rl =  imgs[k];
                    Resources resources = getContext().getResources();
                    Drawable checkDrawable = resources.getDrawable(R.drawable.check_bg);
                    Drawable.ConstantState icon = checkDrawable.getConstantState();
                    Drawable.ConstantState icon1 = rl.getBackground().getConstantState();
                    int code = k + 1;
                    String codeStr = "Z-" + code+"-0";
                    if(icon.equals(icon1)){
                        checkDrawable = resources.getDrawable(R.drawable.uncheck_bg);
                        BettingInfo info = null;
                        for( BettingInfo tempinfo:infos){
                            if(tempinfo.getCode().equals(codeStr)){
                                info = tempinfo;
                            }
                        }
                        infos.remove(info);
                    }else{
                        BettingInfo info = new BettingInfo();
                        info.setCode(codeStr);
                        info.setStage(activity.currentStage.getId());
                        info.setTimes(number);
                        info.setName("正码@" + code);
                        infos.add(info);
                    }
                    if(selectNumberValue > 0) {
                    }else {
                    }
                    rl.setBackground(checkDrawable);
                }
            }
        }
    };
    class TMClickListener implements View.OnClickListener {
        private int number =0;
        MarxSixActivity activity = (MarxSixActivity)getActivity();
        @Override
        public void onClick(final View v) {
            for (int k = 0; k < 4; k++) {
                if (tmids[k] == v.getId()) {
                    RelativeLayout rl =  tms[k];
                    Resources resources = getContext().getResources();
                    Drawable checkDrawable = resources.getDrawable(R.drawable.check_bg);
                    Drawable.ConstantState icon = checkDrawable.getConstantState();
                    Drawable.ConstantState icon1 = rl.getBackground().getConstantState();
                    String codeStr =codevalues[k];
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
                        sumTZ += number;
                        info.setTimes(number);
                        info.setName("正码@" + names[k]);
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
