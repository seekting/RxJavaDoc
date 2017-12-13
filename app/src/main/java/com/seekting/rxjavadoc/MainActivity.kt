package com.seekting.rxjavadoc

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.seekting.rxjavadoc.retrofit.RetrofitClient
import rx.functions.Action1
import rx.schedulers.Schedulers

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val jj = RetrofitClient.getInstance().mHttpRequestApi.audience()

        jj.observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(Action1 {

            Log.d("seekting", "MainActivity.onCreate()" + it)

        }, Action1 {
            Log.d("seekting", "MainActivity.onCreate() error" + it, it)
        })

    }
}
