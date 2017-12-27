package com.seekting.rxjava.observable;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ObservableFilterDemo {
    public static void main(String args[]) {
        Observable.just(1, 2, 3, 4, 5).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer % 2 == 0;
            }
        }).subscribe(new MyDisposableObserver("%2=0:"));
    }
}
