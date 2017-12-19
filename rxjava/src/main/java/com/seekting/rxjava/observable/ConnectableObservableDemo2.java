package com.seekting.rxjava.observable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/12/19.
 */

public class ConnectableObservableDemo2 {
    public static void main(String args[]) throws Throwable {
        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS);
        Observable<Long> publish = observable.publish().refCount();
        MyDisposableObserver observer1 = new MyDisposableObserver("observer1");
        MyDisposableObserver observer2 = new MyDisposableObserver("observer2");
        MyDisposableObserver observer3 = new MyDisposableObserver("observer3");

        publish.subscribe(observer1);
        Thread.sleep(2000);
        publish.subscribe(observer2);
        observer1.dispose();
        Thread.sleep(2000);
        observer2.dispose();

        Thread.sleep(1000);
        publish.subscribe(observer3);

        Thread.sleep(2000);

    }
}
