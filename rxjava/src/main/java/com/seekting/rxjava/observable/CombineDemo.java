package com.seekting.rxjava.observable;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

/**
 * Created by Administrator on 2017/12/27.
 */

public class CombineDemo {

    public static void main(String args[]) {
        Observable<Integer> intObservable = Observable.just(1, 2, 3, 4);
        Observable<String> strObservable = Observable.just("a", "b", "c", "d", "e");
        Observable.combineLatest(intObservable, strObservable, new BiFunction<Integer, String, Person>() {
            @Override
            public Person apply(Integer integer, String s) throws Exception {
                return new Person(s, integer);
            }
        }).subscribe(new MyDisposableObserver("combineLatest"));
    }

}
