package com.seekting.rxjava.flowable;

import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.flowable.FlowableSubscribeOn;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/30.
 */

public class FlowableSubscribeOnDemo {

    public static void main(String args[]) throws InterruptedException {

        Flowable<String> flowable = Flowable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000);
                System.out.println("call:" + Thread.currentThread());
                return "hello";
            }
        });
        FlowableSubscribeOn<String> flowable1 = (FlowableSubscribeOn<String>) flowable.subscribeOn(Schedulers.io());
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept:" + Thread.currentThread());
                System.out.println("s=" + s);

            }
        };
        flowable1.subscribe(consumer);
        System.out.println(Thread.currentThread());


        Thread.sleep(6000);
    }
}
