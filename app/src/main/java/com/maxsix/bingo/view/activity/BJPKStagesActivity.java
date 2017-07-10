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

package com.maxsix.bingo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxsix.bingo.R;
import com.maxsix.bingo.adpter.BJPKStagesAdpter;
import com.maxsix.bingo.adpter.CQSSCStagesAdpter;
import com.maxsix.bingo.config.BaseListActivity;

import java.util.concurrent.Callable;

public class BJPKStagesActivity extends BaseListActivity {

    private BJPKStagesAdpter adapter;
    private TextView imgLoading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bjpkstages);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra(com.maxsix.bingo.config.Constants.NAME);
        imgLoading = (TextView)findViewById(R.id.imgLoading);
        findViewById();
        imgLoading.setVisibility(View.VISIBLE);
        Refresh();
    }

    @Override
    protected void initControl() {

    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.samplessc_actions, menu);
        return true;
    }
    @Override
    protected void Refresh()  {
        doAsync(new Callable<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.Stage>>() {

            @Override
            public com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.Stage> call() throws Exception {
                Gson gson = new Gson();
                String url =com.maxsix.bingo.config.Constants.GetStages_URL + "?"+System.currentTimeMillis()+"&page=" + page+"&status=2&lid=5";
                Log.i("rul",url);
                String json = http.Get(url);
                com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.Stage> items = gson.fromJson(json, new TypeToken<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.Stage>>() {
                }.getType());
                return items;
            }

        }, new com.maxsix.bingo.task.Callback<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.Stage>>() {

            @Override
            public void onCallback(com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.Stage> pCallbackValue) {
                if (pCallbackValue != null && pCallbackValue.getResults() != null&&pCallbackValue.getResults().size()>0) {
                    if(pCallbackValue.getResults().size() <= 10) {
                        adapter = new BJPKStagesAdpter(BJPKStagesActivity.this, pCallbackValue.getResults());
                    }else {
                        adapter = new BJPKStagesAdpter(BJPKStagesActivity.this, pCallbackValue.getResults().subList(0,10));
                    }
                    swip.setRefreshing(false);
                    lv.setAdapter(adapter);
                    imgLoading.setVisibility(View.GONE);
                }else{
                    imgLoading.setVisibility(View.GONE);
                }
            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                imgLoading.setVisibility(View.GONE);
            }
        }, false, "");
    }
    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
