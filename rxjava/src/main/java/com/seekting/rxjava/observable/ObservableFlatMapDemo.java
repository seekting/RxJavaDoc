package com.seekting.rxjava.observable;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/12/25.
 */

public class ObservableFlatMapDemo {

    public static void main(String args[]) {
//        demo1();
//        demo2();
        demo3();


    }

    private static class Person {
        String name;
        int age;

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    private static void demo3() {
        Function function = new Function<Integer, Observable<String>>() {
            @Override
            public Observable<String> apply(Integer o) throws Exception {
                return Observable.just("Jim", "Lucy", "Lily");
            }
        };

        BiFunction biFunction = new BiFunction<Integer, String, Person>() {
            @Override
            public Person apply(Integer o, String o2) throws Exception {
                Person person = new Person();
                person.age = o;
                person.name = o2;
                return person;
            }
        };
        Observable<Integer> observable = Observable.just(23, 24).flatMap(function, biFunction);
        observable.subscribe(new MyDisposableObserver("demo3"));

    }

    private static void demo2() {
        Observable<String> fromCallable = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "1";
//                throw new NullPointerException("xx");
            }
        });
        Observable<Integer> o = fromCallable.flatMap(t -> Observable.just(Integer.parseInt(t) + 2), e -> Observable.just(4), () -> Observable.just(42));
        o.subscribe(new MyDisposableObserver("flatmap2"));

    }

    private static void demo1() {
        Observable<String> observable = Observable.just(1, 2).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                return Observable.just(integer + "-a", integer + "-b", integer + "-c");
            }
        });

        observable.subscribe(new MyDisposableObserver("flap_map"));
    }
}
