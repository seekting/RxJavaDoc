package com.seekting.rxjava.observable;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/12/18.
 */

public class ObservableErrorDemo {

    public static void main(String args[]) {

        Observable.error(new NullPointerException("xx")).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

                System.out.println("o=" + o);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

                throwable.printStackTrace();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {

                System.out.println("end!");
            }
        });
    }
}
