package com.maxsix.bingo.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.config.AppManager;
import com.maxsix.bingo.util.InjectUtil;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.view.activity.BJPKActivity;
import com.maxsix.bingo.view.activity.BaseActivity;
import com.maxsix.bingo.view.activity.CheckActivity;
import com.maxsix.bingo.view.activity.CqsscActivity;
import com.maxsix.bingo.view.activity.GdklcActivity;
import com.maxsix.bingo.view.activity.JSSBActivity;
import com.maxsix.bingo.view.activity.MarxSixActivity;
import com.maxsix.bingo.view.activity.ProfileActivity;
import com.maxsix.bingo.vo.User;

public class FragmentHome extends Fragment implements View.OnClickListener {

    @InjectView(R.id.rl_marksix)
    private RelativeLayout marksix;

    @InjectView(R.id.rl_ssc)
    private RelativeLayout ssc;

    @InjectView(R.id.rl_js)
    private RelativeLayout js;

    @InjectView(R.id.rl_gd)
    private RelativeLayout gd;

    @InjectView(R.id.rl_bj)
    private RelativeLayout bj;

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_home, container, false);
        InjectUtil.autoInjectView(this,view);
        marksix.setOnClickListener(this);
        ssc.setOnClickListener(this);
        js.setOnClickListener(this);
        gd.setOnClickListener(this);
        bj.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
        User user = AppManager.getInstance().getUserSession();
        Intent intent = null;
        if(user == null){
            Utils.showLongToast(getActivity(), "请先注册登录！");
            return;
        }
        switch (v.getId()){
            case R.id.rl_marksix:
                intent = new Intent(getContext(), MarxSixActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_ssc:
                intent = new Intent(getContext(), CqsscActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_js:
                intent = new Intent(getContext(), JSSBActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_gd:
                intent = new Intent(getContext(), GdklcActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_bj:
                intent = new Intent(getContext(), BJPKActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
