package com.maxsix.bingo.view.fragment;

import android.support.v4.app.Fragment;
import android.widget.ListView;
import android.widget.TextView;
import com.maxsix.bingo.vo.BettingInfo;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by shihaixiong on 2016/7/13.
 */
public abstract class BaseFragment extends Fragment implements SwipyRefreshLayout.OnRefreshListener {
    protected SwipyRefreshLayout swip;
    protected ListView lv;
    protected TextView msg;
    protected boolean pageEmpty = false;
    protected long page = 1;
    public int selectNumberValue = 0;
    public List<BettingInfo> infos = new ArrayList<BettingInfo>();
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
    protected void Refresh()
    {

    }
    protected <T> void doAsync(final Callable<T> pCallable, final com.maxsix.bingo.task.Callback<T> pCallback, final com.maxsix.bingo.task.Callback<Exception> pExceptionCallback, final boolean showDialog, String message) {
        com.maxsix.bingo.task.EMobileTask.doAsync(getActivity(), null, message, pCallable, pCallback, pExceptionCallback, false, showDialog);
    }
    public abstract void resetValue();
}
