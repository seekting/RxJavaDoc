package com.seekting.rxjava;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/12/5.
 */

public class FlowableMapDemo {
    public static void main(String args[]) {

        Function<String, Person> function = new Function<String, Person>() {
            @Override
            public Person apply(String s) throws Exception {
                return new Person(s);
            }
        };
        Flowable.just("kate")
                .map(function).subscribe(new Consumer<Person>() {
            @Override
            public void accept(Person s) throws Exception {
                System.out.println("s=" + s);
            }
        });



    }


    static class Person {
        String name;

        public Person(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
