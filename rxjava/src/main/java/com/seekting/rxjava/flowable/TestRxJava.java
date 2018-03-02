package com.seekting.rxjava.flowable;


import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by Administrator on 2017/11/29.
 */

public class TestRxJava {
    public static void main(String args[]) {
        RxJavaPlugins.setOnFlowableAssembly(new Function<Flowable, Flowable>() {
            @Override
            public Flowable apply(Flowable flowable) throws Exception {
                //do some thing
                return flowable;
            }
        });
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        Flowable.just("Hello world")
                .subscribe(consumer);
////        System.out.println("hellow");

        System.setProperty("rx2.buffer-size", "100");
        System.out.println("rx2.buffer-size:" + Integer.getInteger("rx2.buffer-size"));
        Integer j = Integer.getInteger("sun.arch.data.model", 134);
        System.out.println("sun.arch.data.model:" + j);


    }


}
