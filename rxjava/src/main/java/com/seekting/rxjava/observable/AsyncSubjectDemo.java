package com.seekting.rxjava.observable;

import io.reactivex.subjects.AsyncSubject;

/**
 * Created by Administrator on 2017/12/25.
 */

public class AsyncSubjectDemo {

    public static void main(String args[]) {
        AsyncSubject<Integer> asyncSubject = AsyncSubject.create();
        asyncSubject.subscribe(new MyDisposableObserver("asyncSubject"));
        asyncSubject.onNext(1);
        asyncSubject.onNext(2);
        asyncSubject.onNext(3);
        asyncSubject.onNext(4);
        System.out.println("onComplete前");
        asyncSubject.onComplete();
        System.out.println("onCompleter后");

    }
}
