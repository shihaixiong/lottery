package com.maxsix.bingo.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.config.AppManager;
import com.maxsix.bingo.util.InjectView;
import com.maxsix.bingo.util.Utils;
import com.maxsix.bingo.view.activity.AlipaySettingActivity;
import com.maxsix.bingo.view.activity.BalanceActivity;
import com.maxsix.bingo.view.activity.HelpActivity;
import com.maxsix.bingo.view.activity.HomeActivity;
import com.maxsix.bingo.view.activity.InvitecodeActivity;
import com.maxsix.bingo.view.activity.LockLoginActivity;
import com.maxsix.bingo.view.activity.LockLoginToHomeActivity;
import com.maxsix.bingo.view.activity.LoginActivity;
import com.maxsix.bingo.view.activity.ModifyPasswordActivity;
import com.maxsix.bingo.view.activity.PaymentListActivity;
import com.maxsix.bingo.view.activity.RealNameSettingActivity;
import com.maxsix.bingo.view.activity.SettingLockActivity;
import com.maxsix.bingo.view.activity.TelLoginActivity;
import com.maxsix.bingo.view.activity.TelSettingActivity;
import com.maxsix.bingo.view.activity.WXSettingActivity;
import com.maxsix.bingo.vo.User;
import com.maxsix.bingo.widget.ImageViewWithCache;

import java.net.MalformedURLException;
import java.net.URL;

public class SettingFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    @InjectView(R.id.txt_realName)
    private TextView txt_realName;

    @InjectView(R.id.txt_ali)
    private TextView txt_ali;

    @InjectView(R.id.txt_wx)
    private TextView txt_wx;

    @InjectView(R.id.txt_invitecode)
    private TextView txt_invitecode;

    @InjectView(R.id.txt_modifyPassword)
    private TextView txt_modifyPassword;

    @InjectView(R.id.txt_login)
    private TextView txt_login;

    @InjectView(R.id.txt_zijin)
    private TextView txt_zijin;

    @InjectView(R.id.btnexit)
    private Button btnexit;

    @InjectView(R.id.txt_version)
    private TextView txt_version;

    @InjectView(R.id.txt_phone)
    private  TextView txt_phone;

    @InjectView(R.id.rl_phone)
    private RelativeLayout rl_phone;

    @InjectView(R.id.txt_lock)
    private TextView txt_lock;

    @InjectView(R.id.avatar)
    private ImageViewWithCache avatar;

    @InjectView(R.id.tv_phone)
    private TextView tv_phone;

    @InjectView(R.id.tx_version)
    private TextView tx_version;

    @InjectView(R.id.txt_paylist)
    private TextView txt_paylist;

    @InjectView(R.id.txt_service)
    private TextView txt_service;

    User user = AppManager.getInstance().getUserSession();
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;

    private Bundle msavedInstanceState;
    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msavedInstanceState = savedInstanceState;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        com.maxsix.bingo.util.InjectUtil.autoInjectView(this, view);

        Drawable vdrawable= getResources().getDrawable(R.drawable.right);
        vdrawable.setBounds(0, 0, vdrawable.getMinimumWidth(), vdrawable.getMinimumHeight());

        txt_realName.setOnClickListener(this);
        Drawable mdrawable= getResources().getDrawable(R.drawable.ico_name);
        mdrawable.setBounds(0, 0, mdrawable.getMinimumWidth()/2, mdrawable.getMinimumHeight()/2);
        txt_realName.setCompoundDrawables(mdrawable,null,vdrawable,null);

        txt_lock.setOnClickListener(this);
        Drawable ldrawable= getResources().getDrawable(R.drawable.ico_gesture_pattern);
        ldrawable.setBounds(0, 0, ldrawable.getMinimumWidth()/2, ldrawable.getMinimumHeight()/2);
        txt_lock.setCompoundDrawables(ldrawable,null,vdrawable,null);

        txt_ali.setOnClickListener(this);
        Drawable adrawable= getResources().getDrawable(R.drawable.ico_card);
        adrawable.setBounds(0, 0, adrawable.getMinimumWidth()/2, adrawable.getMinimumHeight()/2);
        txt_ali.setCompoundDrawables(adrawable,null,vdrawable,null);

        txt_wx.setOnClickListener(this);
        Drawable wdrawable= getResources().getDrawable(R.drawable.ico_alipay);
        wdrawable.setBounds(0, 0, wdrawable.getMinimumWidth()/3, wdrawable.getMinimumHeight()/3);
        txt_wx.setCompoundDrawables(wdrawable,null,vdrawable,null);

        txt_invitecode.setOnClickListener(this);
        Drawable idrawable= getResources().getDrawable(R.drawable.ico_invitation_code);
        idrawable.setBounds(0, 0, idrawable.getMinimumWidth()/2, idrawable.getMinimumHeight()/2);
        txt_invitecode.setCompoundDrawables(idrawable,null,vdrawable,null);

        txt_modifyPassword.setOnClickListener(this);
        Drawable odrawable= getResources().getDrawable(R.drawable.ico_password);
        odrawable.setBounds(0, 0, odrawable.getMinimumWidth()/2, odrawable.getMinimumHeight()/2);
        txt_modifyPassword.setCompoundDrawables(odrawable,null,vdrawable,null);

        rl_phone.setOnClickListener(this);

        Drawable pdrawable= getResources().getDrawable(R.drawable.ico_phone_number);
        pdrawable.setBounds(0, 0, pdrawable.getMinimumWidth()/2, pdrawable.getMinimumHeight()/2);
        tv_phone.setCompoundDrawables(pdrawable,null,null,null);

        avatar.setOnClickListener(this);
        txt_login.setOnClickListener(this);

        txt_zijin.setOnClickListener(this);
        Drawable zdrawable= getResources().getDrawable(R.drawable.ico_money);
        zdrawable.setBounds(0, 0, zdrawable.getMinimumWidth()/2, zdrawable.getMinimumHeight()/2);
        txt_zijin.setCompoundDrawables(zdrawable,null,vdrawable,null);

        txt_service.setOnClickListener(this);
        Drawable serdrawable= getResources().getDrawable(R.drawable.user_not_login_avatar);
        serdrawable.setBounds(0, 0, serdrawable.getMinimumWidth()/9, serdrawable.getMinimumHeight()/9);
        txt_service.setCompoundDrawables(serdrawable,null,vdrawable,null);

        txt_paylist.setOnClickListener(this);
        Drawable paydrawable= getResources().getDrawable(R.drawable.ico_money);
        paydrawable.setBounds(0, 0, paydrawable.getMinimumWidth()/2, paydrawable.getMinimumHeight()/2);
        txt_paylist.setCompoundDrawables(paydrawable,null,vdrawable,null);

        Drawable vedrawable= getResources().getDrawable(R.drawable.ico_edition);
        vedrawable.setBounds(0, 0, vedrawable.getMinimumWidth()/2, vedrawable.getMinimumHeight()/2);
        tx_version.setCompoundDrawables(vedrawable,null,null,null);

        if(user != null){
            txt_phone.setText(user.getTel());
        }
        txt_version.setText(AppManager.getInstance().getVersion());
        sp = getContext().getSharedPreferences("user", Activity.MODE_PRIVATE);
        ed = sp.edit();
        if(user != null){
            txt_login.setText("注销");
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_login || v.getId() == R.id.avatar) {
            if (user != null) {
                //AppManager.getInstance().AppExit(this.getContext());
//                ed.remove("username");
//                ed.remove("password");
//                ed.commit();
//                if (user != null) {
                AppManager.getInstance().setUserSession(null);
                user = null;
//                }
//                onCreate(msavedInstanceState);
                txt_login.setText("登录/注册");
                txt_phone.setText("");
            } else {
                sp = getContext().getSharedPreferences("user", Activity.MODE_PRIVATE);
                String username = sp.getString("username", "");
                if(username !="") {
                    Utils.start_Activity(getActivity(), LockLoginToHomeActivity.class);
                }else{
                    Utils.start_Activity(getActivity(), TelLoginActivity.class);
                }
            }
        } else{
            if(user == null)
                return;
            switch (v.getId()) {
                case R.id.txt_zijin:
                    Utils.start_Activity(getActivity(), BalanceActivity.class);
                    break;
                case R.id.txt_realName:
                    Utils.start_Activity(getActivity(), RealNameSettingActivity.class);
                    break;
                case R.id.txt_ali:
                    Utils.start_Activity(getActivity(), AlipaySettingActivity.class);
                    break;
                case R.id.txt_wx:
                    Utils.start_Activity(getActivity(), WXSettingActivity.class);
                    break;
                case R.id.txt_invitecode:
                    Utils.start_Activity(getActivity(), InvitecodeActivity.class);
                    break;
                case R.id.txt_modifyPassword:
                    Utils.start_Activity(getActivity(), ModifyPasswordActivity.class);
                    break;
                case R.id.rl_phone:
                    Utils.start_Activity(getActivity(), TelSettingActivity.class);
                    break;
                case R.id.txt_paylist:
                    Utils.start_Activity(getActivity(), PaymentListActivity.class);
                    break;
                case R.id.txt_lock:
                    com.maxsix.bingo.util.Utils.start_Activity(getActivity(), SettingLockActivity.class);
                    break;
                case R.id.txt_service:
                    com.maxsix.bingo.util.Utils.start_Activity(getActivity(), HelpActivity.class);
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        user = AppManager.getInstance().getUserSession();
        if(user!=null){
            txt_login.setText("注销");
        }
    }
}
