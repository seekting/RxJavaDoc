package com.seekting.rxjava.observable;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ScanDemo {

    public static void main(String args[]) {

        Observable.just(1, 2, 3, 4, 5).scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer sum, Integer value) throws Exception {
                return sum + value;
            }
        }).subscribe(new MyDisposableObserver("scan"));
    }
}
