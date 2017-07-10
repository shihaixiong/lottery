package com.maxsix.bingo.config;

/**
 * Created by shihaixiong on 2016/6/2.
 */
public class Constants {
    public static final String APP_ID = "wxabfb327cc933d53f";
    //支付宝信息
    // 商户PID
    public static final String PARTNER = "";
    // 商户收款账号
    public static final String SELLER = "";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";
    // 主机地址
    public static String MAIN_ENGINE = "http://cp.gtweixin.com/api/";
    //public static String MAIN_ENGINE = "http://marsix.ngrok.cc/api/";
    public static String NAME = "NAME";
    // 获取期数
    public  static String GetStages_URL = MAIN_ENGINE + "stages/";
    //用户登录
    public static String Login_URL = MAIN_ENGINE+"token/";
    //注册
    public static String Register_URL = MAIN_ENGINE+"users/";
    //获取发送验证码
    public static String SendCode_URL = MAIN_ENGINE+"sms/";
    //验证验证码
    public static String Validatione_URL = MAIN_ENGINE+"sms/validation/";
    //找回密码
    public static String Getpassword_URL = MAIN_ENGINE+"sms/getpwd/";
    public static String User_BaseURL = MAIN_ENGINE+"users/";
    public static String CONTENT_TYPE_JSON = "application/json";
    public static String CONTENT_TYPE_URLENCODED = "application/x-www-form-urlencoded";
    public static String CONTENT_TYPE_FORMDATA = "multipart/form-data";
    public static String CONTENT_TYPE_TEXT = "text/plain";

    public static String redStr = "01,02,07,08,12,13,18,19,23,24,29,30,34,35,40,45,46";
    public static String blueStr ="03,04,09,10,14,15,20,25,26,31,36,37,41,42,47,48";
    public static String greenStr = "05,06,11,16,17,21,22,27,28,32,33,38,39,43,44,49";
    public static String[] sx = {"10,22,34,46",
            "09,21,33,45",
            "08,20,32,44",
            "07,19,31,43",
            "06,18,30,42",
            "05,17,29,41",
            "04,16,28,40",
            "03,15,27,39",
            "02,14,26,38",
            "01,13,25,37,49",
            "12,24,36,48",
            "11,23,35,47"};
    public static String[] sxstr = {"鼠","牛","虎","兔","龙","蛇","马","羊","猴","鸡","狗","猪"};
    public static final String [] bankTypes ={"中国工商银行","中国农业银行",
            "中国银行","建设银行","交通银行",
            "中国邮政储蓄银行","浦发银行","招商银行",
            "广发银行","中国农村信用社","光大银行",
            "兴业银行","中信银行","华夏银行",
            "中国民生银行"};
    // 屏幕高度
    public static int SCREEN_HEIGHT = 800;

    // 屏幕宽度
    public static int SCREEN_WIDTH = 480;

    // 屏幕密度
    public static float SCREEN_DENSITY = 1.5f;
}
