package com.maxsix.bingo.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxsix.bingo.adpter.PaymentsAdpter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.text.DecimalFormat;
import java.util.concurrent.Callable;


public class PaymentsFragment extends BaseFragment {

    protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
    private PaymentsAdpter adapter;
    private TextView txt_noRecord;
    private TextView imgLoading;
    private int mathod = -1;
    private TextView txt_sumMoney;
    public  static PaymentsFragment newInstance(int _mathod) {
        PaymentsFragment newFragment = new PaymentsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mathod", _mathod);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bettings, container, false);
        imgLoading = (TextView)view.findViewById(R.id.imgLoading);
        imgLoading.setVisibility(View.VISIBLE);
        txt_sumMoney = (TextView)view.findViewById(R.id.txt_sumMoney);
        txt_noRecord = (TextView)view.findViewById(R.id.txt_noRecord);
        findViewById(view);
        Bundle args = getArguments();
        if (args != null) {
            mathod = args.getInt("mathod");
        }
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
        doAsync(new Callable<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.PaymentListInfo>>() {

            @Override
            public com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.PaymentListInfo> call() throws Exception {
                Gson gson = new Gson();
                String json = http.Get(com.maxsix.bingo.config.Constants.User_BaseURL+"0/payments/" + "?"+System.currentTimeMillis()+"&page=" + page+"&method="+mathod);
                com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.PaymentListInfo> items = gson.fromJson(json, new TypeToken<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.PaymentListInfo>>() {
                }.getType());
                return items;
            }

        }, new com.maxsix.bingo.task.Callback<com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.PaymentListInfo>>() {

            @Override
            public void onCallback(com.maxsix.bingo.vo.GridList<com.maxsix.bingo.vo.PaymentListInfo> pCallbackValue) {
                if (pCallbackValue != null && pCallbackValue.getResults() != null && pCallbackValue.getResults().size() >0) {
                    adapter = new PaymentsAdpter(getActivity(), pCallbackValue.getResults());
                    DecimalFormat df = new DecimalFormat("#.##");
                    double money = 0.00;
                    if(mathod == 1) {
                        money = Double.parseDouble(df.format(pCallbackValue.getExtend().getRecharge() / 100.00));
                        txt_sumMoney.setText("￥"+money);
                    }else if(mathod == 2){
                        money = Double.parseDouble(df.format(pCallbackValue.getExtend().getRedraw() / 100.00));
                        txt_sumMoney.setText("￥"+money);
                    }else if(mathod == 3){
                        money = Double.parseDouble(df.format(pCallbackValue.getExtend().getFee() / 100.00));
                        txt_sumMoney.setText("￥"+money);
                    }else if(mathod == 4){
                        money = Double.parseDouble(df.format(pCallbackValue.getExtend().getPay() / 100.00));
                        txt_sumMoney.setText("￥"+money);
                    }else if(mathod == 5){
                        money = Double.parseDouble(df.format(pCallbackValue.getExtend().getWin() / 100.00));
                        txt_sumMoney.setText("￥"+money);
                    }
                    lv.setAdapter(adapter);
                    swip.setRefreshing(false);
                    imgLoading.setVisibility(View.GONE);
                }else{
                    imgLoading.setVisibility(View.GONE);
                    swip.setRefreshing(false);
                    txt_noRecord.setVisibility(View.VISIBLE);
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
