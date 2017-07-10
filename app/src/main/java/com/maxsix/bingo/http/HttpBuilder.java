package com.maxsix.bingo.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;

/**
 * Created by zhanghangting on 2017/2/7.
 *
 * 封装了的 HttpApi 构建器。内含过期时自动刷新 token 的功能。
 */
public class HttpBuilder {
    private static HttpBuilder instance = null;

    private String jwtToken;
    private long tokenExpired, tokenExpiredTick;
    private ITokenRefresher tokenRefresher;

    private HttpBuilder() {
    }

    public static HttpBuilder getInstance() {
        synchronized (HttpBuilder.class) {
            if (instance == null) {
                instance = new HttpBuilder();
            }
        }
        return instance;
    }

    public static <T> T createService(Class<T> service, String baseUrl) {
        return getInstance().create(service, baseUrl);
    }

    public static void setToken(String token, int expired, ITokenRefresher refresher) {
        getInstance().setJwtToken(token, expired, refresher);
    }

    private <T> T create(Class<T> service, String baseUrl) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new JwtTokenInterceptor())
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();

        T api = retrofit.create(service);

        return api;
    }

    private String getJwtToken() {
        long now = (new Timestamp(System.currentTimeMillis())).getTime() / 1000;

        // 如果 JWT 过期，自动刷新。
        // TODO：need fix 当前的请求可能会中断一次，没办法，主线程不能中断
        if (this.jwtToken != null && now > this.tokenExpiredTick) {
            if (this.tokenRefresher != null) {
                this.tokenRefresher.refreshToken(this.jwtToken)
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            Log.d("API_TEST", "new token in builder: " + s);
                            jwtToken = s;
                            tokenExpiredTick = (new Timestamp(System.currentTimeMillis())).getTime() / 1000 + tokenExpired;
                        }
                    });

                this.jwtToken = null;
            }
        }

        return this.jwtToken;
    }

    public void setJwtToken(String jwtToken, int expired, ITokenRefresher refresher) {
        this.jwtToken = jwtToken;
        this.tokenExpired = expired;
        this.tokenRefresher = refresher;
        this.tokenExpiredTick = (new Timestamp(System.currentTimeMillis())).getTime() / 1000 + expired;
    }

    // 自动将 JWT 注入所有的请求头部
    private class JwtTokenInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            String token = getJwtToken();
            if (!StringUtils.isEmpty(token)) {
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", "token " + token)
                        .build();
                return chain.proceed(newRequest);

            } else {
                return chain.proceed(originalRequest);
            }
        }
    }
}
