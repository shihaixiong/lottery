package com.maxsix.bingo.view.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.maxsix.bingo.R;
import com.maxsix.bingo.view.fragment.BettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class BettingListActivity extends AppCompatActivity {
private int lid = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bettinglist);
        Intent intent = getIntent();
        Bundle buldle = intent.getExtras();
        lid = buldle.getInt("lid");
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(BettingsFragment.newInstance(lid,0), "待开奖");
        adapter.addFragment(BettingsFragment.newInstance(lid,1), "中奖");
        adapter.addFragment(BettingsFragment.newInstance(lid,2), "未中奖");
        adapter.addFragment(BettingsFragment.newInstance(lid,3), "和局");
        viewPager.setAdapter(adapter);
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        if (lid == 1) {
            startActivity(new Intent(BettingListActivity.this, MarxSixActivity.class));
        }else if (lid == 2) {
            startActivity(new Intent(BettingListActivity.this, CqsscActivity.class));
        }else if (lid == 3) {
            startActivity(new Intent(BettingListActivity.this, GdklcActivity.class));
        }else if (lid == 4) {
            startActivity(new Intent(BettingListActivity.this, JSSBActivity.class));
        }else if (lid == 5) {
            startActivity(new Intent(BettingListActivity.this, BJPKActivity.class));
        }
        finish();
    }
}
