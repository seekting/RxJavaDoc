package com.seekting.rxjava.framework;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2018/3/2.
 */

public class FunctionDemo {

    public static void main(String args[]) {

        Observable.just(1).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return integer + "";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("s= " + s);
            }
        });
    }
}
