package com.seekting.rxjava.observable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Timed;

/**
 * Created by Administrator on 2017/12/27.
 */

public class TimeStampDemo {

    public static void main(String args[]) {
        Observable<Timed<Timed<Integer>>> timedObservable = Observable.just(1, 2, 3).timestamp().timeInterval();
        timedObservable.subscribe(new MyDisposableObserver("timeStamp"));

    }
}
