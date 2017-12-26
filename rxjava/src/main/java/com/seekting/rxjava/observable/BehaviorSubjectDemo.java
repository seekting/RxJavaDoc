package com.seekting.rxjava.observable;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Administrator on 2017/12/25.
 */

public class BehaviorSubjectDemo {
    public static void main(String args[]) {

        BehaviorSubject<Float> a = BehaviorSubject.create();
        BehaviorSubject<Float> b = BehaviorSubject.create();
        BehaviorSubject<Float> c = BehaviorSubject.create();

        Observable.combineLatest(a, b, new BiFunction<Float, Float, Float>() {
            @Override
            public Float apply(Float aFloat, Float aFloat2) throws Exception {
                return aFloat + aFloat2;
            }
        }).subscribe(c);

        c.subscribe(new MyDisposableObserver("BehaviorSubject.C"));

        a.onNext(1f);
        b.onNext(2f);
        b.onNext(3f);
        a.onNext(2f);
    }
}
