package com.maxsix.bingo.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.maxsix.bingo.R;

public class GameRulescqsscActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cqsscgamerules);
    }
    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
