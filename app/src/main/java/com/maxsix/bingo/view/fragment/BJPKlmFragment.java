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

public class BJPKlmFragment extends BaseFragment implements View.OnClickListener{

    private Context context;

    private int[] linearLayoutIds = new int[54];
    private LinearLayout[] linearLayouts = new LinearLayout[54];
    private String[] valuesStr ={"GYD-0-0","GYX-0-0","GYDN-0-0","GYS-0-0",
            "N-1-DA","N-1-XA","N-1-DN","N-1-SN","N-1-LONG","N-1-HU",
            "N-2-DA","N-2-XA","N-2-DN","N-2-SN","N-2-LONG","N-2-HU",
            "N-3-DA","N-3-XA","N-3-DN","N-3-SN","N-3-LONG","N-3-HU",
            "N-4-DA","N-4-XA","N-4-DN","N-4-SN","N-4-LONG","N-4-WX",
            "N-5-DA","N-5-XA","N-5-DN","N-5-SN","N-5-LONG","N-5-HU",
            "N-6-DA","N-6-XA","N-6-DN","N-6-SN",
            "N-7-DA","N-7-XA","N-7-DN","N-7-SN",
            "N-8-DA","N-8-XA","N-8-DN","N-8-SN",
            "N-9-DA","N-9-XA","N-9-DN","N-9-SN",
            "N-10-DA","N-10-XA","N-10-DN","N-10-SN"};
    private String[] nameStr = {"冠亚大","冠亚小","冠亚单","冠亚双","冠军@大","冠军@小","冠军@单","冠军@双","冠军@龙","冠军@虎",
            "亚军@大","亚军@小","亚军@单","亚军@双","亚军@龙","亚军@虎",
            "第三名@大","第三名@小","第三名@单","第三名@双","第三名@龙","第三名@虎",
            "第四名@大","第四名@小","第四名@单","第四名@双","第四名@龙","第四名@虎",
            "第五名@大","第五名@小","第五名@单","第五名@双","第五名@龙","第五名@虎",
            "第六名@大","第六名@小","第六名@单","第六名@双",
            "第七名@大","第七名@小","第七名@单","第七名@双",
            "第八名@大","第八名@小","第八名@单","第八名@双",
            "第九名@大","第九名@小","第九名@单","第九名@双",
            "第十名@大","第十名@小","第十名@单","第十名@双"
    };
    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
    private View rv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = inflater.inflate(
                R.layout.fragment_bjlm, container, false);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this, rv);
        context = rv.getContext();
        initView(rv);
        infos.clear();
        return rv;
    }
    public void initView(View _rv) {
        Resources resources = context.getResources();

        for(int i=0;i< 54;i++){
            String key = "ll_"+(i+1);
            linearLayoutIds[i] = resources.getIdentifier(key, "id", context.getPackageName());
            linearLayouts[i] = (LinearLayout)_rv.findViewById( resources.getIdentifier(key, "id", context.getPackageName()));
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
            BJPKActivity activity = (BJPKActivity)getActivity();
            if(activity.currentStage ==null || activity.currentStage.getStatus() !=0){
                return;
            }
            for (int k = 0; k < 54; k++) {
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
                        info.setName(nameStr[k]);
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
