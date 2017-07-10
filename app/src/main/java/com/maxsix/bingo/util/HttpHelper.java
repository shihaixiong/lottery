package com.maxsix.bingo.util;

import android.util.Log;


import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tls on 2015/7/14.
 */
public class HttpHelper implements com.maxsix.bingo.util.IHttp {
    private String appCookie;
    private String aspCookie;
    private String Authorization ="";
    private String uid;
    private static final String TAG = "HttpHelper";
    private static HttpHelper helper;
    private OkHttpClient client;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }

    public static HttpHelper GetInstance() {
        if (helper == null) {
            helper = new HttpHelper();
        }

        return helper;
    }

    public String Get(String url) {
        URL realurl = null;
        HttpURLConnection conn = null;
        String result = "";
        try {
            realurl = new URL(url);
            conn = (HttpURLConnection) realurl.openConnection();
            conn.setRequestMethod("GET");
            if (Authorization != null && !Authorization.isEmpty()) conn.setRequestProperty("Authorization", Authorization);

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line = null;
                while((line = in.readLine()) != null ){
                    result += line;
                }
                in.close();

                GetCookie(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null)
                conn.disconnect();
        }
        return result;
    }
    public String OkHttpPost(String url,String json) throws IOException{
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url).header("Authorization",Authorization)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    public InputStream GetStream(String url) {
        InputStream is = null;
        URL realurl = null;
        HttpURLConnection conn = null;
        try {
            realurl = new URL(url);
            conn = (HttpURLConnection) realurl.openConnection();
            conn.setRequestMethod("GET");
            if (Authorization != null && !Authorization.isEmpty()) conn.setRequestProperty("Authorization", Authorization);

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                is = conn.getInputStream();

                GetCookie(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null)
                conn.disconnect();
        }
        return is;
    }


    public String Post(String url, String params) {
        return Post(url, params, com.maxsix.bingo.config.Constants.CONTENT_TYPE_JSON);
    }

    public String Post(String url, String params, String contentType) {
        URL realurl = null;
        HttpURLConnection conn = null;
        String result = "";
        try {
            realurl = new URL(url);
            conn = (HttpURLConnection) realurl.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Content-Type", contentType);
            if (Authorization != null && !Authorization.isEmpty()) conn.setRequestProperty("Authorization", Authorization);
            conn.setDoOutput(true);
            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            out.write(params.getBytes());
            out.flush();
            out.close();
            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line = null;
                while((line = in.readLine()) != null ){
                    result += line;
                }
                in.close();
                //GetCookie(conn);
            }else
            {
                Log.i(TAG, "访问失败" + conn.getErrorStream());
                InputStream errorstream = conn.getErrorStream();
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(errorstream));
                while ((line=br.readLine()) != null) {
                    result+=line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null)
                conn.disconnect();
        }
        return result;
    }
    public String Put(String url,String params, String contentType){
        URL realurl = null;
        String result = "";
        HttpURLConnection httpURLConnection = null;
        DataOutputStream dataOutputStream = null;
        try {
            realurl = new URL(url);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }

        try {
            httpURLConnection = (HttpURLConnection) realurl.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", contentType);
            httpURLConnection.setRequestMethod("PUT");
            if (Authorization != null && !Authorization.isEmpty()) httpURLConnection.setRequestProperty("Authorization", Authorization);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(params.getBytes());

        } catch (IOException exception) {
            exception.printStackTrace();
        }  finally {
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    int responseCode = httpURLConnection.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK){
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(httpURLConnection.getInputStream()));
                        String line = null;
                        while((line = in.readLine()) != null ){
                            result += line;
                        }
                } else{
                        Log.i(TAG, "访问失败" + httpURLConnection.getErrorStream());
                        InputStream errorstream = httpURLConnection.getErrorStream();
                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(errorstream));
                        while ((line=br.readLine()) != null) {
                            result+=line;
                        }
                    }
                }catch (Exception _exception) {
                    _exception.printStackTrace();
                }
            }
                if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return result;
    }
    public String Delete(String url,String contentType){
        URL realurl = null;
        String result = "";
        try {
            realurl = new URL(url);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) realurl.openConnection();
            httpURLConnection.setRequestProperty("Content-Type",
                    contentType);
            if (Authorization != null && !Authorization.isEmpty()) httpURLConnection.setRequestProperty("Authorization", Authorization);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("DELETE");
            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            }else{
                Log.i(TAG, "访问失败" + httpURLConnection.getErrorStream());
                InputStream errorstream = httpURLConnection.getErrorStream();
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(errorstream));
                while ((line=br.readLine()) != null) {
                    result+=line;
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return result;
    }
    // TODO:如果接口还需要其他 Cookie，在这里加上
    private void GetCookie(HttpURLConnection conn) {
        Map<String, List<String>> maps = conn.getHeaderFields();

        List<String> skeylist = maps.get("Authorization");
        if (skeylist != null) {
            Iterator<String> skeyit = skeylist.iterator();
            while (skeyit.hasNext()) {
                String c = skeyit.next();
                Authorization = c;
            }
        }
    }
}
