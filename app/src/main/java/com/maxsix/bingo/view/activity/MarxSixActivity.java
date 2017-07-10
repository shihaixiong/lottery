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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxsix.bingo.R;
import com.maxsix.bingo.config.Constants;
import com.maxsix.bingo.service.ConstValue;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.view.fragment.BaseFragment;
import com.maxsix.bingo.view.fragment.MarxSixBBBFragment;
import com.maxsix.bingo.view.fragment.MarxSixGGFragment;
import com.maxsix.bingo.view.fragment.MarxSixTMFragment;
import com.maxsix.bingo.view.fragment.MarxSixTMTWFragment;
import com.maxsix.bingo.view.fragment.MarxSixTXFragment;
import com.maxsix.bingo.view.fragment.MarxSixWHBBFragment;
import com.maxsix.bingo.view.fragment.MarxSixZMFragment;
import com.maxsix.bingo.view.fragment.MarxSixZMTFragment;
import com.maxsix.bingo.view.fragment.MarxSixZXFragment;
import com.maxsix.bingo.vo.BettingListInfo;
import com.maxsix.bingo.vo.GridList;
import com.maxsix.bingo.vo.Stage;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * TODO
 */
public class MarxSixActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvname;
    private com.maxsix.bingo.vo.User user;
    private BaseFragment[] fragments;
    private Button[] btns;
    private int index = 0;
    private int currentTabIndex = 0;// 当前fragment的index
    private MarxSixTMFragment tmFragment;
    private MarxSixZMFragment zmFragment;
    private MarxSixZMTFragment marxSixZMTFragment;
    private MarxSixBBBFragment marxSixBBBFragment;
    private MarxSixZXFragment marxSixZXFragment;
    private MarxSixTXFragment marxSixTXFragment;
    private MarxSixTMTWFragment marxSixTMTWFragment;
    private MarxSixWHBBFragment marxSixWHBBFragment;

    @InjectView(R.id.btn_tm)
    private Button btn_tm;

    @InjectView(R.id.btn_zm)
    private Button btn_zm;

    @InjectView(R.id.btn_zmt)
    private Button btn_zmt;

    @InjectView(R.id.btn_bbb)
    private Button btn_bbb;

    @InjectView(R.id.btn_zx)
    private Button btn_zx;

    @InjectView(R.id.btn_tx)
    private Button btn_tx;

    @InjectView(R.id.btn_tmtw)
    private Button btn_tmtw;

    @InjectView(R.id.btn_whbb)
    private Button btn_whbb;

    @InjectView(R.id.btn_gg)
    private Button btn_gg;
    // 天，十位
    private TextView tv_day_decade;
    // 天，个位
    private TextView tv_day_unit;
    // 小时，十位
    private TextView tv_hour_decade;
    // 小时，个位
    private TextView tv_hour_unit;
    // 分钟，十位
    private TextView tv_min_decade;
    // 分钟，个位
    private TextView tv_min_unit;
    // 秒，十位
    private TextView tv_sec_decade;
    // 秒，个位
    private TextView tv_sec_unit;

    @InjectView(R.id.title)
    private TextView title;

    private int day_decade;
    private int day_unit;
    private TextView[] openNumbers = new TextView[7];
    private int hour_decade;
    private int hour_unit;
    private int min_decade;
    private int min_unit;
    private int sec_decade;
    private int sec_unit;
    private TextView tv_currentStage;
    private Timer timer;
    public Stage currentStage;
    private Stage previousStage;

    @InjectView(R.id.previousStageName)
    private TextView previousStageName;

    @InjectView(R.id.txt_1)
    private TextView txt_1;

    @InjectView(R.id.txt_2)
    private TextView txt_2;

    @InjectView(R.id.txt_3)
    private TextView txt_3;

    @InjectView(R.id.txt_4)
    private TextView txt_4;

    @InjectView(R.id.txt_5)
    private TextView txt_5;

    @InjectView(R.id.txt_6)
    private TextView txt_6;

    @InjectView(R.id.txt_7)
    private TextView txt_7;

    private Timer timerWinstatus;
    private double win =0;
    private double lose = 0;

    @InjectView(R.id.winStatus)
    private TextView winStatus;

    @InjectView(R.id.fengpStr)
    private TextView fengStr;

    @InjectView(R.id.remailtime)
    private LinearLayout remailtime;

    @InjectView(R.id.btn_rest)
    private Button btn_rest;

    @InjectView(R.id.tv_number)
    public TextView tv_number;

    @InjectView(R.id.btn_tz)
    private Button btn_tz;

    @InjectView(R.id.title_bar)
    private Toolbar title_bar;


    public class winStatusTask extends TimerTask {

        @Override
        public void run() {
            getWinStatus();
        }
    };
    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(msg.what == 0)
            {

                if (previousStage != null) {
                    previousStageName.setText(previousStage.getCurrent()+"期");
                    String open = previousStage.getOpened();
                    String imgName = "";
                    int resId = 0;
                    String[] openNumber = previousStage.getOpened().split(",");
                    if(openNumber.length ==7) {
                        for (int a = 0; a < 7; a++) {
                            TextView tv = openNumbers[a];
                            tv.setText(openNumber[a]);
                            if (Constants.redStr.contains(openNumber[a])) {
                                Drawable drawable = getResources().getDrawable(R.drawable.red);
                                tv.setBackground(drawable);
                            } else if (Constants.blueStr.contains(openNumber[a])) {
                                Drawable drawable = getResources().getDrawable(R.drawable.blue);
                                tv.setBackground(drawable);
                            } else if (Constants.greenStr.contains(openNumber[a])) {
                                Drawable drawable = getResources().getDrawable(R.drawable.green);
                                tv.setBackground(drawable);
                            }
                        }
                    }
                }
            }
        }
    };
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            countDown();
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marxsix);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        setSupportActionBar(title_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText("香港六合彩");
        user = com.maxsix.bingo.config.AppManager.getInstance().getUserSession();
        tv_hour_decade = (TextView) findViewById(R.id.tv_hour_decade);
        tv_hour_unit = (TextView) findViewById(R.id.tv_hour_unit);
        tv_day_decade = (TextView) findViewById(R.id.tv_day_decade);
        tv_day_unit = (TextView) findViewById(R.id.tv_day_unit);
        tv_min_decade = (TextView) findViewById(R.id.tv_min_decade);
        tv_min_unit = (TextView) findViewById(R.id.tv_min_unit);
        tv_sec_decade = (TextView) findViewById(R.id.tv_sec_decade);
        tv_sec_unit = (TextView) findViewById(R.id.tv_sec_unit);
        tv_currentStage = (TextView) findViewById(R.id.tv_nextStage);
        openNumbers[0] = txt_1;
        openNumbers[1] = txt_2;
        openNumbers[2] = txt_3;
        openNumbers[3] = txt_4;
        openNumbers[4] = txt_5;
        openNumbers[5] = txt_6;
        openNumbers[6] = txt_7;

        tmFragment = new MarxSixTMFragment();
        zmFragment = new MarxSixZMFragment();
        marxSixZMTFragment = new MarxSixZMTFragment();
        marxSixBBBFragment = new MarxSixBBBFragment();
        marxSixZXFragment = new MarxSixZXFragment();
        marxSixTXFragment = new MarxSixTXFragment();
        marxSixTMTWFragment = new MarxSixTMTWFragment();
        marxSixWHBBFragment = new MarxSixWHBBFragment();
        fragments = new BaseFragment[]{tmFragment,zmFragment,marxSixZMTFragment,marxSixBBBFragment
                ,marxSixZXFragment,marxSixTXFragment
                ,marxSixTMTWFragment,marxSixWHBBFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, tmFragment)
                .add(R.id.fragment_container, zmFragment)
                .add(R.id.fragment_container, marxSixZMTFragment)
                .add(R.id.fragment_container, marxSixBBBFragment)
                .add(R.id.fragment_container,marxSixZXFragment)
                .add(R.id.fragment_container, marxSixTXFragment)
                .add(R.id.fragment_container, marxSixTMTWFragment)
                .add(R.id.fragment_container, marxSixWHBBFragment)
                .hide(zmFragment)
                .hide(marxSixZMTFragment)
                .hide(marxSixBBBFragment)
                .hide(marxSixZXFragment)
                .hide(marxSixTXFragment)
                .hide(marxSixTMTWFragment)
                .hide(marxSixWHBBFragment)
                .show(tmFragment).commit();
        btn_zm.setOnClickListener(this);
        btn_tm.setOnClickListener(this);
        btn_zmt.setOnClickListener(this);
        //btn_gg.setOnClickListener(this);
        btn_bbb.setOnClickListener(this);
        btn_tmtw.setOnClickListener(this);
        btn_whbb.setOnClickListener(this);
        btn_tx.setOnClickListener(this);
        btn_zx.setOnClickListener(this);
        btn_rest.setOnClickListener(this);
        btn_tz.setOnClickListener(this);
        btns = new Button[8];
        btns[0] = btn_tm;
        btns[1] = btn_zm;
        btns[2] = btn_zmt;
        //btns[3] = btn_gg;
        btns[3] =btn_bbb;
        btns[4] = btn_zx;
        btns[5] = btn_tx;
        btns[6] = btn_tmtw;
        btns[7] =btn_whbb;
//        btn_tm.setTextColor(Color.parseColor("#c92b2f"));
        getCurrentStage();
        getProStage();
        if(timerWinstatus ==null){
            timerWinstatus = new Timer();
            timerWinstatus.schedule(new winStatusTask(), 0, 600000);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.bettingRecord:
                Bundle bundle = new Bundle();
                bundle.putInt("lid", 1);
                Intent intent = new Intent(context,
                        BettingListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_stages:
                Intent _intent = new Intent(context,
                        GameRulesActivity.class);
                startActivity(_intent);
                finish();
                break;
            case R.id.stagepoenls:
                intent = new Intent(context,
                        MarxSixStagesActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getProStage(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        String url = ConstValue.BASE_URL+"stages/?"+System.currentTimeMillis()+"&lid=1&status=1";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String content = response.body().string();
                Gson gson = new Gson();
                try {
                    GridList<Stage> items = gson.fromJson(content, new TypeToken<GridList<Stage>>() {
                    }.getType());
                    if (items != null && items.getResults() != null && items.getResults().size() > 0) {
                        for (int i = 0; i < items.getResults().size(); i++) {
                            if (items.getResults().get(i).getOpened() != null && items.getResults().get(i).getOpened() != "") {
                                previousStage = items.getResults().get(i);
                                mHandler.sendEmptyMessage(0);
                                break;
                            }
                        }

                    }
                } catch (Exception e) {

                }
            }
        });
    }
    public void setNumber(int _number){
        tv_number.setText("共 "+_number+" 注");
    }
    public void tz(BaseFragment fragment,String stageName){
        if(fragment.infos.size() ==0){
            Utils.showLongToast(MarxSixActivity.this, "请选择投注项！");
        }else
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable("infos", (Serializable) fragment.infos);
            bundle.putString("title","六合彩");
            bundle.putString("stageName",stageName);
            Intent intent = new Intent(context, CheckActivity.class);
            bundle.putSerializable("stage", currentStage);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    public void getCurrentStage(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        String url = ConstValue.BASE_URL+"stages/?"+System.currentTimeMillis()+"&lid=1";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String content = response.body().string();
                Gson gson = new Gson();
                try {
                    GridList<Stage> items = gson.fromJson(content, new TypeToken<GridList<Stage>>() {
                    }.getType());
                    if (items != null && items.getResults() != null && items.getResults().size() > 0) {
                        currentStage = items.getResults().get(0);
                        mcurrentHandler.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    Log.i("TAG", e.getMessage().toString());
                    getCurrentStage();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected void initControl() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.btn_rest){
            fragments[index].resetValue();
            fragments[index].selectNumberValue = 0;
            setNumber(0);
        }else if(v.getId() == R.id.btn_tz){
            tz(fragments[index], btns[index].getText().toString());
        }else {
            switch (v.getId()) {
                case R.id.btn_tm:
                    index = 0;
                    break;
                case R.id.btn_zm:
                    index = 1;
                    break;
                case R.id.btn_zmt:
                    index = 2;
                    break;
                case R.id.btn_bbb:
                    index = 3;
                    break;
                case R.id.btn_zx:
                    index = 4;
                    break;
                case R.id.btn_tx:
                    index = 5;
                    break;
                case R.id.btn_tmtw:
                    index = 6;
                    break;
                case R.id.btn_whbb:
                    index = 7;
                    break;
                default:
                    break;
            }
            if (currentTabIndex != index) {
                FragmentTransaction trx = getSupportFragmentManager()
                        .beginTransaction();
                trx.hide(fragments[currentTabIndex]);
                if (!fragments[index].isAdded()) {
                    trx.add(R.id.fragment_container, fragments[index]);
                }
                trx.show(fragments[index]).commit();
            }
            btns[currentTabIndex].setTextColor(getResources().getColor(R.color.cp_default));
            btns[currentTabIndex].setBackgroundColor(getResources().getColor(R.color.cp_bg));
            btns[index].setTextColor(Color.WHITE);
            btns[index].setBackground(null);
            btns[index].setBackgroundColor(Color.parseColor("#00000000"));
            currentTabIndex = index;
        }
    }

    /**
     *
     * @Description: 开始计时
     * @param
     * @return void
     * @throws
     */
    public void start() {

        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 1000);
        }
    }

    /**
     *
     * @Description: 停止计时
     * @param
     * @return void
     * @throws
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    // 如果:sum = 12345678
    public void addTime(int sum) {

        // 求出天数
        int day = sum / 60 / 60 / 24;
        // int day_time = sum % 24;
        Log.e("小时", day + "");
        Log.e("小时", sum % 24 + "");

        // 求出小时
        // int hour = day_time / 60;
        // int hour_time = day_time % 60;
        //
        // Log.e("小时", hour + "");
        //
        // 先获取个秒数值
        int sec = sum % 60;
        // 如果大于60秒，获取分钟。（秒数）
        int sec_time = sum / 60;
        // 再获取分钟
        int min = sec_time % 60;
        // 如果大于60分钟，获取小时（分钟数）。
        int min_time = sec_time / 60;
        // 获取小时
        int hour = min_time % 24;
        // 剩下的自然是天数
        day = min_time / 24;

        //
        // Log.e("分钟", min + "");
        //
        // // 求出秒数
        // Log.e("秒数", sec + "");
        setTime(day, hour, min, sec);

    }

    /**
     * @throws Exception
     *
     * @Description: 设置倒计时的时长
     * @param
     * @return void
     * @throws
     */
    public void setTime(int day, int hour, int min, int sec) {
        //这里的天数不写也行，我写365
        if (day >= 365 || hour >= 24 || min >= 60 || sec >= 60 || day < 0
                || hour < 0 || min < 0 || sec < 0) {
            throw new RuntimeException(
                    "Time format is error,please check out your code");
        }
        // day 的十位数
        day_decade = day / 10;
        // day的个位数,这里求余就行
        day_unit = day - day_decade * 10;

        hour_decade = hour / 10;
        hour_unit = hour - hour_decade * 10;

        min_decade = min / 10;
        min_unit = min - min_decade * 10;

        sec_decade = sec / 10;
        sec_unit = sec - sec_decade * 10;
        // 第个time 进行初始化
        timeClean();

    }

    private void timeClean() {
        tv_day_decade.setText(day_decade + "");
        tv_day_unit.setText(day_unit + "");
        tv_hour_decade.setText(hour_decade + "");
        tv_hour_unit.setText(hour_unit + "");
        tv_min_decade.setText(min_decade + "");
        tv_min_unit.setText(min_unit + "");
        tv_sec_decade.setText(sec_decade + "");
        tv_sec_unit.setText(sec_unit + "");
    }

    /**
     *
     * @Description: 倒计时
     * @param
     * @return boolean
     * @throws
     */
    public Boolean countDown() {

        if (isCarry4Unit(tv_sec_unit)) {
            if (isCarry4Decade(tv_sec_decade)) {

                if (isCarry4Unit(tv_min_unit)) {
                    if (isCarry4Decade(tv_min_decade)) {

                        if (isDay4Unit(tv_hour_unit)) {
                            if (isDay4Decade(tv_hour_decade)) {

                                if (isDay4Unit(tv_day_unit)) {
                                    if (isDay4Decade(tv_day_decade)) {
                                        tv_day_decade.setText("0");
                                        tv_day_unit.setText("0");
                                        tv_hour_decade.setText("0");
                                        tv_hour_unit.setText("0");
                                        tv_min_decade.setText("0");
                                        tv_min_unit.setText("0");
                                        tv_sec_decade.setText("0");
                                        tv_sec_unit.setText("0");
                                        stop();
                                        remailtime.setVisibility(View.GONE);
                                        fengStr.setVisibility(View.VISIBLE);
                                        btn_rest.setEnabled(false);
                                        btn_tz.setEnabled(false);
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 进行——时分秒，判断个位数
     *
     * @Description: 变化十位，并判断是否需要进位
     * @param
     * @return boolean
     * @throws
     */
    private boolean isCarry4Decade(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 5;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }

    /**
     * 进行——时分秒，判断个位数
     *
     * @Description: 变化个位，并判断是否需要进位
     * @param
     * @return boolean
     * @throws
     */
    private boolean isCarry4Unit(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 9;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }

    /**
     * 进行——时分秒，判断个位数
     *
     * @Description: 变化十位，并判断是否需要进位
     * @param
     * @return boolean
     * @throws
     */
    private boolean isDay4Unit(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 3;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }

    /**
     * 进行——时分秒，判断个位数
     *
     * @Description: 变化个位，并判断是否需要进位
     * @param
     * @return boolean
     * @throws
     */
    private boolean isDay4Decade(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 2;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }
    Handler mcurrentHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(msg.what == 0)
            {
                if (currentStage != null && currentStage.getStatus() == 0) {

                    tv_currentStage.setText(currentStage.getCurrent() + "期");
                    String[] dates = currentStage.getEnded().split("T");
                    if (dates.length > 0) {
                        try {
                            String dateStr = dates[0] + " " + dates[1];
                            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date fDate = fmt.parse(dateStr);
                            String nowDateStr = fmt.format(new Date());
                            Date nDate = fmt.parse(nowDateStr);
                            long diff = fDate.getTime() - nDate.getTime();
                            long day = diff / 86400000;                         //以天数为单位取整
                            long hour = diff % 86400000 / 3600000;               //以小时为单位取整
                            long min = diff % 86400000 % 3600000 / 60000;       //以分钟为单位取整
                            long seconds = diff % 86400000 % 3600000 % 60000 / 1000;   //以秒为单位取整
                            if (diff > 0) {
                                fengStr.setVisibility(View.GONE);
                                remailtime.setVisibility(View.VISIBLE);
                                btn_tz.setEnabled(true);
                                btn_tz.setBackground(getResources().getDrawable(R.color.timeselect));
                                btn_tz.setTextColor(Color.WHITE);
                                setTime((int) day, (int) hour, (int) min, (int) seconds);
                                start();
                            } else {
                                btn_tz.setBackgroundDrawable(getResources().getDrawable(
                                        R.drawable.btn_unenable_green));
                                btn_tz.setTextColor(Color.parseColor("#aaaaaa"));
                                btn_tz.setEnabled(false);
                                remailtime.setVisibility(View.GONE);
                                fengStr.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {

                        }
                    }
                }else if(currentStage != null){
                    btn_tz.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.btn_unenable_green));
                    btn_tz.setTextColor(Color.parseColor("#aaaaaa"));
                    btn_tz.setEnabled(false);
                    tv_currentStage.setText(currentStage.getCurrent() + "期");
                    remailtime.setVisibility(View.GONE);
                    fengStr.setVisibility(View.VISIBLE);

                    getCurrentStage();
                }
            }
        }
    };
    @Override
    protected void findViewById() {

    }
    private int allRow;         //总记录数
    private int totalPage;        //总页数
    private int currentPage =1;    //当前页
    private int pageSize = 20;        //每页记录数
    public void getWinStatus(){
        doAsync(new Callable<GridList<BettingListInfo>>() {

            @Override
            public com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo> call() throws Exception {
                Gson gson = new Gson();
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format( new Date());
                String url = Constants.User_BaseURL + "0/bettings/" + "?" + System.currentTimeMillis() + "&date=" + date + "&page="+currentPage+"&status=1&lid=1";
                Log.i("rul",url);
                String json = http.Get(url);
                com.maxsix.bingo.vo.GridList<BettingListInfo> items = gson.fromJson(json, new TypeToken<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo>>() {
                }.getType());
                return items;
            }

        }, new com.maxsix.bingo.task.Callback<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo>>() {

            @Override
            public void onCallback(com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo> pCallbackValue) {
                if (pCallbackValue != null && pCallbackValue.getResults() != null && pCallbackValue.getResults().size() > 0) {
                    allRow = (int)pCallbackValue.getCount();
                    totalPage = allRow % pageSize == 0 ? allRow/pageSize : allRow/pageSize+1;
                    for(BettingListInfo info :pCallbackValue.getResults()){
                        win +=info.getTimes()*(info.getOdds()-1);
                    }
                    if(totalPage > currentPage){
                        currentPage++;
                        getWinStatus();
                    }else {
                        currentPage = 1;
                        getLoseStatus();
                    }
                }else{
                    getLoseStatus();
                }
            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {

            }
        }, false, "");
    }
    public void getLoseStatus(){
        doAsync(new Callable<GridList<BettingListInfo>>() {

            @Override
            public com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo> call() throws Exception {
                Gson gson = new Gson();
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format( new Date());
                String url = Constants.User_BaseURL + "0/bettings/" + "?" + System.currentTimeMillis() + "&date=" + date + "&page="+currentPage+"&status=2&lid=1";
                Log.i("rul",url);
                String json = http.Get(url);
                com.maxsix.bingo.vo.GridList<BettingListInfo> items = gson.fromJson(json, new TypeToken<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo>>() {
                }.getType());
                return items;
            }

        }, new com.maxsix.bingo.task.Callback<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo>>() {

            @Override
            public void onCallback(com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo> pCallbackValue) {
                if (pCallbackValue != null && pCallbackValue.getResults() != null && pCallbackValue.getResults().size() > 0) {
                    allRow = (int)pCallbackValue.getCount();
                    totalPage = allRow % pageSize == 0 ? allRow/pageSize : allRow/pageSize+1;
                    for(BettingListInfo info :pCallbackValue.getResults()){
                        lose +=info.getTimes();
                    }
                    if(totalPage > currentPage){
                        currentPage++;
                        getLoseStatus();
                    }else {

                        double getwin = win - lose;
//                        if (getwin < 0) {
//                            winStatus.setTextColor(Color.RED);
//
//                        } else {
//                            winStatus.setTextColor(Color.GREEN);
//                        }
                        DecimalFormat df = new DecimalFormat("#.##");
                        winStatus.setText(Double.parseDouble(df.format(getwin)) + "");
                    }
                }else {
                    currentPage = 1;
                    double getwin = win - lose;
//                    if (getwin < 0) {
//                        winStatus.setTextColor(Color.RED);
//
//                    } else {
//                        winStatus.setTextColor(Color.GREEN);
//                    }
                    DecimalFormat df = new DecimalFormat("#.##");
                    winStatus.setText(Double.parseDouble(df.format(getwin)) + "");
                }
            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {

            }
        }, false, "");
    }
}
