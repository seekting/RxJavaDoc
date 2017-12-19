package com.seekting.rxjava.observable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Administrator on 2017/12/19.
 */

public class PublishSubjectDemo {
    public static void main(String args[]) throws Throwable{

        Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);
        PublishSubject<Long> publishSubject = PublishSubject.create();
        interval.subscribe(publishSubject);
        MyDisposableObserver observer1 = new MyDisposableObserver("observer1");
        MyDisposableObserver observer2 = new MyDisposableObserver("observer2");
        publishSubject.subscribe(observer1);
        publishSubject.subscribe(observer2);

        Thread.sleep(500);
        publishSubject.onNext(553L);
        MyDisposableObserver observer3 = new MyDisposableObserver("observer3");
        publishSubject.subscribe(observer3);
        Thread.sleep(2500);

        observer1.dispose();
        observer2.dispose();
        observer3.dispose();







    }
}
