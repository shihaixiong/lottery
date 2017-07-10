package com.maxsix.bingo.util;

/**
 * Created by tls on 2015/7/14.
 */
public interface IHttp {
    public String Get(String url);
    public String  Post(String url, String data);
    public String Post(String url, String data, String contentType);
}
