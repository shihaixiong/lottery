package com.maxsix.bingo.service;



import com.maxsix.bingo.vo.Jwt;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zhanghangting on 2017/2/7.
 *
 * Restful Api 接口全部写在这里，一般不用特殊处理就可以返回对象
 */
public interface IApiService {

    @POST("token/auth/")
    @FormUrlEncoded
    Call<Jwt> login(@Field("username") String username, @Field("password") String password);

    @POST("token/refresh/")
    @FormUrlEncoded
    Call<Jwt> refreshToken(@Field("token") String token);


}
