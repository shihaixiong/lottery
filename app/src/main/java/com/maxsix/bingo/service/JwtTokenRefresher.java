package com.maxsix.bingo.service;

import android.util.Log;


import com.maxsix.bingo.http.HttpBuilder;
import com.maxsix.bingo.http.ITokenRefresher;
import com.maxsix.bingo.vo.Jwt;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhanghangting on 2017/2/7.
 */
public class JwtTokenRefresher implements ITokenRefresher {

    /*
     * 刷新器是一个 Observable，用以做前期处理，最终生成 String 类型的 Token
     */
    @Override
    public Observable<String> refreshToken(final String oldToken) {

        return Observable.create(new Observable.OnSubscribe<String>(){

            @Override
            public void call(final Subscriber<? super String> subscriber) {
                IApiService api = HttpBuilder.createService(IApiService.class, ConstValue.BASE_URL);
                Call<Jwt> call = api.refreshToken(oldToken);
                call.enqueue(new Callback<Jwt>() {
                    @Override
                    public void onResponse(Call<Jwt> call, Response<Jwt> response) {

                        String token = response.body().getToken();

                        Log.i("API_TEST", "new token: " + token);

                        subscriber.onNext(token);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(Call<Jwt> call, Throwable t) {
                        Log.e("API_TEST", "new token error: ", t);

                        subscriber.onNext("");
                        subscriber.onCompleted();
                    }
                });

            }
        });
    }
}
