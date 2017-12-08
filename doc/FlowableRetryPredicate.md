# FlowableRetryPredicate

## Demo

```java
Flowable.fromCallable(new Callable<String>() {
        @Override
        public String call() throws Exception {
            System.out.println("call");
            if (Math.random() > 0.9f) {
                return "ok";
            } else {
                throw new TimeoutException("timeout");
            }
        }
    }).retry(3).subscribe(new Consumer<String>() {
        @Override
        public void accept(String integer) throws Exception {
            System.out.println("i=" + integer);
        }
    });

```

## 输出

```Console
call
call
ok
```

可以看出来，提供商品的时候，允许最多重试3次

## 源码分析

```java
public final Flowable<T> retry(long times, Predicate<? super Throwable> predicate) {
   //...ignore code
    return RxJavaPlugins.onAssembly(new FlowableRetryPredicate<T>(this, times, predicate));
}

```
subscribeActual方法
```java
@Override
public void subscribeActual(Subscriber<? super T> s) {
    SubscriptionArbiter sa = new SubscriptionArbiter();
    s.onSubscribe(sa);

    RepeatSubscriber<T> rs = new RepeatSubscriber<T>(s, count, predicate, sa, source);
    rs.subscribeNext();
}


```
s是LambdaSubscriber，也就是LambdaSubscriber.onSubscribe(SubscriptionArbiter)
也就是调用SubscriptionArbiter.request方法，该分支没有重要的逻辑，重点看subscribeNext，<br>
它会遍历去调用source.subscribe(this);这里的source是FlowableCallable，也就是走<br>
FlowableCallable.subscribe(RepeatSubscriber)

```java
void subscribeNext() {
    if (getAndIncrement() == 0) {
        int missed = 1;
        for (;;) {
            if (sa.isCancelled()) {
                return;
            }
            source.subscribe(this);

            missed = addAndGet(-missed);
            if (missed == 0) {
                break;
            }
        }
    }
}

```

如果失败会走onError()，而onError()又会调用subscribeNext，而这里的subscribeNext会调用getAndIncrement让它自增，但不会为0，所以该方法就退出

onError里会把remaining自减，而remaining到达0的时候就抛出异常，最终实现重试

```java
public void onError(Throwable t) {
        long r = remaining;
        if (r != Long.MAX_VALUE) {
            remaining = r - 1;
        }
        if (r == 0) {
            actual.onError(t);
        } else {
            boolean b;
            try {
                b = predicate.test(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                actual.onError(new CompositeException(t, e));
                return;
            }
            if (!b) {
                actual.onError(t);
                return;
            }
            subscribeNext();
        }
    }

```