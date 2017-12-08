package com.seekting.rxjava;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/12/5.
 */

public class FlowableRangeDemo {
    public static void main(String args[]) {

        Flowable.range(0, 5).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("i=" + integer);
            }
        });
    }


}
