package com.seekting.rxjava.complete;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/1.
 */

public class CompletableFromAction {
    public static void main(String args[]) {
        action();


        observer();

    }


    private static void observer() {
        Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {

                        System.out.println("run1" + Thread.currentThread());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");

                    }

                    @Override
                    public void onError(Throwable e) {

                        System.out.println("onError");

                    }
                });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void action() {
        Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {

                        System.out.println("run1" + Thread.currentThread());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("run2" + Thread.currentThread());
                    }
                });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
