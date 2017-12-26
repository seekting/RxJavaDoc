package com.seekting.rxjava.observable;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/12/25.
 */

public class ObservableMapDemo {
    public static void main(String args[]) {

        Observable<Integer> observable = Observable.just(1);
        Observable mapObservable = observable.map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return String.valueOf(integer) + "hello!";
            }
        });
        mapObservable.subscribe(new MyDisposableObserver("map"));

    }
}
