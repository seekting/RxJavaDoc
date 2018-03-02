package com.seekting.rxjava.flowable;

import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.flowable.FlowableObserveOn;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/4.
 */

public class FlowableObserveOnDemo {

    public static void main(String args[]) throws InterruptedException {
        Flowable<String> flowable = Flowable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000);
                System.out.println("call:" + Thread.currentThread());
                return "hello";
            }
        });
        FlowableObserveOn<String> flowable1 = (FlowableObserveOn<String>) flowable.observeOn(Schedulers.io());
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept:" + Thread.currentThread());
                System.out.println("s=" + s);

            }
        };
        flowable1.subscribe(consumer);
        System.out.println(Thread.currentThread());


        Thread.sleep(60000);


    }
}
