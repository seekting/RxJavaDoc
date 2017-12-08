package com.seekting.rxjava;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/12/6.
 */
public class FlowableRetryPredicateDemo {
    public static void main(String args[]){

        Flowable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hello";
            }
        }).replay();
        Flowable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("call");
                if (Math.random() > 0.7f) {
                    return "ok";
                } else {
                    throw new TimeoutException("timeout");
                }
            }
        }).retry(3).subscribe(new Consumer<String>() {
            @Override
            public void accept(String integer) throws Exception {
                System.out.println("i=" + integer);
            }
        });
    }
}
