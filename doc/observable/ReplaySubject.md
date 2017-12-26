# ReplaySubject

## demo:
```java
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


```

## 输出

```Console
replaySubjectA:1
replaySubjectA:2
wait
replaySubjectA:3
replaySubjectB:1
replaySubjectB:2
replaySubjectB:3
replaySubjectA:onComplete
replaySubjectB:onComplete

```

后来注册的监听者能得到之前发生的事情