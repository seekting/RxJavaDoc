package com.seekting.rxjava.flowable;

import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/5.
 */

public class FlowableBlockingSubscribeDemo {

    public static void main(String args[]) {
        Flowable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("begin call");
                Thread.sleep(2000);
                System.out.println("end call");
                return "hello";
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .blockingSubscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("accept=" + s);
                    }
                });

        System.out.println("gogogo");
    }
}
