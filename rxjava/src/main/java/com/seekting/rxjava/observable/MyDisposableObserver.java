package com.seekting.rxjava.observable;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by Administrator on 2017/12/19.
 */

public class MyDisposableObserver extends DisposableObserver {

        String name;

        public MyDisposableObserver(String name) {
            this.name = name;
        }


        @Override
        public void onNext(Object o) {
            System.out.println(name + ":" + o);

        }

        @Override
        public void onError(Throwable e) {
            System.out.println(name + ":" + e);
        }

        @Override
        public void onComplete() {
            System.out.println(name + ":onComplete");
        }
}
