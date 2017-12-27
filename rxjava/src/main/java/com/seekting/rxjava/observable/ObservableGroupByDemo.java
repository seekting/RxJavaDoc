package com.seekting.rxjava.observable;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Administrator on 2017/12/26.
 */

public class ObservableGroupByDemo {

    public static void main(String args[]) throws Throwable {
        List<String> albums = Arrays.asList(
                "The Piper at the Gates of Dawn",
                "A Saucerful of Secrets",
                "More", "Ummagumma", "Atom Heart Mother",
                "Meddle", "Obscured by Clouds",
                "The Dark Side of the Moon",
                "Wish You Were Here", "Animals", "The Wall"
        );
        Function<String, Integer> groupby = new Function<String, Integer>() {
            @Override
            public Integer apply(String str) throws Exception {
                return str.split(" ").length;
            }
        };
        Observable
                .fromIterable(albums)
                .groupBy(groupby)
                .subscribe(new DisposableObserver<GroupedObservable<Integer, String>>() {
                    @Override
                    public void onNext(GroupedObservable<Integer, String> integerStringGroupedObservable) {
                        Integer key = integerStringGroupedObservable.getKey();
                        integerStringGroupedObservable.subscribe(new MyDisposableObserver(key + ":"));

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
