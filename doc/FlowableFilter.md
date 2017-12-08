# FlowableFilter

## 例子
```java
Flowable.range(0, 10).filter(new Predicate<Integer>() {
        @Override
        public boolean test(Integer integer) throws Exception {
            return integer % 2 == 0;
        }
    }).subscribe(new Consumer<Integer>() {
        @Override
        public void accept(Integer integer) throws Exception {
            System.out.println("integer=" + integer);
        }
    });

```
## 输出
```Console
integer=0
integer=2
integer=4
integer=6
integer=8

Process finished with exit code 0

```

## 源码解析
filter返回的是一个FlowableFilter对象
```java
public final Flowable<T> filter(Predicate<? super T> predicate) {
    ObjectHelper.requireNonNull(predicate, "predicate is null");
    return RxJavaPlugins.onAssembly(new FlowableFilter<T>(this, predicate));
}

```
调用FlowableFilter.subscribe方法和普通的一样，生成一个new 一个LambdaSubscriber，然后调用调用FlowableFilter.subscribeActual方法
```java
   protected void subscribeActual(Subscriber<? super T> s) {
       //...ignore code
            source.subscribe(new FilterSubscriber<T>(s, predicate));
    }

```
这里的source是FlowableRange,s是LambdaSubscriber，也就是调用了FlowableRange.subscribe(FilterSubscriber)

然后会走FlowableRange.subscribeActual

```java
 public void subscribeActual(Subscriber<? super Integer> s) {
       //...ignore code
            s.onSubscribe(new RangeSubscription(s, start, end));
    }

```
调用FilterSubscriber.onSubscribe(RangeSubscription)

FilterSubscriber.onSubscribe调用的是父类BasicFuseableSubscriber的onSubscribe方法

这里的actual是LambdaSubscriber

```java
public final void onSubscribe(Subscription s) {
        if (SubscriptionHelper.validate(this.s, s)) {

            this.s = s;
            if (s instanceof QueueSubscription) {
                this.qs = (QueueSubscription<T>)s;
            }

            if (beforeDownstream()) {

                actual.onSubscribe(this);

                afterDownstream();
            }

        }
    }

```
LambdaSubscriber.onSubscribe(this)则会走this.request()也就是FilterSubscriber的request方法
FilterSubscriber的request方法在BasicFuseableSubscriber里实现;这里的s是RangeSubscription<br>
BasicFuseableSubscriber.java

```java
@Override
public void request(long n) {
    s.request(n);
}

```
然后是RangeSubscription.request方法，会调用到a.onNext(i)这里的a是FilterSubscriber

```java
@Override
void fastPath() {
    int f = end;
    ConditionalSubscriber<? super Integer> a = actual;

    for (int i = index; i != f; i++) {
        if (cancelled) {
            return;
        }
        a.tryOnNext(i);
    }
    if (cancelled) {
        return;
    }
    a.onComplete();
}
```
也就是FilterSubscriber.tryOnNext

```java
 @Override
public boolean tryOnNext(T t) {
    if (done) {
        return false;
    }
    if (sourceMode != NONE) {
        actual.onNext(null);
        return true;
    }
    boolean b;
    try {
        b = filter.test(t);
    } catch (Throwable e) {
        fail(e);
        return true;
    }
    if (b) {
        actual.onNext(t);
    }
    return b;
}

```

tryNext里调用filter.test(t)如果b返回true才调用actual.onNext(t);也就是调用LambdaSubscriber.onNext(t)

LambdaSubscriber.java
```java
@Override
public void onNext(T t) {
    if (!isDisposed()) {
        try {
            onNext.accept(t);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            get().cancel();
            onError(e);
        }
    }
}

```
走到回调
```java
public void accept(Integer integer) throws Exception {
        System.out.println("integer=" + integer);
    }

```

这里有一个ConditionalSubscriber，尝试走tryOnNext

```java
public interface ConditionalSubscriber<T> extends FlowableSubscriber<T> {
    boolean tryOnNext(T t);
}

```
看FilterSubscriber的实现，它的意思是有可能走onNext也有可能不走
```java
@Override
public boolean tryOnNext(T t) {
    if (done) {
        return false;
    }
    if (sourceMode != NONE) {
        actual.onNext(null);
        return true;
    }
    boolean b;
    try {
        b = filter.test(t);
    } catch (Throwable e) {
        fail(e);
        return true;
    }
    if (b) {
        actual.onNext(t);
    }
    return b;
}

```

当FlowableRange调用subscribeActual的时候，会判断是不是ConditionalSubscriber；而此时FilterSubscriber是ConditionalSubscriber。<br>
所以它会用RangeConditionalSubscription

```java
  @Override
public void subscribeActual(Subscriber<? super Integer> s) {
    if (s instanceof ConditionalSubscriber) {
        s.onSubscribe(new RangeConditionalSubscription(
                (ConditionalSubscriber<? super Integer>)s, start, end));
    } else {
        s.onSubscribe(new RangeSubscription(s, start, end));
    }
}

```

所以它才会有遍历所有元素去走tryOnNext(i)

```java
void fastPath() {
    int f = end;
    ConditionalSubscriber<? super Integer> a = actual;

    for (int i = index; i != f; i++) {
        if (cancelled) {
            return;
        }
        a.tryOnNext(i);
    }
    if (cancelled) {
        return;
    }
    a.onComplete();
}

```