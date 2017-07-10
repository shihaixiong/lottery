package com.maxsix.bingo.vo;

import java.io.Serializable;

/**
 * Created by shihaixiong on 2016/6/6.
 */
public class DataResult implements Serializable{
    private String token;
    private String detail;
    private String[] error;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = "Token "+token;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String[] getError() {
        return error;
    }

    public void setError(String[] error) {
        this.error = error;
    }

}
