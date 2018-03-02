package com.seekting.rxjava.flowable;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/11/30.
 */

public class FlowableFromArrayDemo {

    public static void main(String args[]) {
        Flowable<String> flowable = Flowable.just("1", "2", "3");
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("s=" + s);
            }
        };
        flowable.subscribe(consumer);

    }
}
