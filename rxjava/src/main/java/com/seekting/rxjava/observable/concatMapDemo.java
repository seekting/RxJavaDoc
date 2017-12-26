package com.seekting.rxjava.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/12/26.
 */

public class concatMapDemo {

    public static void main(String args[]) {

        Observable.just(1,2).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                return Observable.just(integer + "_a", integer + "_b");
            }
        }).subscribe(new MyDisposableObserver("concatMap"));
    }
}
