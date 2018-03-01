package com.seekting.rxjava.observable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ZipDemo {
    public static void main(String args[]) {
        List<String> strings = new ArrayList<>();
        strings.add("a");
        strings.add("b");
        strings.add("c");
        strings.add("d");
        strings.add("e");
        Observable.just(1, 2, 3, 4).zipWith(strings, new BiFunction<Integer, String, Person>() {
            @Override
            public Person apply(Integer integer, String s) throws Exception {
                return new Person(s, integer);
            }
        }).subscribe(new MyDisposableObserver("zip:"));
    }




}
