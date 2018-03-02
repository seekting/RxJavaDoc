package com.seekting.rxjava2;


import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;

public class Collect {

    public static void main(String args[]) {

        Observable.just(3, 4, 5, 6)
                .collect(new Func0<List<Integer>>() { //

                    @Override
                    public List<Integer> call() {
                        return new ArrayList<Integer>();
                    }
                }, new Action2<List<Integer>, Integer>() { //
                    @Override
                    public void call(List<Integer> integers, Integer integer) {
                        integers.add(integer);
                    }
                })
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {

                        for (Integer integer : integers) {
                            System.out.println("integer" + integer);

                        }
                    }
                });

    }
}
