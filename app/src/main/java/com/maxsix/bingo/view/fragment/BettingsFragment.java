package com.maxsix.bingo.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxsix.bingo.adpter.BettingsAdpter;
import com.maxsix.bingo.vo.BettingListInfo;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.text.DecimalFormat;
import java.util.concurrent.Callable;

public class BettingsFragment extends BaseFragment {

    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
    private BettingsAdpter adapter;
    private TextView txt_noRecord;
    private TextView imgLoading;
    private int lid = -1;
    private int status = -1;
    private TextView txt_sumMoney;
    public static BettingsFragment newInstance(int _lid,int _status) {
        BettingsFragment newFragment = new BettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("lid", _lid);
        bundle.putInt("status", _status);
        newFragment.setArguments(bundle);
        return newFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bettings, container, false);
        imgLoading = (TextView)view.findViewById(R.id.imgLoading);
        txt_noRecord = (TextView)view.findViewById(R.id.txt_noRecord);
        txt_sumMoney = (TextView)view.findViewById(R.id.txt_sumMoney);
        findViewById(view);
        Bundle args = getArguments();
        if (args != null) {
            lid = args.getInt("lid");
            status = args.getInt("status");
        }
            imgLoading.setVisibility(View.VISIBLE);
        Refresh();
        return view;
    }
    protected void findViewById(View view) {
        swip = (SwipyRefreshLayout) view.findViewById(R.id.swip);
        lv = (ListView) view.findViewById(R.id.list_supplier_quote);
        msg = (TextView) view.findViewById(R.id.nodata);
        swip.setOnRefreshListener(this);
    }
    @Override
    public void Refresh(){
        doAsync(new Callable<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo>>() {

            @Override
            public com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.BettingListInfo> call() throws Exception {
                Gson gson = new Gson();
                String url = com.maxsix.bingo.config.Constants.User_BaseURL + "0/bettings/" + "?" + System.currentTimeMillis() + "&page=" + page + "&status="+status+"&lid="+lid;
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
                    adapter = new BettingsAdpter(getActivity(), pCallbackValue.getResults());
                    DecimalFormat df = new DecimalFormat("#.##");
                    double money = 0.00;
                    String moneyStr = "";
                    if(status == 1) {
                        money = Double.parseDouble(df.format(pCallbackValue.getExtend().getWin() / 100.00));
                        moneyStr = "赢总金额："+money;
                    }else if(status == 0){
                        money = Double.parseDouble(df.format(pCallbackValue.getExtend().getWait() / 100.00));
                        moneyStr = "下注总金额："+money+"   下注数："+pCallbackValue.getCount();
                    }else if(status == 2){
                        money = Double.parseDouble(df.format(pCallbackValue.getExtend().getLost() / 100.00));
                        moneyStr = "输总金额："+money;
                    }else{
                        txt_sumMoney.setVisibility(View.GONE);
                    }
                    txt_sumMoney.setText(moneyStr);
                    lv.setAdapter(adapter);
                    swip.setRefreshing(false);
                    imgLoading.setVisibility(View.GONE);
                } else {
                    swip.setRefreshing(false);
                    imgLoading.setVisibility(View.GONE);
                    //txt_noRecord.setVisibility(View.VISIBLE);
                }
            }
        }, new com.maxsix.bingo.task.Callback<Exception>() {

            @Override
            public void onCallback(Exception pCallbackValue) {
                swip.setRefreshing(false);
                imgLoading.setVisibility(View.GONE);
            }
        }, false, "");
    }

    @Override
    public void resetValue() {

    }
}
