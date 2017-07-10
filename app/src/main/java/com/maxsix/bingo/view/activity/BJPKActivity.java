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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxsix.bingo.R;
import com.maxsix.bingo.adpter.BettingsAdpter;
import com.maxsix.bingo.config.Constants;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.view.fragment.BJPKGYFragment;
import com.maxsix.bingo.view.fragment.BJPKeightFragment;
import com.maxsix.bingo.view.fragment.BJPKfineFragment;
import com.maxsix.bingo.view.fragment.BJPKfourFragment;
import com.maxsix.bingo.view.fragment.BJPKlmFragment;
import com.maxsix.bingo.view.fragment.BJPKnineFragment;
import com.maxsix.bingo.view.fragment.BJPKoneFragment;
import com.maxsix.bingo.view.fragment.BJPKsevenFragment;
import com.maxsix.bingo.view.fragment.BJPKsixFragment;
import com.maxsix.bingo.view.fragment.BJPKtenFragment;
import com.maxsix.bingo.view.fragment.BJPKthreeFragment;
import com.maxsix.bingo.view.fragment.BJPKtwoFragment;
import com.maxsix.bingo.view.fragment.BaseFragment;
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

public class BJPKActivity extends BaseActivity implements View.OnClickListener{

    private BaseFragment[] fragments;
    private BJPKeightFragment bjpKeightFragment;
    private BJPKfineFragment bjpKfineFragment;
    private BJPKfourFragment bjpKfourFragment;
    private BJPKsevenFragment bjpKsevenFragment;
    private BJPKnineFragment bjpKnineFragment;
    private BJPKsixFragment bjpKsixFragment;
    private BJPKtenFragment bjpKtenFragment;
    private BJPKthreeFragment bjpKthreeFragment;
    private BJPKGYFragment bjpkgyFragment;
    private BJPKlmFragment bjpKlmFragment;
    private BJPKoneFragment bjpKoneFragment;
    private BJPKtwoFragment bjpKtwoFragment;
    private TextView tvname;
    private com.maxsix.bingo.vo.User user;
    private int index = 0;
    public Stage currentStage;
    private Stage previousStage;
    private int currentTabIndex = 0;// 当前fragment的index
    @InjectView(R.id.nextqs)
    private TextView nextStage;
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
    @InjectView(R.id.txt_8)
    private TextView txt_8;
    @InjectView(R.id.txt_9)
    private TextView txt_9;
    @InjectView(R.id.txt_10)
    private TextView txt_10;

    private TextView[] tv = new TextView[10];

    @InjectView(R.id.btn_1)
    private Button btn_1;
    @InjectView(R.id.btn_2)
    private Button btn_2;
    @InjectView(R.id.btn_3)
    private Button btn_3;
    @InjectView(R.id.btn_4)
    private Button btn_4;
    @InjectView(R.id.btn_5)
    private Button btn_5;
    @InjectView(R.id.btn_6)
    private Button btn_6;
    @InjectView(R.id.btn_7)
    private Button btn_7;
    @InjectView(R.id.btn_8)
    private Button btn_8;
    @InjectView(R.id.btn_9)
    private Button btn_9;
    @InjectView(R.id.btn_10)
    private Button btn_10;
    @InjectView(R.id.btn_11)
    private Button btn_11;
    @InjectView(R.id.btn_12)
    private Button btn_12;

    private Button[] btns;
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

    @InjectView(R.id.remailtime)
    private RelativeLayout remailtime;

    @InjectView(R.id.openStr)
    private RelativeLayout openStr;

    @InjectView(R.id.fengpStr)
    private RelativeLayout fengpStr;

    private int hour_decade;
    private int hour_unit;
    private int min_decade;
    private int min_unit;
    private int sec_decade;
    private int sec_unit;
    // 计时器
    private Timer timer;
    private Timer timerStage;
    private Timer timerWinstatus;
    private double win =0;
    private double lose = 0;

    @InjectView(R.id.winStatus)
    private TextView winStatus;
    @InjectView(R.id.btn_rest)
    private Button btn_rest;

    @InjectView(R.id.tv_number)
    public TextView tv_number;

    @InjectView(R.id.btn_tz)
    private Button btn_tz;

    @InjectView(R.id.title)
    private TextView title;

    @InjectView(R.id.title_bar)
    private Toolbar title_bar;

    public class winStatusTask extends TimerTask {

        @Override
        public void run() {
            win = 0;
            lose = 0;
            getWinStatus();
        }
    };
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bjpk);
        com.maxsix.bingo.config.AppManager.getInstance().addActivity(this);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this);
        setSupportActionBar(title_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText("北京赛车(PK10)");
        btn_tz.setOnClickListener(this);
        btn_rest.setOnClickListener(this);
        user = com.maxsix.bingo.config.AppManager.getInstance().getUserSession();

        bjpKlmFragment = new BJPKlmFragment();
        bjpkgyFragment = new BJPKGYFragment();
        bjpKoneFragment = new BJPKoneFragment();
        bjpKtwoFragment = new BJPKtwoFragment();
        bjpKthreeFragment = new BJPKthreeFragment();
        bjpKfourFragment = new BJPKfourFragment();
        bjpKfineFragment = new BJPKfineFragment();
        bjpKsixFragment = new BJPKsixFragment();
        bjpKsevenFragment = new BJPKsevenFragment();
        bjpKeightFragment = new BJPKeightFragment();
        bjpKnineFragment = new BJPKnineFragment();
        bjpKtenFragment = new BJPKtenFragment();
        fragments =  new BaseFragment[]{bjpKlmFragment,bjpkgyFragment,bjpKoneFragment,bjpKtwoFragment,
                bjpKthreeFragment,bjpKfourFragment,bjpKfineFragment,bjpKsixFragment,
                bjpKsevenFragment,bjpKeightFragment,bjpKnineFragment,bjpKtenFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, bjpKlmFragment)
                .add(R.id.fragment_container, bjpkgyFragment)
                .add(R.id.fragment_container,bjpKoneFragment)
                .add(R.id.fragment_container,bjpKtwoFragment)
                .add(R.id.fragment_container,bjpKthreeFragment)
                .add(R.id.fragment_container,bjpKfourFragment)
                .add(R.id.fragment_container,bjpKfineFragment)
                .add(R.id.fragment_container,bjpKsixFragment)
                .add(R.id.fragment_container,bjpKsevenFragment)
                .add(R.id.fragment_container,bjpKeightFragment)
                .add(R.id.fragment_container,bjpKnineFragment)
                .add(R.id.fragment_container,bjpKtenFragment)
                .hide(bjpkgyFragment)
                .hide(bjpKoneFragment)
                .hide(bjpKtwoFragment)
                .hide(bjpKthreeFragment)
                .hide(bjpKfourFragment)
                .hide(bjpKfineFragment)
                .hide(bjpKsixFragment)
                .hide(bjpKsevenFragment)
                .hide(bjpKeightFragment).hide(bjpKnineFragment).hide(bjpKtenFragment).show(bjpKlmFragment).commit();
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_10.setOnClickListener(this);
        btn_11.setOnClickListener(this);
        btn_12.setOnClickListener(this);
        btns = new Button[12];
        btns[0] = btn_1;
        btns[1] = btn_2;
        btns[2] = btn_3;
        btns[3] = btn_4;
        btns[4] = btn_5;
        btns[5] = btn_6;
        btns[6] = btn_7;
        btns[7] = btn_8;
        btns[8] = btn_9;
        btns[9] = btn_10;
        btns[10] = btn_11;
        btns[11] = btn_12;
        tv[0] = txt_1;
        tv[1] = txt_2;
        tv[2] = txt_3;
        tv[3] = txt_4;
        tv[4] = txt_5;
        tv[5] = txt_6;
        tv[6] = txt_7;
        tv[7] = txt_8;
        tv[8] = txt_9;
        tv[9] = txt_10;
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
        Intent intent = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.bettingRecord:
                Bundle bundle = new Bundle();
                bundle.putInt("lid", 5);
                intent = new Intent(context,
                        BettingListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_stages:
                intent = new Intent(context,
                        BJPKGameRulesActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.stagepoenls:
                intent = new Intent(context,
                        BJPKStagesActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setNumber(int _number){
        tv_number.setText("共 "+_number+" 注");
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
            Utils.showLongToast(BJPKActivity.this, "请选择投注！");
        }else
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable("infos", (Serializable) baseFragment.infos);
            bundle.putString("title", "北京赛车(PK10)");
            bundle.putString("stageName",stageName);
            Intent intent = new Intent(context, CheckActivity.class);
            bundle.putSerializable("stage", currentStage);
            intent.putExtras(bundle);
            startActivity(intent);
        }
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
                case R.id.btn_1:
                    index = 0;
                    break;
                case R.id.btn_2:
                    index = 1;
                    break;
                case R.id.btn_3:
                    index = 2;
                    break;
                case R.id.btn_4:
                    index = 3;
                    break;
                case R.id.btn_5:
                    index = 4;
                    break;
                case R.id.btn_6:
                    index = 5;
                    break;
                case R.id.btn_7:
                    index = 6;
                    break;
                case R.id.btn_8:
                    index = 7;
                    break;
                case R.id.btn_9:
                    index = 8;
                    break;
                case R.id.btn_10:
                    index = 9;
                    break;
                case R.id.btn_11:
                    index = 10;
                    break;
                case R.id.btn_12:
                    index = 11;
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
    public void getProStage(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        String url = Constants.GetStages_URL+"?"+System.currentTimeMillis()+"&lid=5&status=2";
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
        String url = "http://cp.gtweixin.com/api/stages/?"+System.currentTimeMillis()+"&lid=5";
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
                        tv_min_decade.setText("0");
                        tv_min_unit.setText("0");
                        tv_sec_decade.setText("0");
                        tv_sec_unit.setText("0");
                        fengpStr.setVisibility(View.VISIBLE);
                        btn_tz.setEnabled(false);
                        btn_rest.setEnabled(false);
                        remailtime.setVisibility(View.GONE);
                        stop();
                        getCurrentStage();
                        return false;
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
        //tv_hour_decade.setText(hour_decade + "");
        //tv_hour_unit.setText(hour_unit + "");
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
                if (previousStage != null) {
                    if(currentStage == null){
                        nextStage.setText("距"+previousStage.getCurrent() + "期封盘");
                        fengpStr.setVisibility(View.VISIBLE);
                        remailtime.setVisibility(View.GONE);
                    }
                    previousStageName.setText(previousStage.getCurrent()+"期开奖号码");
                    String[] opens = previousStage.getOpened().split(",");
                    Drawable drawable = null;
                    for (int i = 0; i <opens.length; i++) {
                        int a = Integer.parseInt(opens[i]);
                        if(a ==1){
                            drawable = getResources().getDrawable(R.drawable.one);
                        }else if(a==2){
                            drawable = getResources().getDrawable(R.drawable.two);
                        }else if(a==3){
                            drawable = getResources().getDrawable(R.drawable.three);
                        }else if(a==4){
                            drawable = getResources().getDrawable(R.drawable.four);
                        }else if(a==5){
                            drawable = getResources().getDrawable(R.drawable.fine);
                        }else if(a==6){
                            drawable = getResources().getDrawable(R.drawable.six);
                        }else if(a==7){
                            drawable = getResources().getDrawable(R.drawable.seven);
                        }else if(a==8){
                            drawable = getResources().getDrawable(R.drawable.eight);
                        }else if(a==9){
                            drawable = getResources().getDrawable(R.drawable.nine);
                        }else if(a==10){
                            drawable = getResources().getDrawable(R.drawable.ten);
                        }
                        tv[i].setBackground(drawable);
                    }
                }
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
                            long min = diff % 86400000 % 3600000 / 60000;       //以分钟为单位取整
                            long seconds = diff % 86400000 % 3600000 % 60000 / 1000;   //以秒为单位取整
                            if (diff > 0 && hour ==0) {
                                fengpStr.setVisibility(View.GONE);
                                remailtime.setVisibility(View.VISIBLE);
                                btn_tz.setEnabled(true);
                                btn_tz.setBackground(getResources().getDrawable(R.color.timeselect));
                                btn_tz.setTextColor(Color.WHITE);
                                setTime((int) day, (int) hour, (int) min, (int) seconds);
                                start();
                            } else {
                                fengpStr.setVisibility(View.VISIBLE);
                                TextView tx = (TextView)fengpStr.getChildAt(0);
                                tx.setText("未到下注时间");
                                btn_tz.setBackgroundDrawable(getResources().getDrawable(
                                        R.drawable.btn_unenable_green));
                                btn_tz.setTextColor(Color.parseColor("#aaaaaa"));
                                btn_tz.setEnabled(false);
                                remailtime.setVisibility(View.GONE);
                                getCurrentStage();
                            }
                        } catch (Exception e) {
                            Log.i("TAG", e.toString());
                        }
                    }
                }else {
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
                String url = com.maxsix.bingo.config.Constants.User_BaseURL + "0/bettings/" + "?" + System.currentTimeMillis() + "&date=" + date + "&page="+currentPage+"&status=1&lid=5";
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
                String url = com.maxsix.bingo.config.Constants.User_BaseURL + "0/bettings/" + "?" + System.currentTimeMillis() + "&date=" + date + "&page="+currentPage+"&status=2&lid=5";
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    /**
     *
     *
     * @param view
     */
    public void userinfo(View view) {
        startActivity(new Intent(BJPKActivity.this, ProfileActivity.class));
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }
    @Override
    public void onPause() {
        super.onPause();
        getCurrentStage();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        getCurrentStage();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getCurrentStage();
    }
}
