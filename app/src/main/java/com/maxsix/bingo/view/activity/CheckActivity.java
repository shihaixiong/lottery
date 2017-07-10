package com.maxsix.bingo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxsix.bingo.R;
import com.maxsix.bingo.SlideCutListView.SlideCutListView;
import com.maxsix.bingo.adpter.BettingsAdpter;
import com.maxsix.bingo.adpter.BettingscheckAdpter;
import com.maxsix.bingo.adpter.Line;
import com.maxsix.bingo.adpter.LineAdapter;
import com.maxsix.bingo.circleprogress.DonutProgress;
import com.maxsix.bingo.config.AppManager;
import com.maxsix.bingo.config.ViewHolder;
import com.maxsix.bingo.util.InjectUtil;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.vo.BettingInfo;
import com.maxsix.bingo.vo.BettingListInfo;
import com.maxsix.bingo.vo.GridList;
import com.maxsix.bingo.vo.Stage;
import com.maxsix.bingo.vo.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class CheckActivity extends BaseActivity implements SlideCutListView.RemoveListener {
    private SlideCutListView slideCutListView ;
    private LineAdapter adapter;
    private List<BettingInfo> mData;
    private List<BettingListInfo> lsData = new ArrayList<BettingListInfo>();
    private User user;
    private Stage stage;
    private String tmType;
    @InjectView(R.id.tzdonut_progress)
    private DonutProgress tzdonut_progress;

    @InjectView(R.id.checktitle)
    private TextView checktitle;

    @InjectView(R.id.check)
    private Button check;
    // 计时器
    private Timer timer;
    private int lid = -1;

    private int allRow;         //总记录数
    private int totalPage;        //总页数
    private int currentPage =1;    //当前页
    private int pageSize = 20;        //每页记录数
    private TextView txaccountremain;
    HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        InjectUtil.autoInjectView(this);
        Intent intent = getIntent();
        Bundle buldle = intent.getExtras();
        mData = (List<BettingInfo>)buldle.getSerializable("infos");
        for(BettingInfo info:mData){
            info.setBettingno(Utils.genRandomNum(10));
        }
        String title = buldle.getString("title");
        checktitle.setText(title);
        if(title.equals("六合彩")){
            lid = 1;
        }else if(title.equals("重庆时时彩")){
            lid = 2;
        }else if(title.equals("北京赛车(PK10)")){
            lid = 5;
        }else if(title.equals("江苏骰宝(快3)")){
            lid = 4;
        }else if(title.equals("广东快乐十分")){
            lid = 3;
        }
        slideCutListView = (SlideCutListView) findViewById(R.id.slideCutListView);
        slideCutListView.setRemoveListener(this);
        adapter = new LineAdapter(mData);
        slideCutListView.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(slideCutListView);
        user = com.maxsix.bingo.config.AppManager.getInstance().getUserSession();
        if(user !=null) {
            TextView txaccount = (TextView) findViewById(R.id.txaccount);
            txaccount.setText(user.getUsername());
            txaccountremain = (TextView) findViewById(R.id.txaccountremain);
            DecimalFormat df = new DecimalFormat("#.##");
            double sumwind = user.getRemain()/100.0;
            txaccountremain.setText(Double.parseDouble(df.format(sumwind))+"");
        }
        tmType = buldle.getString("stageName");
        stage = (Stage)buldle.getSerializable("stage");
        TextView title_check = (TextView)findViewById(R.id.title_check);
        TextView title_stage = (TextView)findViewById((R.id.title_stage));
        title_check.setText(stage.getCurrent()+"期数");
        title_stage.setText(tmType);
        getLS();
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

    @Override
    public void removeItem(SlideCutListView.RemoveDirection direction, int position) {
        mData.remove(adapter.getItem(position));
        adapter.notifyDataSetChanged();
        if(mData.size() ==0){
            this.finish();
        }
    }
    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }
    public void getUserRemain() {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();

            @Override
            public Boolean call() throws Exception {

                String usrInfo = http.Get(com.maxsix.bingo.config.Constants.User_BaseURL + "0/?" + System.currentTimeMillis());
                user = gson.fromJson(usrInfo, com.maxsix.bingo.vo.User.class);
                if (user != null) {
                    com.maxsix.bingo.config.AppManager.getInstance().setUserSession(user);
                    return true;
                } else {
                    return false;
                }

            }

        }, new com.maxsix.bingo.task.Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {
                if (pCallbackValue == true) {
                    txaccountremain = (TextView) findViewById(R.id.txaccountremain);
                    DecimalFormat df = new DecimalFormat("#.##");
                    double sumwind = user.getRemain() / 100.0;
                    txaccountremain.setText(Double.parseDouble(df.format(sumwind)) + "");
                }
            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {

            }
        }, false, "");

    }
    public void getUserInfo(View view) {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();

            @Override
            public Boolean call() throws Exception {

                String usrInfo = http.Get(com.maxsix.bingo.config.Constants.User_BaseURL + "0/?" + System.currentTimeMillis());
                user = gson.fromJson(usrInfo, com.maxsix.bingo.vo.User.class);
                if (user != null) {
                    com.maxsix.bingo.config.AppManager.getInstance().setUserSession(user);
                    return true;
                } else {
                    return false;
                }

            }

        }, new com.maxsix.bingo.task.Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {
                if (pCallbackValue == true) {
                    txaccountremain = (TextView) findViewById(R.id.txaccountremain);
                    DecimalFormat df = new DecimalFormat("#.##");
                    double sumwind = user.getRemain()/100.0;
                    txaccountremain.setText(Double.parseDouble(df.format(sumwind))+"");
                    tz();
                } else {
                    com.maxsix.bingo.util.Utils.showLongToast(context, "拉取用户信息失败！请重新登录");
                    Intent intent = new Intent(CheckActivity.this,
                            TelLoginActivity.class);
                    if (lid == 2) {
                        intent.putExtra("letteyType", "cqssc");
                        startActivity(intent);
                    } else if (lid == 1) {
                        intent.putExtra("letteyType", "marxsix");
                        startActivity(intent);
                    } else if (lid == 3) {
                        intent.putExtra("letteyType", "gdkl");
                        startActivity(intent);
                    } else if (lid == 4) {
                        intent.putExtra("letteyType", "jssb");
                        startActivity(intent);
                    } else if (lid == 5) {
                        intent.putExtra("letteyType", "bjpk");
                        startActivity(intent);
                    }
                    finish();
                }

            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {

            }
        }, false, "");

    }
    public void tz(){
        tzdonut_progress.setVisibility(View.VISIBLE);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tzdonut_progress.setProgress(tzdonut_progress.getProgress() + 1);
                    }
                });
            }
        }, 1000, 100);
        int sumTZ =0;
        boolean status = true;
        int limitPay = AppManager.getInstance().getLimitPay();
        for(BettingInfo info:mData){
            if(info.getTimes() ==0){
                com.maxsix.bingo.util.Utils.showLongToast(context, "亲,请确认所有投注项金额填写并且金额大于零");
                tzdonut_progress.setVisibility(View.GONE);
                timer.cancel();
                status = false;
                break;
            }else if(info.getTimes() > limitPay){
                com.maxsix.bingo.util.Utils.showLongToast(context, "亲,单注投注不能大于"+limitPay+"元哦");
                tzdonut_progress.setVisibility(View.GONE);
                timer.cancel();
                status = false;
                break;
            } else{
                sumTZ += info.getTimes();
            }
            int sum = 0;
            if(lsData != null && lsData.size() > 0) {
                for (BettingListInfo info1 : lsData) {
                    if (info.getCode().equals(info1.getCode())) {
                        sum += info1.getTimes();
                    }
                }
            }
            if((sum+info.getTimes()) > limitPay){
                com.maxsix.bingo.util.Utils.showLongToast(context, "亲,当前期单注投注金额总和不能大于"+limitPay+"元哦");
                tzdonut_progress.setVisibility(View.GONE);
                timer.cancel();
                status = false;
                break;
            }
        }
        if(status) {
            if (sumTZ == 0) {
                com.maxsix.bingo.util.Utils.showLongToast(context, "请填写金额");
            } else if (sumTZ > 0 && sumTZ*100 < user.getRemain()) {
                check.setEnabled(false);
                if (mData != null && mData.size() > 0) {
                    for (int a = 0; a < mData.size(); a++) {
                        postBettingserive(mData.get(a), a);
                    }
                }
            } else {
                tzdonut_progress.setVisibility(View.GONE);
                timer.cancel();
                com.maxsix.bingo.util.Utils.showLongToast(context, "余额不足,请充值");
                startActivity(new Intent(CheckActivity.this, BalanceActivity.class));
            }
        }
    }

    private void postBettingserive(final com.maxsix.bingo.vo.BettingInfo _bettinginfo,final Integer _next) {

        doAsync(new Callable<Boolean>() {
            Gson gson = new Gson();

            @Override
            public Boolean call() throws Exception {
                String info = http.Post(com.maxsix.bingo.config.Constants.User_BaseURL + "0/bettings/", gson.toJson(_bettinginfo));
                com.maxsix.bingo.vo.DataResult result = gson.fromJson(info, com.maxsix.bingo.vo.DataResult.class);
                if (result != null && result.getError() == null) {
                    return true;
                } else {
                    return false;
                }
            }

        }, new com.maxsix.bingo.task.Callback<Boolean>() {
            @Override
            public void onCallback(Boolean pCallbackValue) {
                if (pCallbackValue == true) {
                    if (_next == mData.size() - 1) {
                        tzdonut_progress.setVisibility(View.GONE);
                        Bundle bundle = new Bundle();
                        bundle.putInt("lid", lid);
                        Intent intent = new Intent(context,
                                BettingListActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        getUserRemain();
                        finish();
                    }
                } else {
                    tzdonut_progress.setVisibility(View.GONE);
                    Utils.showLongToast(context, "投注失败，请稍候再投注或是超时要重新登录");
                    finish();
                    if (lid == 1) {
                        startActivity(new Intent(CheckActivity.this, MarxSixActivity.class));
                    }else if (lid == 2) {
                        startActivity(new Intent(CheckActivity.this, CqsscActivity.class));
                    }else if (lid == 3) {
                        startActivity(new Intent(CheckActivity.this, GdklcActivity.class));
                    }else if (lid == 4) {
                        startActivity(new Intent(CheckActivity.this, JSSBActivity.class));
                    }else if (lid == 5) {
                        startActivity(new Intent(CheckActivity.this, BJPKActivity.class));
                    }
                }
            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {
            @Override
            public void onCallback(Exception pCallbackValue) {
                tzdonut_progress.setVisibility(View.GONE);
                Utils.showLongToast(context, "投注失败，请稍候再投注或是超时要重新登录");
                finish();
                if (lid == 1) {
                    startActivity(new Intent(CheckActivity.this, MarxSixActivity.class));
                }else if (lid == 2) {
                    startActivity(new Intent(CheckActivity.this, CqsscActivity.class));
                }else if (lid == 3) {
                    startActivity(new Intent(CheckActivity.this, GdklcActivity.class));
                }else if (lid == 4) {
                    startActivity(new Intent(CheckActivity.this, JSSBActivity.class));
                }else if (lid == 5) {
                    startActivity(new Intent(CheckActivity.this, BJPKActivity.class));
                }
            }
        }, false, "");
    }
    class CheckAdpter extends BaseAdapter {
        private List<BettingInfo> mData;
        private View v;
        private Context mContext;

        public CheckAdpter(Context context, List<BettingInfo> data) {
            this.mContext = context;
            this.mData = data;
        }
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final BettingInfo info = mData.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.listview_item, null);
            }
            TextView jiaoyNumber = com.maxsix.bingo.config.ViewHolder.get(convertView,
                    R.id.letteyName);
            EditText jine = ViewHolder.get(convertView,
                    R.id.jine);
            //jine.setText(info.getTimes()+"");
            jine.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String tempValue = s.toString();
                    if (!tempValue.equals("")) {
                        hashMap.put(position, s.toString());
                        info.setTimes(Integer.parseInt(tempValue));
                    } else {
                        info.setTimes(0);
                    }
                }
            });
            if(hashMap.get(position) != null){
                jine.setText(hashMap.get(position));
            }
            jiaoyNumber.setText(mData.get(position).getName());
            return convertView;
        }
    }
    private ArrayList<Line> createLines() {
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Line line = new Line();
            line.setNum(i);
            lines.add(line);
        }
        return lines;
    }

    public void getLS(){
        doAsync(new Callable<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo>>() {

            @Override
            public com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo> call() throws Exception {
                Gson gson = new Gson();
                String url = com.maxsix.bingo.config.Constants.User_BaseURL + "0/bettings/" + "?" + System.currentTimeMillis() + "&page=" + currentPage + "&status=0&lid="+lid;
                Log.i("rul", url);
                String json = http.Get(url);
                com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo> items = gson.fromJson(json, new TypeToken<GridList<BettingListInfo>>() {
                }.getType());
                return items;
            }

        }, new com.maxsix.bingo.task.Callback<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo>>() {

            @Override
            public void onCallback(com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo> pCallbackValue) {
                if (pCallbackValue != null && pCallbackValue.getResults() != null && pCallbackValue.getResults().size() > 0) {
                    allRow = (int)pCallbackValue.getCount();
                    for(BettingListInfo info:pCallbackValue.getResults()){
                        lsData.add(info);
                    }
                    totalPage = allRow % pageSize == 0 ? allRow/pageSize : allRow/pageSize+1;
                    if(totalPage > currentPage){
                        currentPage++;
                        getLS();
                    }else{
                        currentPage = 1;
                    }
            }
            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {

            }
        }, false, "");
    }

}
