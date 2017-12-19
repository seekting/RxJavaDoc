# ConnectableObservable之replay

## demo

```java
 public static void main(String args[]) throws Throwable {
        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS);
        Observable<Long> publish = observable.publish().refCount();
        MyDisposableObserver observer1 = new MyDisposableObserver("observer1");
        MyDisposableObserver observer2 = new MyDisposableObserver("observer2");
        MyDisposableObserver observer3 = new MyDisposableObserver("observer3");

        publish.subscribe(observer1);
        Thread.sleep(2000);
        publish.subscribe(observer2);
        observer1.dispose();
        Thread.sleep(2000);
        observer2.dispose();

        Thread.sleep(1000);
        publish.subscribe(observer3);

        Thread.sleep(2000);

    }

```
只要有一个订阅者，它就会运行，如果一个订阅者都没有，它就会停止，一旦再来一个订阅者，它会重新运行
## 输出

```Console
observer1:0
observer2:1
observer2:2
observer3:0
observer3:1

Process finished with exit code 0



```

## 源码解析

待完成

