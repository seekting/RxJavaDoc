# Disposable

## demo
可以通过调用disposable.dispose()取消订阅，这样在你自定义的Observable里去判断它是否取消订阅而结束自己
```java
public class DisposableDemo {

    public static void main(String args[]) throws Throwable {
        final List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        DisposableObserver<String> disposableObserver = new DisposableObserver<String>() {
            @Override
            protected void onStart() {
                super.onStart();
                System.out.println("onStart");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext" + s);

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

            }

            @Override
            public void onComplete() {

                System.out.println("onComplete");

            }
        };
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                for (String s : list) {


                    if (e.isDisposed()) {
                        break;
                    }
                    e.onNext(s);
                    try {

                        Thread.sleep(1000);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }

                }

                if (!e.isDisposed()) {
                    e.onComplete();
                } else {
                    System.out.println("订阅者已经取消");
                }
            }
        }).subscribeOn(Schedulers.computation()).subscribe(disposableObserver);

        Thread.sleep(1500);
        disposableObserver.dispose();
        Thread.sleep(40000);
    }
}

```

## 输出

```Console
onStart
onNext1
onNext2
java.lang.InterruptedException: sleep interrupted
	at java.lang.Thread.sleep(Native Method)
	at com.seekting.rxjava.observable.SubscribtionDemo$2.subscribe(SubscribtionDemo.java:63)
	at io.reactivex.internal.operators.observable.ObservableCreate.subscribeActual(ObservableCreate.java:40)
	at io.reactivex.Observable.subscribe(Observable.java:10981)
	at io.reactivex.internal.operators.observable.ObservableSubscribeOn$SubscribeTask.run(ObservableSubscribeOn.java:96)
	at io.reactivex.internal.schedulers.ScheduledDirectTask.call(ScheduledDirectTask.java:38)
	at io.reactivex.internal.schedulers.ScheduledDirectTask.call(ScheduledDirectTask.java:26)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$201(ScheduledThreadPoolExecutor.java:180)
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:293)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
订阅者已经取消

Process finished with exit code 0

```

## 源码解析

待补充