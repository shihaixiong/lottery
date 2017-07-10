package com.maxsix.bingo.vo;

/**
 * Created by shihaixiong on 2016/6/14.
 */
public class Wxpayinfo {

    /**
     * msg : null
     * status : 1
     * code : null
     * data : {"out_trade_no":"20160614091916079","result":{"trade_type":"APP","prepay_id":"wx20160614091917c00fcf264a0299848847","nonce_str":"j7BJF44yvsjDvqgY","return_code":"SUCCESS","return_msg":"OK","sign":"DD0F08CD14928B9EC4856385E13AE2F7","mch_id":"1354155002","appid":"wxabfb327cc933d53f","result_code":"SUCCESS"}}
     */

    private Object msg;
    private int status;
    private Object code;
    /**
     * out_trade_no : 20160614091916079
     * result : {"trade_type":"APP","prepay_id":"wx20160614091917c00fcf264a0299848847","nonce_str":"j7BJF44yvsjDvqgY","return_code":"SUCCESS","return_msg":"OK","sign":"DD0F08CD14928B9EC4856385E13AE2F7","mch_id":"1354155002","appid":"wxabfb327cc933d53f","result_code":"SUCCESS"}
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
        private String out_trade_no;
        /**
         * trade_type : APP
         * prepay_id : wx20160614091917c00fcf264a0299848847
         * nonce_str : j7BJF44yvsjDvqgY
         * return_code : SUCCESS
         * return_msg : OK
         * sign : DD0F08CD14928B9EC4856385E13AE2F7
         * mch_id : 1354155002
         * appid : wxabfb327cc933d53f
         * result_code : SUCCESS
         */

        private ResultBean result;

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            private String trade_type;
            private String prepay_id;
            private String nonce_str;
            private String return_code;
            private String return_msg;
            private String sign;
            private String mch_id;
            private String appid;
            private String result_code;

            public String getTrade_type() {
                return trade_type;
            }

            public void setTrade_type(String trade_type) {
                this.trade_type = trade_type;
            }

            public String getPrepay_id() {
                return prepay_id;
            }

            public void setPrepay_id(String prepay_id) {
                this.prepay_id = prepay_id;
            }

            public String getNonce_str() {
                return nonce_str;
            }

            public void setNonce_str(String nonce_str) {
                this.nonce_str = nonce_str;
            }

            public String getReturn_code() {
                return return_code;
            }

            public void setReturn_code(String return_code) {
                this.return_code = return_code;
            }

            public String getReturn_msg() {
                return return_msg;
            }

            public void setReturn_msg(String return_msg) {
                this.return_msg = return_msg;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getMch_id() {
                return mch_id;
            }

            public void setMch_id(String mch_id) {
                this.mch_id = mch_id;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getResult_code() {
                return result_code;
            }

            public void setResult_code(String result_code) {
                this.result_code = result_code;
            }
        }
    }
}
