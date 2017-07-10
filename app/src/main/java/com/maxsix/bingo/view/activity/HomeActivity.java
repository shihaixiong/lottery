package com.maxsix.bingo.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.view.fragment.FragmentHome;
import com.maxsix.bingo.view.fragment.SettingFragment;
import com.maxsix.bingo.view.fragment.WinFragment;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentHome fragmentHome;
    private WinFragment fragmentBettingList;
    private SettingFragment fragmentProfile;
    private TextView home;
    private TextView open;
    private TextView myaccount;
    private TextView[] txtBtn;

    private TextView user;
    private TextView title;
    private TextView message;
    private int index = 0;
    private int currentTabIndex = 0;// 当前fragment的index
    private android.support.v4.app.Fragment[] fragments;

    public static final String TAG = AppCompatActivity.class.getSimpleName();

    private int[] currentTabIndexArray = {R.drawable.ico_home,R.drawable.ico_win,R.drawable.ico_myaccount};
    private int[] indexArray = {R.drawable.ico_homehl,R.drawable.ico_winhl,R.drawable.ico_myaccounthl};
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        title = (TextView)findViewById(R.id.title);
        home = (TextView)findViewById(R.id.txthome);
        Drawable drawableHome= getResources().getDrawable(R.drawable.ico_homehl);
        drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth()/2, drawableHome.getMinimumHeight()/2);
        home.setCompoundDrawables(null,drawableHome,null,null);

        open = (TextView)findViewById(R.id.txtWin);
        Drawable drawableUser= getResources().getDrawable(R.drawable.ico_win);
        drawableUser.setBounds(0, 0, drawableUser.getMinimumWidth()/2, drawableUser.getMinimumHeight()/2);
        open.setCompoundDrawables(null,drawableUser,null,null);

        myaccount = (TextView)findViewById(R.id.txtAccount);
        Drawable drawableProject= getResources().getDrawable(R.drawable.ico_myaccount);
        drawableProject.setBounds(0, 0, drawableProject.getMinimumWidth()/2, drawableProject.getMinimumHeight()/2);
        myaccount.setCompoundDrawables(null,drawableProject,null,null);

        fragmentHome = new FragmentHome();
        fragmentBettingList = new WinFragment();
        fragmentProfile = new SettingFragment();
        fragments = new android.support.v4.app.Fragment[]{fragmentHome,fragmentBettingList,fragmentProfile};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragmentHome)
                .add(R.id.fragment_container, fragmentBettingList)
                .add(R.id.fragment_container, fragmentProfile)
                .hide(fragmentBettingList)
                .hide(fragmentProfile)
                .show(fragmentHome).commit();
        home.setOnClickListener(this);
        open.setOnClickListener(this);
        myaccount.setOnClickListener(this);
        txtBtn = new TextView[3];
        txtBtn[0] = home;
        txtBtn[1] = open;
        txtBtn[2] = myaccount;

    }
    @Override
    public void onClick(View view) {
        Drawable drawableCurrent;
        Drawable drawableIndex;
        switch (view.getId()) {
            case R.id.txthome:
               index = 0;
                title.setText("购彩大厅");
                break;
            case R.id.txtWin:
                index = 1;
                title.setText("最新开奖信息");
                break;
            case R.id.txtAccount:
                index = 2;
                title.setText("个人中心");
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
//        txtBtn[currentTabIndex].setTextColor(getResources().getColor(R.color.menu_normal));
        drawableCurrent = getResources().getDrawable(currentTabIndexArray[currentTabIndex]);
        drawableCurrent.setBounds(0,0,drawableCurrent.getMinimumWidth()/2,drawableCurrent.getMinimumHeight()/2);
        txtBtn[currentTabIndex].setCompoundDrawables(null,drawableCurrent,null,null);

//        txtBtn[index].setTextColor(getResources().getColor(R.color.menu_presses));
        drawableIndex = getResources().getDrawable(indexArray[index]);
        drawableIndex.setBounds(0,0,drawableIndex.getMinimumWidth()/2,drawableIndex.getMinimumHeight()/2);
        txtBtn[index].setCompoundDrawables(null,drawableIndex,null,null);
        currentTabIndex = index;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getProject();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//    @Override
//    public void onBackPressed() {
//        moveTaskToBack(false);
//    }
}
