package com.seekting.rxjava;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/11/30.
 */

public class FlowableFromIterableDemo {

    public static void main(String args[]) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        Flowable<String> flowable = Flowable.fromIterable(list);
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("s=" + s);
            }
        };
        flowable.subscribe(consumer);


    }
}
