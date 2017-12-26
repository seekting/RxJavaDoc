# PublishSubject

Demo
```java
public class PublishSubjectDemo {
    public static void main(String args[]) throws Throwable{

        Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);
        PublishSubject<Long> publishSubject = PublishSubject.create();
        interval.subscribe(publishSubject);
        MyDisposableObserver observer1 = new MyDisposableObserver("observer1");
        MyDisposableObserver observer2 = new MyDisposableObserver("observer2");
        publishSubject.subscribe(observer1);
        publishSubject.subscribe(observer2);

        Thread.sleep(500);
        publishSubject.onNext(553L);
        MyDisposableObserver observer3 = new MyDisposableObserver("observer3");
        publishSubject.subscribe(observer3);
        Thread.sleep(2500);

        observer1.dispose();
        observer2.dispose();
        observer3.dispose();







    }
}

```
输出：
```Console
observer1:553
observer2:553
observer1:0
observer2:0
observer3:0
observer1:1
observer2:1
observer3:1
observer1:2
observer2:2
observer3:2

```

Subject是接收者，也是事件发起者，可以把它当成一个中间者，能发能收，收的时候它会把事件发给自己的监听者
