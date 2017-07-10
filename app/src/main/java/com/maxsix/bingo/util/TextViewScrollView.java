package com.maxsix.bingo.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by shihaixiong on 2016/7/12.
 */
public class TextViewScrollView extends ScrollView {
    private ScrollView mAlternativeScrollView;
    public TextViewScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setAlternativeScrollView(ScrollView alternativeScrollView) {
        mAlternativeScrollView = alternativeScrollView;
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mAlternativeScrollView != null) {
            mAlternativeScrollView.scrollTo(l, t);
        }
    }
}
