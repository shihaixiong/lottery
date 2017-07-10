package com.maxsix.bingo.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxsix.bingo.R;
import com.maxsix.bingo.adpter.BJPKStagesAdpter;
import com.maxsix.bingo.adpter.CQSSCStagesAdpter;
import com.maxsix.bingo.adpter.GDKLSFStagesAdpter;
import com.maxsix.bingo.adpter.JSSBStagesAdpter;
import com.maxsix.bingo.adpter.MarxSixStagesAdpter;
import com.maxsix.bingo.pulltorefresh.PullToRefreshLayout;
import com.maxsix.bingo.service.ConstValue;
import com.maxsix.bingo.util.InjectUtil;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.view.activity.JSSBActivity;
import com.maxsix.bingo.view.activity.SscStagesActivity;
import com.maxsix.bingo.vo.GridList;
import com.maxsix.bingo.vo.Stage;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class WinFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private CQSSCStagesAdpter sscadapter;
    private List<Stage> sscData;

    private BJPKStagesAdpter bjadapter;
    private List<Stage> bjData;

    private JSSBStagesAdpter jsadapter;
    private List<Stage> jsData;

    private GDKLSFStagesAdpter gdadapter;
    private List<Stage> gdData;

    private MarxSixStagesAdpter marsixadapter;
    private List<Stage> marsixData;


    @InjectView(R.id.lv_ssc)
    private ListView lv_ssc;

    @InjectView(R.id.lv_bj)
    private ListView lv_bj;

    @InjectView(R.id.lv_js)
    private ListView lv_js;

    @InjectView(R.id.lv_gd)
    private ListView lv_gd;

    @InjectView(R.id.lv_marsix)
    private ListView lv_marsix;

    @InjectView(R.id.sscProgressBar)
    private ContentLoadingProgressBar sscProgressBar;

    @InjectView(R.id.bjProgressBar)
    private ContentLoadingProgressBar bjProgressBar;

    @InjectView(R.id.jsProgressBar)
    private ContentLoadingProgressBar jsProgressBar;

    @InjectView(R.id.gdProgressBar)
    private ContentLoadingProgressBar gdProgressBar;

    @InjectView(R.id.marsixProgressBar)
    private ContentLoadingProgressBar marsixProgressBar;

    @InjectView(R.id.swip_ssc)
    private PullToRefreshLayout swip_ssc;

    @InjectView(R.id.swip_bj)
    private PullToRefreshLayout swip_bj;

    @InjectView(R.id.swip_js)
    private PullToRefreshLayout swip_js;

    @InjectView(R.id.swip_gd)
    private PullToRefreshLayout swip_gd;

    @InjectView(R.id.swip_marsix)
    private PullToRefreshLayout swip_marsix;

    private Timer timer;

    public class StageTask extends TimerTask {

        @Override
        public void run() {
            getkj(2);
            getkj(5);
            getkj(4);
            getkj(3);
            getkj(1);
        }
    };
    public WinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WinFragment newInstance(String param1, String param2) {
        WinFragment fragment = new WinFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_win, container, false);
        InjectUtil.autoInjectView(this, view);
        getkj(2);
        getkj(5);
        getkj(4);
        getkj(3);
        getkj(1);
        if(timer == null) {
            timer = new Timer();
            timer.schedule(new StageTask(), 0, 300000);
        }
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    protected void getkj(final int _lid)  {
        //sscProgressBar.setVisibility(View.VISIBLE);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        String url = "";
        if(_lid == 1) {
            url = com.maxsix.bingo.config.Constants.GetStages_URL + "?" + System.currentTimeMillis() + "&page=1&status=1&lid=" + _lid;
        }else{
            url = com.maxsix.bingo.config.Constants.GetStages_URL + "?" + System.currentTimeMillis() + "&page=1&status=2&lid=" + _lid;
        }
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //sscProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String content = response.body().string();
                Gson gson = new Gson();
                try {
                    GridList<Stage> items = gson.fromJson(content, new TypeToken<GridList<Stage>>() {
                    }.getType());
                    if (items != null && items.getResults() != null && items.getResults().size() > 0) {
                        if(_lid ==2) {
                            sscData = items.getResults();
                            sscHandler.sendEmptyMessage(0);
                        }else if(_lid == 5){
                            bjData = items.getResults();
                            sscHandler.sendEmptyMessage(1);
                        }else if(_lid == 4){
                            jsData = items.getResults();
                            sscHandler.sendEmptyMessage(2);
                        }else if(_lid == 3){
                            gdData = items.getResults();
                            sscHandler.sendEmptyMessage(3);
                        }else if(_lid == 1){
                            marsixData = items.getResults();
                            sscHandler.sendEmptyMessage(4);
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
    }
    Handler sscHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(msg.what == 0)
            {
                sscProgressBar.setVisibility(View.GONE);
                if(sscData !=null && sscData.size()>0) {
                    sscadapter = new CQSSCStagesAdpter(getActivity(), sscData.subList(0, 5));
                    lv_ssc.setAdapter(sscadapter);
                    Utils.setListViewHeightBasedOnChildren(lv_ssc);
                }
            }else if(msg.what ==1){
                bjProgressBar.setVisibility(View.GONE);
                if(bjData !=null && bjData.size()>0) {
                    bjadapter = new BJPKStagesAdpter(getActivity(), bjData.subList(0, 5));
                    lv_bj.setAdapter(bjadapter);
                    Utils.setListViewHeightBasedOnChildren(lv_bj);
                }
            }else if(msg.what ==2){
                jsProgressBar.setVisibility(View.GONE);
                if(jsData !=null && jsData.size()>0) {
                    jsadapter = new JSSBStagesAdpter(getActivity(), jsData.subList(0, 5));
                    lv_js.setAdapter(jsadapter);
                    Utils.setListViewHeightBasedOnChildren(lv_js);
                }
            }else if(msg.what ==3){
                gdProgressBar.setVisibility(View.GONE);
                if(gdData !=null && gdData.size()>0) {
                    gdadapter = new GDKLSFStagesAdpter(getActivity(), gdData.subList(0, 5));
                    lv_gd.setAdapter(gdadapter);
                    Utils.setListViewHeightBasedOnChildren(lv_gd);
                }
            }else if(msg.what ==4){
                marsixProgressBar.setVisibility(View.GONE);
                if(marsixData !=null && marsixData.size()>0) {
                    marsixadapter = new MarxSixStagesAdpter(getActivity(), marsixData.subList(0, 5));
                    lv_marsix.setAdapter(marsixadapter);
                    Utils.setListViewHeightBasedOnChildren(lv_marsix);
                }
            }
        }
    };
}
