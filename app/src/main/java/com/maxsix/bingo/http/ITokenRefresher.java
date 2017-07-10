package com.maxsix.bingo.http;

import rx.Observable;

/**
 * Created by zhanghangting on 2017/2/7.
 */
public interface ITokenRefresher {
    Observable<String> refreshToken(String oldToken);
}
