package com.maxsix.bingo.config;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.maxsix.bingo.R;
import com.maxsix.bingo.view.activity.BaseActivity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

/**
 * Created by tls on 2015/8/11.
 * 如果是列表，就继承这个
 */
public class BaseListActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener {
    protected SwipyRefreshLayout swip;
    protected ListView lv;
    protected TextView msg;
    protected boolean pageEmpty = false;
    protected long page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initControl() {

    }

    protected void Refresh()
    {

    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            if (page - 1 >= 1) page--;
            else {
                swip.setRefreshing(false);
                return;
            }
            ;
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            if (!pageEmpty)
                page++;
        }
        Refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            Refresh();
        }
    }

    @Override
    protected void findViewById() {
        swip = (SwipyRefreshLayout) findViewById(R.id.swip);
        lv = (ListView) findViewById(R.id.list_supplier_quote);
        msg = (TextView) findViewById(R.id.nodata);
        swip.setOnRefreshListener(this);
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
}
