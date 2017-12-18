package com.seekting.rxjava.observable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/12/18.
 */

public class ObservableTimerDemo {
    public static void main(String args[]) throws Throwable {
        Observable<Long> timerObservable = Observable.timer(1000, TimeUnit.MILLISECONDS);
        System.out.println("begin");
        timerObservable.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("aLong=" + aLong);
            }
        });

        Thread.sleep(6000);
    }
}
