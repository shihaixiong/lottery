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

public class MarxSixBBBFragment extends BaseFragment implements View.OnClickListener{

    private LinearLayout[] bbbs= new LinearLayout[24];
    private int selectBtnIndex = 1;

    private Context context;
    private int[] bbbIds = new int[24];
    private TextView[] txts = new TextView[24];
    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
//    private List<BettingInfo> infos = new ArrayList<BettingInfo>();
    private int sumTZ = 0;
    private View rv;
    private String[] codevalues = {"T-BH-DD","T-BH-DS","T-BH-XD","T-BH-XS","T-BLV-DD","T-BLV-DS","T-BLV-XD","T-BLV-XS","T-BL-DD","T-BL-DS","T-BL-XD","T-BL-XS",
            "T-TBH-DA","T-TBH-XA","T-TBH-DN","T-TBH-SN",
            "T-TBLV-DA","T-TBLV-XA","T-TBLV-DN","T-TBLV-SN",
            "T-TBL-DA","T-TBL-XA","T-TBL-DN","T-TBL-SN"};
    private String[] names = {"半半波@红大单","半半波@红大双","半半波@红小单","半半波@红小双","半半波@绿大单","半半波@绿大双","半半波@绿小单","半半波@绿小双","半半波@蓝大单","半半波@蓝大双","半半波@蓝小单","半半波@蓝小双",
            "半波@红大","半波@红小","半波@红单", "半波@红双",
            "半波@绿大","半波@绿小","半波@绿单","半波@绿双",
            "半波@蓝大","半波@蓝小","半波@蓝单","半波@蓝双"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = inflater.inflate(
                R.layout.fragment_marxsixbbb, container, false);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this, rv);
        context = rv.getContext();
        initView(rv);
        infos.clear();
        return rv;
    }
    public void initView(View _rv) {
        Resources resources = context.getResources();
        for (int k =1;k<25;k++){
            String key = "zx_"+k;
            String txtKey = "txt_"+k;
            bbbIds[k-1] = resources.getIdentifier(key, "id", context.getPackageName());
            bbbs[k-1] = (LinearLayout)_rv.findViewById(bbbIds[k-1]);
            bbbs[k-1].setOnClickListener(new ImgClickListener());
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
        bundle.putString("stageName","半半波");
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
        for(LinearLayout ll :bbbs) {
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
            for (int k = 0; k < 24; k++) {
                if (bbbIds[k] == v.getId()) {
                    LinearLayout rl =  bbbs[k];
                    Resources resources = getContext().getResources();
                    Drawable checkDrawable = resources.getDrawable(R.drawable.check_bg);
                    Drawable.ConstantState icon = checkDrawable.getConstantState();
                    Drawable.ConstantState icon1 = rl.getBackground().getConstantState();
                    String code = codevalues[k];
                    String codeStr ="";
                    codeStr = code;
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
                        info.setName(names[k]);
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
