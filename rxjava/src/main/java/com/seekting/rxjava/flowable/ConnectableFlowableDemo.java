package com.seekting.rxjava.flowable;

import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/15.
 */

public class ConnectableFlowableDemo {

    public static void main(String args[]) throws Throwable {

        ConnectableFlowable<Integer> connectableFlowable = Flowable.fromArray(1, 2, 3, 4).publish();

        FlowableSubscriber subscriber = new FlowableSubscriber() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };
        connectableFlowable
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);

        Thread.sleep(1000);
        System.out.println("connect-begin");
        connectableFlowable.connect();

        System.out.println("connect-end");
        Thread.sleep(1000);
//        disposable.dispose();
        Thread.sleep(5000);
    }
}
