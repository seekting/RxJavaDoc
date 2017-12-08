package com.seekting.rxjava;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/11/30.
 */

public class FlowableFromPublisherDemo {

    public static void main(String args[]) {

        Publisher<String> publisher = new Publisher<String>() {
            @Override
            public void subscribe(Subscriber s) {
                s.onNext("s");

            }
        };
        Flowable<String> flowable = Flowable.fromPublisher(publisher);
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("s=" + s);
            }
        };
        flowable.subscribe(consumer);




    }
}
