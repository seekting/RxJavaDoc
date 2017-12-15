package com.seekting.rxjavadoc.retrofit;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Administrator on 2017/12/13.
 */

public interface HttpRequestApi {

    @GET(value = "http/girls/")
    Observable<String> girls();

    @GET(value = "http/json/audience.json")
    public Observable<String> audience();


}