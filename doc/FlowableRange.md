# FlowableRange

## 例子

```java
Flowable.range(0, 5).subscribe(new Consumer<Integer>() {
        @Override
        public void accept(Integer integer) throws Exception {
            System.out.println("i=" + integer);
        }
    });

```
输出：
```Console
i=0
i=1
i=2
i=3
i=4

Process finished with exit code 0

```

## 源码解析

```java
public static Flowable<Integer> range(int start, int count) {
    if (count < 0) {
        throw new IllegalArgumentException("count >= 0 required but it was " + count);
    } else
    if (count == 0) {
        return empty();
    } else
    if (count == 1) {
        return just(start);
    } else
    if ((long)start + (count - 1) > Integer.MAX_VALUE) {
        throw new IllegalArgumentException("Integer overflow");
    }
    return RxJavaPlugins.onAssembly(new FlowableRange(start, count));
}

```

1. 如果count>start+1就会new 一个FlowableRange
1. 然后就是组装成LambdaSubscriber然后走subscribeActual(LambdaSubscriber)
```java
public void subscribeActual(Subscriber<? super Integer> s) {
    if (s instanceof ConditionalSubscriber) {
        s.onSubscribe(new RangeConditionalSubscription(
                (ConditionalSubscriber<? super Integer>)s, start, end));
    } else {
        s.onSubscribe(new RangeSubscription(s, start, end));
    }
}

```
这里的s就是LambdaSubscriber，走LambdaSubscriber.onSubscribe(RangeSubscription)

LambdaSubscriber里有自己的
```java
 public void onSubscribe(Subscription s) {
        if (SubscriptionHelper.setOnce(this, s)) {
            try {
                onSubscribe.accept(this);//RequestMax
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                s.cancel();
                onError(ex);
            }
        }


    }

```
onSubscribe是RequestMax，也就是调用RequestMax.accept(LambdaSubscriber)
```java
 public enum RequestMax implements Consumer<Subscription> {
         INSTANCE;
         @Override
         public void accept(Subscription t) throws Exception {
             t.request(Long.MAX_VALUE);
         }
     }

```
RequestMax反过来调用LambdaSubscriber.request

LambdaSubscriber.java
```java
public void request(long n) {
    get().request(n);
}

```
这里的get()就是RangeSubscription

RangeSubscription.request调用的是fastPath
```java
@Override
public final void request(long n) {
    if (SubscriptionHelper.validate(n)) {
        if (BackpressureHelper.add(this, n) == 0L) {
            if (n == Long.MAX_VALUE) {
                fastPath();
            } else {
                slowPath(n);
            }
        }
    }
}

```

其实就是一个for循环调用onNext(i),最后nComplete()
```java

@Override
void fastPath() {
    int f = end;
    Subscriber<? super Integer> a = actual;

    for (int i = index; i != f; i++) {
        if (cancelled) {
            return;
        }
        a.onNext(i);
    }
    if (cancelled) {
        return;
    }
    a.onComplete();
}
```


