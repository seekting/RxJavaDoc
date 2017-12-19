package com.seekting.rxjava.observable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/18.
 */

public class DisposableDemo {

    public static void main(String args[]) throws Throwable {
        final List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        DisposableObserver<String> disposableObserver = new DisposableObserver<String>() {
            @Override
            protected void onStart() {
                super.onStart();
                System.out.println("onStart");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext" + s);

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

            }

            @Override
            public void onComplete() {

                System.out.println("onComplete");

            }
        };
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                for (String s : list) {


                    if (e.isDisposed()) {
                        break;
                    }
                    e.onNext(s);
                    try {

                        Thread.sleep(1000);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }

                }

                if (!e.isDisposed()) {
                    e.onComplete();
                } else {
                    System.out.println("订阅者已经取消");
                }
            }
        }).subscribeOn(Schedulers.computation()).subscribe(disposableObserver);

        Thread.sleep(1500);
        disposableObserver.dispose();
        Thread.sleep(40000);
    }
}
