package com.maxsix.bingo.config;

/**
 * Created by shihaixiong on 2016/8/8.
 */
public class UpdateInfo {
    private String version;
    private String description;
    private String url;
    private String sysupdate;
    private String payWay;
    private String limitPay;
    private String multiple;
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSysupdate() {
        return sysupdate;
    }

    public void setSysupdate(String sysupdate) {
        this.sysupdate = sysupdate;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }
}
