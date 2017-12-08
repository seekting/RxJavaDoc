package com.seekting.rxjava;

/**
 * Created by Administrator on 2017/11/29.
 */

public class MyFlowable {
    String value;

    public MyFlowable setValue(String s) {
        this.value = s;
        return this;
    }

    public void subscribe(MyConsumer a) {
        a.accept(value);

    }

    static class MyConsumer {
        public void accept(String value) {
            System.out.println("value=" + value);

        }

    }

    public static void main(String args[]) {
        MyFlowable myFlowable = new MyFlowable();
        MyConsumer myConsumer = new MyConsumer();
        myFlowable.setValue("xxxx").subscribe(myConsumer);

        Consumer1<String> consumer1 = new Consumer1<String>() {
            @Override
            public void accept(String s) {

                System.out.println("s=" + s);
            }
        };
        Publisher1<String> myP = new MyPublisher1("ttt");
        myP.provide(consumer1);

    }

    public interface Consumer1<T> {

        void accept(T t);
    }

    static abstract class Publisher1<T> {
        protected T t;

        public Publisher1(T t) {
            this.t = t;
        }

        abstract void provide(Consumer1 consumer1);
    }

    static class MyPublisher1 extends Publisher1<String> {
        public MyPublisher1(String s) {
            super(s);
        }

        @Override
        public void provide(Consumer1 consumer1) {
            consumer1.accept(t);

        }
    }
}
