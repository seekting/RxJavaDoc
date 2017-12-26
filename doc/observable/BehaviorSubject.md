# BehaviorSubject

## demo

```java
public class BehaviorSubjectDemo {
    public static void main(String args[]) {

        BehaviorSubject<Float> a = BehaviorSubject.create();
        BehaviorSubject<Float> b = BehaviorSubject.create();
        BehaviorSubject<Float> c = BehaviorSubject.create();

        Observable.combineLatest(a, b, new BiFunction<Float, Float, Float>() {
            @Override
            public Float apply(Float aFloat, Float aFloat2) throws Exception {
                return aFloat + aFloat2;
            }
        }).subscribe(c);

        c.subscribe(new MyDisposableObserver("BehaviorSubject.C"));

        a.onNext(1f);
        b.onNext(2f);
        b.onNext(3f);
        a.onNext(2f);
    }
}

```
输出：
```Console

BehaviorSubject.C:3.0
BehaviorSubject.C:4.0
BehaviorSubject.C:5.0

Process finished with exit code 0
```

有点像广播注册