package com.seekting.rxjava.flowable;


import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/12/5.
 */

public class FlowableFlatMapDemo {
    public static void main(String args[]) {

        Flowable.range(7, 13).flatMap(new Function<Integer, Publisher<Person>>() {
            @Override
            public Publisher<Person> apply(Integer s) throws Exception {
                System.out.print("s=" + s);
                return Flowable.just(new Person(s));
            }
        }).flatMap(new Function<Person, Publisher<School>>() {
            @Override
            public Publisher<School> apply(Person person) throws Exception {
                Publisher publisher = null;
                if (person.age < 11) {
                    publisher = Flowable.just(new School("small School"));
                } else if (person.age < 18) {
                    publisher = Flowable.just(new School("mid School"));

                } else if (person.age < 21) {
                    publisher = Flowable.just(new School("big School"));
                } else {
                    publisher = Flowable.just(new School("big School"));
                }
                return publisher;
            }
        }).subscribe(new Consumer<School>() {
            @Override
            public void accept(School school) throws Exception {
                System.out.println("school=" + school);
            }
        });
    }

    static class Person {
        int age;

        public Person(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age='" + age + '\'' +
                    '}';
        }
    }

    static class School {
        String name;

        public School(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "School{" +
                    "name='" + name + '\'' +
                    '}';
        }


    }

}
