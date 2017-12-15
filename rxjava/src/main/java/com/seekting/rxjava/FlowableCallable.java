package com.seekting.rxjava;

import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/11/30.
 */

public class FlowableCallable {
    public static void main(String args[]) {

        Flowable<String> flowable = Flowable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (Math.random() > 0.5f) {
                    throw new NullPointerException("");
                }
                return "hello";
            }
        });
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        };
        Consumer<Throwable> throwableConsumer = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable s) throws Exception {
                System.out.println("exception:" + s);
            }
        };
        flowable.subscribe(consumer, throwableConsumer);
    }
}
