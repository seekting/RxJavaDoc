package com.seekting.rxjava;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/11/30.
 */

public class FlowableFutureDemo {

    public static void main(String args[]) {
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "future";
            }
        });

        Flowable<String> flowable = Flowable.fromFuture(future);
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("s=" + s);
            }
        };

        Thread thread = new Thread(future);
        thread.start();
        flowable.subscribe(consumer);

    }
}
