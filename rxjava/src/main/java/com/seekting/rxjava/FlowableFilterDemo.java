package com.seekting.rxjava;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by Administrator on 2017/12/6.
 */

public class FlowableFilterDemo {

    public static void main(String args[]) {

        Flowable.range(0, 10).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer % 2 == 0;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("integer=" + integer);
            }
        });
    }
}
