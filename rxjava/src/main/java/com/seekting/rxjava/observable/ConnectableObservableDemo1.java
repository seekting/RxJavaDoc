package com.seekting.rxjava.observable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

/**
 * Created by Administrator on 2017/12/19.
 */

public class ConnectableObservableDemo1 {
    public static void main(String args[]) throws Throwable {

        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS);
        ConnectableObservable<Long> replayObservable = observable.replay();
        MyDisposableObserver observer1 = new MyDisposableObserver("observer1");
        MyDisposableObserver observer2 = new MyDisposableObserver("observer2");
        MyDisposableObserver observer3 = new MyDisposableObserver("observer3");
        replayObservable.connect();
        replayObservable.subscribe(observer1);
        replayObservable.subscribe(observer2);
        Thread.sleep(2000);
        replayObservable.subscribe(observer3);


        Thread.sleep(1000);
        observer1.dispose();

        Thread.sleep(2000);


    }

}
