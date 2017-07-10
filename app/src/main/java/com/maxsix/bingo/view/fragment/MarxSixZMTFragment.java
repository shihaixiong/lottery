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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.circleprogress.Utils;
import com.maxsix.bingo.view.activity.CheckActivity;
import com.maxsix.bingo.view.activity.MarxSixActivity;
import com.maxsix.bingo.vo.BettingInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MarxSixZMTFragment extends BaseFragment implements View.OnClickListener{

    private RelativeLayout[] imgs= new RelativeLayout[49];
    private int selectBtnIndex = 1;
    private Button[] btns = new Button[6];
    private int[] btnIds = new int[6];
    private Context context;
    private int[] imgIds = new int[49];

    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
//    private List<BettingInfo> infos = new ArrayList<BettingInfo>();
    private int sumTZ = 0;
    private View rv;
    private String[] names = {"单","双","大","小","合单","合双","尾大","尾小","红波","蓝波","绿波"};
    private String[] codevalues = {"DN","SN","DA","XA","HSD","HSS","WD","WX","HB","LB","LVB"};
    private RelativeLayout[] tms= new RelativeLayout[11];
    private int tmids[] = new int[11];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = inflater.inflate(
                R.layout.fragment_marxsixzmt, container, false);
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
        for(int a=1;a<12;a++){
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
        for(int a=1;a<7;a++){
            String key = "zmt_"+a;
            btnIds[a-1] = resources.getIdentifier(key, "id", context.getPackageName());
            btns[a-1] = (Button)_rv.findViewById(btnIds[a-1]);
            btns[a-1].setOnClickListener(new ZMTClickListener());
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
        bundle.putString("stageName","正码特");
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
                    String codeStr ="";
                    codeStr = "Z-" +  code+ "-" + selectBtnIndex;
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
                        info.setName("正"+selectBtnIndex+"码@" + code);
                        infos.add(info);
                        selectNumberValue = selectNumberValue+1;
                        activity.setNumber(selectNumberValue);
                    }

                    rl.setBackground(checkDrawable);
                }
            }
        }
    };
    class TMClickListener implements View.OnClickListener {
        private int number =0;
        @Override
        public void onClick(final View v) {
            int selectNumberValue = 0;
            MarxSixActivity activity = (MarxSixActivity)getActivity();
            if(activity.currentStage ==null){
                com.maxsix.bingo.util.Utils.showShortToast(context, "当前没有有效投注期,请在有效投注期内投注");
                return;
            }
            for (int k = 0; k < 11; k++) {
                if (tmids[k] == v.getId()) {
                    RelativeLayout rl =  tms[k];
                    Resources resources = getContext().getResources();
                    Drawable checkDrawable = resources.getDrawable(R.drawable.check_bg);
                    Drawable.ConstantState icon = checkDrawable.getConstantState();
                    Drawable.ConstantState icon1 = rl.getBackground().getConstantState();
                    String codeStr ="Z-"+codevalues[k]+"-"+selectBtnIndex;
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
                        sumTZ += number;
                        info.setTimes(number);
                        info.setName("正"+selectBtnIndex+"码@" + names[k]);
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
    class ZMTClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            Resources resources = getContext().getResources();
            Button btn = null;
            for(RelativeLayout ll :imgs) {
                Drawable drawable = resources.getDrawable(R.drawable.uncheck_bg);
                ll.setBackground(drawable);
            }
            for(RelativeLayout rl:tms){
                Drawable drawable = resources.getDrawable(R.drawable.uncheck_bg);
                rl.setBackground(drawable);
            }
            for (int k = 0; k < 6; k++) {
                btn  =  btns[k];
                if (btnIds[k] == v.getId()) {
                    selectBtnIndex = k+1;
                    Drawable checkDrawable = resources.getDrawable(R.color.red);
                    Drawable.ConstantState icon = checkDrawable.getConstantState();
                    Drawable.ConstantState icon1 = btn.getBackground().getConstantState();
                    if(icon.equals(icon1)){
                        Drawable whiteDrawable = resources.getDrawable(R.color.white);
                        btn.setBackground(whiteDrawable);
                    }else{
                        btn.setBackground(checkDrawable);
                    }
                }else{
                    Drawable whiteDrawable = resources.getDrawable(R.color.white);
                    btn.setBackground(whiteDrawable);
                }
            }
            for(BettingInfo info:infos){
                    String strlm = info.getCode().substring(info.getCode().length()-1,info.getCode().length());
                    if(Integer.parseInt(strlm) == selectBtnIndex){
                        Drawable drawable = resources.getDrawable(R.drawable.check_bg);
                        RelativeLayout rl = null;
                        String[] strs = info.getCode().split("-");
                        if(com.maxsix.bingo.util.Utils.isNumber(strs[1])){
                            int a = Integer.parseInt(strs[1]);
                            rl = imgs[a-1];
                        }else{
                            for(int a=0;a<11;a++){
                                if(codevalues[a].equals(strs[1])){
                                    rl = tms[a];
                                }
                            }
                        }
                        rl.setBackground(drawable);
                }
            }
        }
    };
}
