package com.maxsix.bingo.vo;

import java.io.Serializable;

/**
 * Created by shihaixiong on 2016/6/7.
 */
public class BettingInfo implements Serializable {
    private int stage;
    private String code;
    private int times;
    private int voucher = 0;
    private String name;
    private String bettingno;
    private boolean focus;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getVoucher() {
        return voucher;
    }

    public void setVoucher(int voucher) {
        this.voucher = voucher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBettingno() {
        return bettingno;
    }

    public void setBettingno(String bettingno) {
        this.bettingno = bettingno;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }
}
