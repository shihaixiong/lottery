package com.maxsix.bingo.config;

import java.util.ArrayList;
import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

public class AppManager {
    private static Stack<Activity> mActivityStack;
    private static AppManager mAppManager;
    private com.maxsix.bingo.config.Constants config;
    private com.maxsix.bingo.vo.User userSession;
    private String version = "未知版本";
    private ArrayList<com.maxsix.bingo.vo.BettingInfo> infos;
    private String payWay;
    private String mulitiple = "43";
    private int limitPay;
    private AppManager() {
        config = new com.maxsix.bingo.config.Constants();
    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        if (mAppManager == null) {
            mAppManager = new AppManager();
        }
        return mAppManager;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    public Activity getTopActivity() {
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public void killTopActivity() {
        Activity activity = mActivityStack.lastElement();
        killActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void killActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void killActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                killActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void killAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            killAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public com.maxsix.bingo.config.Constants getConfig() {
        return config;
    }

    public com.maxsix.bingo.vo.User getUserSession() {
        return userSession;
    }

    public void setUserSession(com.maxsix.bingo.vo.User userSession) {
        this.userSession = userSession;
    }

    public ArrayList<com.maxsix.bingo.vo.BettingInfo> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<com.maxsix.bingo.vo.BettingInfo> infos) {
        this.infos = infos;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(int limitPay) {
        this.limitPay = limitPay;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getMulitiple() {
        return mulitiple;
    }

    public void setMulitiple(String mulitiple) {
        this.mulitiple = mulitiple;
    }
}

