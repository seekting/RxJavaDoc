package com.seekting.rxjava.observable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/12/18.
 */

public class IntervalObservableDemo {
    public static void main(String args[]) throws Throwable {

        Observable<Long> observable = Observable.interval(2000, TimeUnit.MILLISECONDS);
        observable.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("value=" + aLong);
            }
        });

        Thread.sleep(5000);
    }
}
