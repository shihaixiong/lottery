package com.maxsix.bingo.vo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shihaixiong on 2016/6/14.
 */
public class Wxparame {

    /**
     * msg : null
     * status : 1
     * code : null
     * data : {"result":{"package":"Sign=WXPay","timestamp":"1465870085","sign":"A88B7FAB88E2CF2F14E16C0F624E880C","partnerid":"1354155002","appid":"wxabfb327cc933d53f","prepayid":"wx201606141003316aeae9e2c10865315651","noncestr":"pk1vu3x8fub2si8nza1lqijfga3iys1i"}}
     */

    private Object msg;
    private int status;
    private Object code;
    /**
     * result : {"package":"Sign=WXPay","timestamp":"1465870085","sign":"A88B7FAB88E2CF2F14E16C0F624E880C","partnerid":"1354155002","appid":"wxabfb327cc933d53f","prepayid":"wx201606141003316aeae9e2c10865315651","noncestr":"pk1vu3x8fub2si8nza1lqijfga3iys1i"}
     */

    private DataBean data;

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * package : Sign=WXPay
         * timestamp : 1465870085
         * sign : A88B7FAB88E2CF2F14E16C0F624E880C
         * partnerid : 1354155002
         * appid : wxabfb327cc933d53f
         * prepayid : wx201606141003316aeae9e2c10865315651
         * noncestr : pk1vu3x8fub2si8nza1lqijfga3iys1i
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            @SerializedName("package")
            private String packageX;
            private String timestamp;
            private String sign;
            private String partnerid;
            private String appid;
            private String prepayid;
            private String noncestr;

            public String getPackageX() {
                return packageX;
            }

            public void setPackageX(String packageX) {
                this.packageX = packageX;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getPartnerid() {
                return partnerid;
            }

            public void setPartnerid(String partnerid) {
                this.partnerid = partnerid;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getPrepayid() {
                return prepayid;
            }

            public void setPrepayid(String prepayid) {
                this.prepayid = prepayid;
            }

            public String getNoncestr() {
                return noncestr;
            }

            public void setNoncestr(String noncestr) {
                this.noncestr = noncestr;
            }
        }
    }
}
