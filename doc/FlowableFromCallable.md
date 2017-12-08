# FlowableFromCallable

通过fromCallable创建FlowableCallable
```java
Flowable<String> flowable = Flowable.fromCallable(new Callable<String>() {
    @Override
    public String call() throws Exception {
        if (Math.random() > 0.5f) {
            throw new NullPointerException("");
        }
        return "hello";
    }
});

```
Callable充当supplier提供商品，也就是说，以前是Flowable能给订阅者提供，现在改成Flowable找supplier去要了
```java
public static <T> Flowable<T> fromCallable(Callable<? extends T> supplier) {
ObjectHelper.requireNonNull(supplier, "supplier is null");
return RxJavaPlugins.onAssembly(new FlowableFromCallable<T>(supplier));
}

```


之前分析过订阅的时候最终会走到自己的subscribeActual,而参数s就是LambdaSubscriber
```java
@Override
public void subscribeActual(Subscriber<? super T> s) {
    DeferredScalarSubscription<T> deferred = new DeferredScalarSubscription<T>(s);
    s.onSubscribe(deferred);

    T t;
    try {
        t = ObjectHelper.requireNonNull(callable.call(), "The callable returned a null value");
    } catch (Throwable ex) {
        Exceptions.throwIfFatal(ex);
        s.onError(ex);
        return;
    }

    deferred.complete(t);
}

```
创建了一个DeferredScalarSubscription对象Subscription有request方法，当调用s.onSubscribe(Subscription)的时候其实就是调用了Subscription的request
```java
  public enum RequestMax implements Consumer<Subscription> {
        INSTANCE;
        @Override
        public void accept(Subscription t) throws Exception {
            t.request(Long.MAX_VALUE);
        }
    }

```
也就会调用到DeferredScalarSubscription的request方法

```java
  @Override
public final void request(long n) {
    if (SubscriptionHelper.validate(n)) {
        for (;;) {
            int state = get();
            // if the any bits 1-31 are set, we are either in fusion mode (FUSED_*)
            // or request has been called (HAS_REQUEST_*)
            if ((state & ~NO_REQUEST_HAS_VALUE) != 0) {
                return;
            }
            if (state == NO_REQUEST_HAS_VALUE) {
                if (compareAndSet(NO_REQUEST_HAS_VALUE, HAS_REQUEST_HAS_VALUE)) {
                    T v = value;
                    if (v != null) {
                        value = null;
                        Subscriber<? super T> a = actual;
                        a.onNext(v);
                        if (get() != CANCELLED) {
                            a.onComplete();
                        }
                    }
                }
                return;
            }
            if (compareAndSet(NO_REQUEST_NO_VALUE, HAS_REQUEST_NO_VALUE)) {
                return;
            }
        }
    }
}

```

由于state=0这里会走
```java
 if (compareAndSet(NO_REQUEST_NO_VALUE, HAS_REQUEST_NO_VALUE)) {
                    return;
                }

```

然后会走

```java
 try {
        t = ObjectHelper.requireNonNull(callable.call(), "The callable returned a null value");
    } catch (Throwable ex) {
        Exceptions.throwIfFatal(ex);
        s.onError(ex);
        return;
    }

```
最后果要的是callable.call()，最后调 deferred.complete(t);结束

