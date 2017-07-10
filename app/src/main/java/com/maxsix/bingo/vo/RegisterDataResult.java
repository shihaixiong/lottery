package com.maxsix.bingo.vo;

import java.io.Serializable;

/**
 * Created by shihaixiong on 2016/6/6.
 */
public class RegisterDataResult extends DataResult implements Serializable{
    private String[] non_field_errors;
    private String username;


    public String[] getNon_field_errors() {
        return non_field_errors;
    }

    public void setNon_field_errors(String[] non_field_errors) {
        this.non_field_errors = non_field_errors;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
