package com.seekting.rxjavadoc.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by Administrator on 2017/12/13.
 */

public class RetrofitClient {

    private Retrofit mRetrofit;
    public final HttpRequestApi mHttpRequestApi;
    private static RetrofitClient instance = new RetrofitClient();

    public static RetrofitClient getInstance() {
        return instance;

    }

    RetrofitClient() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("http://192.168.1.108");
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        builder.addConverterFactory(HttpGsonConverterFactory.create(gson));
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        mRetrofit = builder.build();
        mHttpRequestApi = mRetrofit.create(HttpRequestApi.class);
    }


}







