package com.seekting.rxjava.observable;

import io.reactivex.subjects.ReplaySubject;

/**
 * Created by Administrator on 2017/12/25.
 */

public class ReplaySubjectDemo {

    public static void main(String args[]) {

        ReplaySubject<String> replaySubject = ReplaySubject.create();
        replaySubject.onNext("1");
        replaySubject.subscribe(new MyDisposableObserver("replaySubjectA"));
        replaySubject.onNext("2");
        System.out.println("wait");
        replaySubject.onNext("3");
        replaySubject.subscribe(new MyDisposableObserver("replaySubjectB"));
        replaySubject.onComplete();



    }
}
