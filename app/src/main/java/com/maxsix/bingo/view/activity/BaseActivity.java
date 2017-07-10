package com.maxsix.bingo.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import org.apache.http.message.BasicNameValuePair;

import java.util.concurrent.Callable;

public abstract class BaseActivity extends AppCompatActivity {
	protected Activity context;
	protected com.maxsix.bingo.dialog.FlippingLoadingDialog mLoadingDialog;
	protected com.maxsix.bingo.util.HttpHelper http = com.maxsix.bingo.util.HttpHelper.GetInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		com.maxsix.bingo.App.getInstance2().addActivity(this);
		initControl();
		initView();
		initData();
		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			com.maxsix.bingo.util.Utils.finish(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	protected abstract void findViewById();
	/**
	 * 绑定控件id
	 */
	protected abstract void initControl();

	/**
	 * 初始化控件
	 */
	protected abstract void initView();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 设置监听
	 */
	protected abstract void setListener();
	/*
 * 返回
 */
	public void doBack(View view) {
		onBackPressed();
	}

	/**
	 * 打开 Activity
	 * 
	 * @param activity
	 * @param cls
	 * @param name
	 */
	public void start_Activity(Activity activity, Class<?> cls,
			BasicNameValuePair... name) {
		com.maxsix.bingo.util.Utils.start_Activity(activity, cls, name);
	}

	/**
	 * 关闭 Activity
	 * 
	 * @param activity
	 */
	public void finish(Activity activity) {
		com.maxsix.bingo.util.Utils.finish(activity);
	}

	/**
	 * 判断是否有网络连接
	 */
	public boolean isNetworkAvailable(Context context) {
		return com.maxsix.bingo.util.Utils.isNetworkAvailable(context);
	}

	public com.maxsix.bingo.dialog.FlippingLoadingDialog getLoadingDialog(String msg) {
		if (mLoadingDialog == null)
			mLoadingDialog = new com.maxsix.bingo.dialog.FlippingLoadingDialog(this, msg);
		return mLoadingDialog;
	}
	/**
	 * @param <T>       模板参数，操作时要返回的内容
	 * @param pCallable 需要异步调用的操作
	 * @param pCallback 回调
	 */
	protected <T> void doAsync(final Callable<T> pCallable, final com.maxsix.bingo.task.Callback<T> pCallback, final com.maxsix.bingo.task.Callback<Exception> pExceptionCallback, final boolean showDialog, String message) {
		com.maxsix.bingo.task.EMobileTask.doAsync(this, null, message, pCallable, pCallback, pExceptionCallback, false, showDialog);
	}

	protected <T> void doAsync(final CharSequence pTitle, final CharSequence pMessage, final Callable<T> pCallable, final com.maxsix.bingo.task.Callback<T> pCallback, final boolean showDialog) {
		com.maxsix.bingo.task.EMobileTask.doAsync(this, pTitle, pMessage, pCallable, pCallback, null, false, showDialog);
	}

	/**
	 * Performs a task in the background, showing a {@link ProgressDialog},
	 * while the {@link Callable} is being processed.
	 *
	 * @param <T>
	 * @param pTitleResID
	 * @param pMessageResID
	 * @param pCallable
	 * @param pCallback
	 */
	protected <T> void doAsync(final int pTitleResID, final int pMessageResID, final Callable<T> pCallable, final com.maxsix.bingo.task.Callback<T> pCallback) {
		this.doAsync(pTitleResID, pMessageResID, pCallable, pCallback, null);
	}

	/**
	 * Performs a task in the background, showing a indeterminate
	 * {@link ProgressDialog}, while the {@link Callable} is being processed.
	 *
	 * @param <T>
	 * @param pTitleResID
	 * @param pMessageResID
	 * @param pCallable
	 * @param pCallback
	 * @param pExceptionCallback
	 */
	protected <T> void doAsync(final int pTitleResID, final int pMessageResID, final Callable<T> pCallable, final com.maxsix.bingo.task.Callback<T> pCallback, final com.maxsix.bingo.task.Callback<Exception> pExceptionCallback) {
		com.maxsix.bingo.task.EMobileTask.doAsync(this, pTitleResID, pMessageResID, pCallable, pCallback, pExceptionCallback);
	}

	/**
	 * Performs a task in the background, showing a {@link ProgressDialog} with
	 * an ProgressBar, while the {@link com.maxsix.bingo.task.AsyncCallable} is being processed.
	 *
	 * @param <T>
	 * @param pTitleResID
	 * @param pCallback
	 */
	protected <T> void doProgressAsync(final int pTitleResID, final com.maxsix.bingo.task.ProgressCallable<T> pCallable, final com.maxsix.bingo.task.Callback<T> pCallback) {
		this.doProgressAsync(pTitleResID, pCallable, pCallback, null);
	}

	/**
	 * Performs a task in the background, showing a {@link ProgressDialog} with
	 * a ProgressBar, while the {@link com.maxsix.bingo.task.AsyncCallable} is being processed.
	 *
	 * @param <T>
	 * @param pTitleResID
	 * @param pCallback
	 * @param pExceptionCallback
	 */
	protected <T> void doProgressAsync(final int pTitleResID, final com.maxsix.bingo.task.ProgressCallable<T> pCallable, final com.maxsix.bingo.task.Callback<T> pCallback, final com.maxsix.bingo.task.Callback<Exception> pExceptionCallback) {
		com.maxsix.bingo.task.EMobileTask.doProgressAsync(this, pTitleResID, pCallable, pCallback, pExceptionCallback);
	}

	/**
	 * Performs a task in the background, showing an indeterminate
	 * {@link ProgressDialog}, while the {@link com.maxsix.bingo.task.AsyncCallable} is being
	 * processed.
	 *
	 * @param <T>
	 * @param pTitleResID
	 * @param pMessageResID
	 * @param pAsyncCallable
	 * @param pCallback
	 * @param pExceptionCallback
	 */
	protected <T> void doAsync(final int pTitleResID, final int pMessageResID, final com.maxsix.bingo.task.AsyncCallable<T> pAsyncCallable, final com.maxsix.bingo.task.Callback<T> pCallback, final com.maxsix.bingo.task.Callback<Exception> pExceptionCallback) {
		com.maxsix.bingo.task.EMobileTask.doAsync(this, pTitleResID, pMessageResID, pAsyncCallable, pCallback, pExceptionCallback);
	}
}