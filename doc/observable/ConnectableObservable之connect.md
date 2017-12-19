# ConnectableObservable之connect
## demo
先定义Observer
```java
static class MyDisposableObserver extends DisposableObserver {

    String name;

    public MyDisposableObserver(String name) {
        this.name = name;
    }


    @Override
    public void onNext(Object o) {
        System.out.println(name + ":" + o);

    }

    @Override
    public void onError(Throwable e) {
        System.out.println(name + ":" + e);
    }

    @Override
    public void onComplete() {
        System.out.println(name + ":onComplete");
    }
}

```
创建了三个Observer，然后observer3在2秒后加入，observer1在三秒后退出订阅
```java
public static void main(String args[]) throws Throwable {

    Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS);
    ConnectableObservable<Long> publish = observable.publish();
    MyDisposableObserver observer1 = new MyDisposableObserver("observer1");
    MyDisposableObserver observer2 = new MyDisposableObserver("observer2");
    MyDisposableObserver observer3 = new MyDisposableObserver("observer3");

    publish.subscribe(observer1);
    publish.subscribe(observer2);
    publish.connect();
    Thread.sleep(2000);
    publish.subscribe(observer3);

    Thread.sleep(1000);
    observer1.dispose();

    Thread.sleep(5000);


}

```
可以看出observer3在后来加进来，它收到的是从1开始
```Console
observer1:0
observer2:0
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

Process finished with exit code 0


```