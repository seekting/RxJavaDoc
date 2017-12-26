package com.seekting.rxjava.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/26.
 */

public class ObservableSwitchMapDemo {

    public static void main(String args[]) throws Throwable {
        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        Observable.fromArray(array).switchMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                return Observable.just(integer + "_a", integer + "_b").subscribeOn(Schedulers.io());
            }
        }).subscribe(new MyDisposableObserver("flatMap"));

        Thread.sleep(2000);
    }
}
