# ConnectableObservable之replay

## demo

```java
 public static void main(String args[]) throws Throwable {

        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS);
        ConnectableObservable<Long> replayObservable = observable.replay();
        MyDisposableObserver observer1 = new MyDisposableObserver("observer1");
        MyDisposableObserver observer2 = new MyDisposableObserver("observer2");
        MyDisposableObserver observer3 = new MyDisposableObserver("observer3");
        replayObservable.connect();
        replayObservable.subscribe(observer1);
        replayObservable.subscribe(observer2);
        Thread.sleep(2000);
        replayObservable.subscribe(observer3);


        Thread.sleep(1000);
        observer1.dispose();

        Thread.sleep(2000);


    }

```
它可以通知后来注册的监听者之前发生过的事情
## 输出

```Console
observer1:0
observer2:0
observer3:0
observer1:1
observer2:1
observer3:1
observer1:2
observer2:2
observer3:2
observer2:3
observer3:3
observer2:4
observer3:4


```

## 源码解析

待完成

