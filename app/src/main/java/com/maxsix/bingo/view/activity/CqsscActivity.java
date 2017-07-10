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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxsix.bingo.R;
import com.maxsix.bingo.config.Constants;
import com.maxsix.bingo.service.ConstValue;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.view.fragment.BaseFragment;
import com.maxsix.bingo.view.fragment.CqsscfiveFragment;
import com.maxsix.bingo.view.fragment.CqsscfourFragment;
import com.maxsix.bingo.view.fragment.CqssclmFragment;
import com.maxsix.bingo.view.fragment.CqssconeFragment;
import com.maxsix.bingo.view.fragment.CqsscqzhFragment;
import com.maxsix.bingo.view.fragment.CqsscthreeFragment;
import com.maxsix.bingo.view.fragment.CqssctwoFragment;
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
public class CqsscActivity extends BaseActivity implements View.OnClickListener {

    private Button[] btns;
    private BaseFragment[] fragments;
    private CqssclmFragment cqsscFragment;
    private CqssconeFragment cqsscFragment1;
    private CqssctwoFragment cqsscFragment2;
    private CqsscthreeFragment cqsscFragment3;
    private CqsscfourFragment cqsscFragment4;
    private CqsscfiveFragment cqsscFragment5;
    private CqsscqzhFragment cqsscFragment6;
    private TextView tvname;
    private com.maxsix.bingo.vo.User user;
    private int index = 0;
    public Stage currentStage;
    private Stage previousStage;
    private int currentTabIndex = 0;// 当前fragment的index
    @InjectView(R.id.previousStageName)
    private TextView previousStageName;
    @InjectView(R.id.nextqs)
    private TextView nextStage;
    @InjectView(R.id.txt_wan)
    private TextView txt_wan;
    @InjectView(R.id.txt_qian)
    private TextView txt_qian;
    @InjectView(R.id.txt_bai)
    private TextView txt_bai;
    @InjectView(R.id.txt_shi)
    private TextView txt_shi;
    @InjectView(R.id.txt_ge)
    private TextView txt_ge;

    @InjectView(R.id.txt_sum)
    private TextView txt_sum;
    @InjectView(R.id.txt_ds)
    private TextView txt_ds;
    @InjectView(R.id.txt_dx)
    private TextView txt_dx;
    @InjectView(R.id.txt_lh)
    private TextView txt_lh;

    @InjectView(R.id.btn_lm)
    private Button btn_lm;
    @InjectView(R.id.btn_one)
    private Button btn_one;
    @InjectView(R.id.btn_two)
    private Button btn_two;
    @InjectView(R.id.btn_three)
    private Button btn_three;
    @InjectView(R.id.btn_four)
    private Button btn_four;
    @InjectView(R.id.btn_fine)
    private Button btn_fine;
    @InjectView(R.id.btn_qzh)
    private Button btn_qzh;

    // 小时，十位
    @InjectView(R.id.tv_hour_decade)
    private TextView tv_hour_decade;
    // 小时，个位
    @InjectView(R.id.tv_hour_unit)
    private TextView tv_hour_unit;
    // 分钟，十位
    @InjectView(R.id.tv_min_decade)
    private TextView tv_min_decade;
    // 分钟，个位
    @InjectView(R.id.tv_min_unit)
    private TextView tv_min_unit;
    // 秒，十位
    @InjectView(R.id.tv_sec_decade)
    private TextView tv_sec_decade;
    // 秒，个位
    @InjectView(R.id.tv_sec_unit)
    private TextView tv_sec_unit;

    @InjectView(R.id.jieztime)
    private RelativeLayout remailtime;

    @InjectView(R.id.openStr)
    private RelativeLayout openStr;

    @InjectView(R.id.fengpStr)
    private RelativeLayout fengpStr;

    @InjectView(R.id.btn_rest)
    private Button btn_rest;

    @InjectView(R.id.tv_number)
    public TextView tv_number;

    @InjectView(R.id.btn_tz)
    private Button btn_tz;

    @InjectView(R.id.title)
    private TextView title;

    private int hour_decade;
    private int hour_unit;
    private int min_decade;
    private int min_unit;
    private int sec_decade;
    private int sec_unit;
    private int gewei = -1;
    private int shiwei = -1;
    private int baiwei = -1;
    private int qianwei = -1;
    private int wanwei = -1;
    private int sum = 0;
    private String lh = "";
    private String danshuang = "";
    protected String daxiao = "";
    // 计时器
    private Timer timer;
    private Timer timerStage;
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            countDown();
        }
    };
    public class StageTask extends TimerTask {

        @Override
        public void run() {
            getProStage();
        }
    };
    private Timer timerWinstatus;
    private double win =0;
    private double lose = 0;

    @InjectView(R.id.winStatus)
    private TextView winStatus;

    @InjectView(R.id.title_bar)
    private Toolbar title_bar;
    public class winStatusTask extends TimerTask {

        @Override
        public void run() {
            win = 0;
            lose =0;
            getWinStatus();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cqssc);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        setSupportActionBar(title_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText("重庆时时彩");
        title.setTextColor(Color.WHITE);
        user = com.maxsix.bingo.config.AppManager.getInstance().getUserSession();
        cqsscFragment = new CqssclmFragment();
        cqsscFragment1 = new CqssconeFragment();
        cqsscFragment2 = new CqssctwoFragment();
        cqsscFragment3 = new CqsscthreeFragment();
        cqsscFragment4 = new CqsscfourFragment();
        cqsscFragment5 = new CqsscfiveFragment();
        cqsscFragment6 = new CqsscqzhFragment();
        fragments = new BaseFragment[]{cqsscFragment,
                cqsscFragment1, cqsscFragment2, cqsscFragment3, cqsscFragment4, cqsscFragment5, cqsscFragment6};
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, cqsscFragment)
                .add(R.id.fragment_container, cqsscFragment1)
                .add(R.id.fragment_container, cqsscFragment2)
                .add(R.id.fragment_container, cqsscFragment3)
                .add(R.id.fragment_container, cqsscFragment4)
                .add(R.id.fragment_container, cqsscFragment5)
                .add(R.id.fragment_container, cqsscFragment6)
                .hide(cqsscFragment1)
                .hide(cqsscFragment2)
                .hide(cqsscFragment3)
                .hide(cqsscFragment4)
                .hide(cqsscFragment5)
                .hide(cqsscFragment6).show(cqsscFragment).commit();

        btn_lm.setOnClickListener(this);
        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_four.setOnClickListener(this);
        btn_fine.setOnClickListener(this);
        btn_qzh.setOnClickListener(this);

        btn_rest.setOnClickListener(this);
        btn_tz.setOnClickListener(this);
        btns = new Button[7];
        btns[0] = btn_lm;
        btns[1] = btn_one;
        btns[2] = btn_two;
        btns[3] = btn_three;
        btns[4] = btn_four;
        btns[5] = btn_fine;
        btns[6] = btn_qzh;
        getCurrentStage();
        if(timerStage == null) {
            timerStage = new Timer();
            timerStage.schedule(new StageTask(), 0, 60000);
        }
        if(timerWinstatus ==null){
            timerWinstatus = new Timer();
            timerWinstatus.schedule(new winStatusTask(), 0, 180000);
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
                bundle.putInt("lid", 2);
                Intent intent = new Intent(context,
                        BettingListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_stages:
                Intent _intent = new Intent(context,
                        GameRulescqsscActivity.class);
                startActivity(_intent);
                finish();
                break;
            case R.id.stagepoenls:
                intent = new Intent(context,
                        SscStagesActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected void findViewById() {

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

    public void tz(BaseFragment baseFragment,String stageName){
        if(baseFragment.infos.size() == 0){
            Utils.showLongToast(CqsscActivity.this, "请选择投注！");
        }else
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable("infos", (Serializable) baseFragment.infos);
            bundle.putString("title", "重庆时时彩");
            bundle.putString("stageName",stageName);
            Intent intent = new Intent(context, CheckActivity.class);
            bundle.putSerializable("stage", currentStage);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_rest){
            fragments[index].resetValue();
            fragments[index].selectNumberValue = 0;
            setNumber(0);
        }else if(v.getId() == R.id.btn_tz){
            tz(fragments[index],btns[index].getText().toString());
        }else {
            switch (v.getId()) {
                case R.id.btn_lm:
                    index = 0;
                    break;
                case R.id.btn_one:
                    index = 1;
                    break;
                case R.id.btn_two:
                    index = 2;
                    break;
                case R.id.btn_three:
                    index = 3;
                    break;
                case R.id.btn_four:
                    index = 4;
                    break;
                case R.id.btn_fine:
                    index = 5;
                    break;
                case R.id.btn_qzh:
                    index = 6;
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
    public void setNumber(int _number){
        tv_number.setText("共 "+_number+" 注");
    }
    /**
     *
     *
     * @param view
     */
    public void userinfo(View view) {
        startActivity(new Intent(CqsscActivity.this, ProfileActivity.class));
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }
    public void getProStage(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        String url = ConstValue.BASE_URL+"stages/?"+System.currentTimeMillis()+"&lid=2&status=2";
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
                                break;
                            }
                        }
                        if (previousStage != null) {
                            String str = previousStage.getOpened();
                            for (int i = 0; i < str.length(); i++) {
                                int m = Integer.parseInt(str.charAt(i) + "");
                                if (i == 0) {
                                    wanwei = m;

                                } else if (i == 1) {
                                    qianwei = m;

                                } else if (i == 2) {
                                    baiwei = m;

                                } else if (i == 3) {
                                    shiwei = m;

                                } else if (i == 4) {
                                    gewei = m;

                                }
                            }
                            sum = gewei + shiwei + baiwei + qianwei + wanwei;

                            if (sum >= 23) {
                                daxiao = "大";
                            } else {
                                daxiao = "小";
                            }
                            if (sum % 2 == 0) {
                                danshuang = "双";
                            } else {
                                danshuang = "单";
                            }
                            if (wanwei > gewei) {
                                lh = "龙";
                            } else if (wanwei < gewei) {
                                lh = "虎";
                            } else if (wanwei == gewei) {
                                lh = "和";
                            }
                        }
                        mHandler.sendEmptyMessage(0);

                    }
                } catch (Exception e) {

                }
            }
        });
    }
    public void getCurrentStage(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        String url = ConstValue.BASE_URL+"stages/?"+System.currentTimeMillis()+"&lid=2&status=0";
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
                    }else {
                        getCurrentStage();
                    }
                }catch (Exception e){
                    Log.i("TAG", e.getMessage().toString());
                    getCurrentStage();
                }
            }
        });
    }
    public Boolean countDown() {

        if (isCarry4Unit(tv_sec_unit)) {
            if (isCarry4Decade(tv_sec_decade)) {
                if (isCarry4Unit(tv_min_unit)) {
                    if (isCarry4Decade(tv_min_decade)) {
                        if (isDay4Unit(tv_hour_unit)) {
                            if (isDay4Decade(tv_hour_decade)) {
                                tv_min_decade.setText("0");
                                tv_min_unit.setText("0");
                                tv_sec_decade.setText("0");
                                tv_sec_unit.setText("0");
                                btn_tz.setEnabled(false);
                                btn_rest.setEnabled(false);
                                fengpStr.setVisibility(View.VISIBLE);
                                remailtime.setVisibility(View.GONE);
                                stop();
                                getCurrentStage();
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
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
        hour_decade = hour / 10;
        hour_unit = hour - hour_decade * 10;
        if(hour_decade == 0 && hour_unit==0){
            //txthour.setVisibility(View.GONE);
            //tv_hour.setVisibility(View.GONE);
        }
        min_decade = min / 10;
        min_unit = min - min_decade * 10;

        sec_decade = sec / 10;
        sec_unit = sec - sec_decade * 10;
        // 第个time 进行初始化
        timeClean();

    }
    private void timeClean() {
        tv_hour_decade.setText(hour_decade + "");
        tv_hour_unit.setText(hour_unit + "");
        tv_min_decade.setText(min_decade + "");
        tv_min_unit.setText(min_unit + "");
        tv_sec_decade.setText(sec_decade + "");
        tv_sec_unit.setText(sec_unit + "");
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
    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(msg.what == 0)
            {
                txt_wan.setText(wanwei + "");
                txt_qian.setText(qianwei + "");
                txt_bai.setText(baiwei + "");
                txt_shi.setText(shiwei + "");
                txt_ge.setText(gewei + "");
                txt_sum.setText(sum + "");
                if (previousStage != null) {
                    previousStageName.setText(previousStage.getCurrent()+"期");
                    if(currentStage == null){
                        nextStage.setText("距"+previousStage.getCurrent() + "期封盘");
                        fengpStr.setVisibility(View.VISIBLE);
                        remailtime.setVisibility(View.GONE);
                    }
                }
                txt_dx.setText(daxiao);
                txt_ds.setText(danshuang);
                txt_lh.setText(lh);
            }
        }
    };
    Handler mcurrentHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(msg.what == 0)
            {
                if (currentStage != null && currentStage.getStatus() == 0) {

                    nextStage.setText("距"+currentStage.getCurrent() + "期封盘");
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
                            long min = diff % 86400000 % 3600000 / 60000-1;       //以分钟为单位取整
                            long seconds = diff % 86400000 % 3600000 % 60000 / 1000;   //以秒为单位取整
                            if(diff > 0 && hour > 0){
                                btn_tz.setBackgroundDrawable(getResources().getDrawable(
                                        R.drawable.btn_unenable_green));
                                btn_tz.setTextColor(Color.parseColor("#aaaaaa"));
                                btn_tz.setEnabled(false);
                                fengpStr.setVisibility(View.VISIBLE);
                                remailtime.setVisibility(View.GONE);
                                getCurrentStage();
                            } else if (diff > 0 && min <10) {
                                remailtime.setVisibility(View.VISIBLE);
                                fengpStr.setVisibility(View.GONE);
                                setTime((int) day, (int) hour, (int) min, (int) seconds);
                                btn_tz.setEnabled(true);
                                btn_tz.setBackground(getResources().getDrawable(R.color.timeselect));
                                btn_tz.setTextColor(Color.WHITE);
                                start();
                            } else {
                                btn_tz.setBackgroundDrawable(getResources().getDrawable(
                                        R.drawable.btn_unenable_green));
                                btn_tz.setTextColor(Color.parseColor("#aaaaaa"));
                                btn_tz.setEnabled(false);
                                fengpStr.setVisibility(View.VISIBLE);
                                remailtime.setVisibility(View.GONE);
                                getCurrentStage();
                            }
                        } catch (Exception e) {
                            Log.i("TAG", e.getMessage().toString());
                        }
                    }
                }else{
                    btn_tz.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.btn_unenable_green));
                    btn_tz.setTextColor(Color.parseColor("#aaaaaa"));
                    btn_tz.setEnabled(false);
                    fengpStr.setVisibility(View.VISIBLE);
                    remailtime.setVisibility(View.GONE);
                    getCurrentStage();
                }
            }
        }
    };
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
                String url = Constants.User_BaseURL + "0/bettings/" + "?" + System.currentTimeMillis() + "&date=" + date + "&page="+currentPage+"&status=1&lid=2";
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
                }else {
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
                String url = com.maxsix.bingo.config.Constants.User_BaseURL + "0/bettings/" + "?" + System.currentTimeMillis() + "&date=" + date + "&page="+currentPage+"&status=2&lid=2";
                Log.i("rul",url);
                String json = http.Get(url);
                com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo> items = gson.fromJson(json, new TypeToken<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo>>() {
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
                        currentPage = 1;
                        double getwin = win - lose;

                        DecimalFormat df = new DecimalFormat("#.##");
                        winStatus.setText(Double.parseDouble(df.format(getwin)) + "");
                    }
                }else {
                    currentPage = 1;
                    double getwin = win - lose;
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
