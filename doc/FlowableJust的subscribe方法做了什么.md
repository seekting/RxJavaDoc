# FlowableJust的subscribe方法做了什么

subscribe方法最终会走的地方:
```java
public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
        Action onComplete, Consumer<? super Subscription> onSubscribe) {
    ObjectHelper.requireNonNull(onNext, "onNext is null");
    ObjectHelper.requireNonNull(onError, "onError is null");
    ObjectHelper.requireNonNull(onComplete, "onComplete is null");
    ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");

    LambdaSubscriber<T> ls = new LambdaSubscriber<T>(onNext, onError, onComplete, onSubscribe);

    subscribe(ls);

    return ls;
}

```

说明它有四个重要因子:
1. Consumer类型 onNext，我们原生的消费者泛型为T，表示该内容提供者能提供T给onNext
1. Consumer类型 onError，能接收Throwable的一个消费者
1. Action类型 onComplete,Action只有一个函数run，运行结束的回调
1. Consumer类型，能接受Subscription的消费者

通过以上四种因子，我们至少知道，这个Flowable能提供3种产品给消费者，还会给一个run回调
这3种产品分别是T，Throwable，Subscription。


## T是什么？
```java

Flowable.just("Hello world")
```
以上代码表示，Flowable能提供的T是"Hello World"字符

## LambdaSubscriber

然后把这四个对象组装到LambdaSubscriber，调用subscribe(LambdaSubscriber)
```java

LambdaSubscriber<T> ls = new LambdaSubscriber<T>(onNext, onError, onComplete, onSubscribe);

subscribe(ls);

```

其实也就是把消费者改装成订阅者嘛,而订阅者里有多个消费者，因为Flowable还是能提供一些商品的

```java
 public final void subscribe(FlowableSubscriber<? super T> s) {
ObjectHelper.requireNonNull(s, "s is null");
try {
    Subscriber<? super T> z = RxJavaPlugins.onSubscribe(this, s);

    ObjectHelper.requireNonNull(z, "Plugin returned null Subscriber");

    subscribeActual(z);
}
//...ignore code

```
又开始用RxJavaPlugins去改装内容提供者和消费者<br>
改装后调用subscribeActual方法,而subscribeActual方法由子类去完成，也就是FlowableJust的subscribeActual

```java
 public final class FlowableJust<T> extends Flowable<T> implements ScalarCallable<T> {
     private final T value;
     public FlowableJust(final T value) {
         this.value = value;
     }

     @Override
     protected void subscribeActual(Subscriber<? super T> s) {
         s.onSubscribe(new ScalarSubscription<T>(s, value));
     }

     @Override
     public T call() {
         return value;
     }
 }



```
FlowableJust的代码很简单，这里泛型了一个T，用value保持引用，然后调订阅者(包含3个消费者一个Action)的onSubscribe方法
也就是LambdaSubscriber的方法，这里的Subscription你先把它想成是一个商品，它有value

```java
@Override
public void onSubscribe(Subscription s) {
    if (SubscriptionHelper.setOnce(this, s)) {
        try {
            onSubscribe.accept(this);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            s.cancel();
            onError(ex);
        }
    }
}
```

它做的事情就是调用this.request()方法，因为这里的onSubscribe其实是
```java
    public enum RequestMax implements Consumer<Subscription> {
        INSTANCE;
        @Override
        public void accept(Subscription t) throws Exception {
            t.request(Long.MAX_VALUE);
        }
    }

```
所以就会调用request
```java
 @Override
    public void request(long n) {
        get().request(n);
    }

```
这里的get()是因为之前set了
```java
SubscriptionHelper.setOnce(this, s)


```
而s就是ScalarSubscription

ScalarSubscription的request其实就是调用LambdaSubscriber的onNext和onComplete()
```java
  @Override
    public void request(long n) {
        if (!SubscriptionHelper.validate(n)) {
            return;
        }
        if (compareAndSet(NO_REQUEST, REQUESTED)) {
            Subscriber<? super T> s = subscriber;

            s.onNext(value);
            if (get() != CANCELLED) {
                s.onComplete();
            }
        }

    }

```
LambdaSubscriber的onNext和onComplete会把处理后的商品返回给前面提到的那几个消费者，并告诉回调，我处理完毕

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

    @Override
    public void onError(Throwable t) {
        if (get() != SubscriptionHelper.CANCELLED) {
            lazySet(SubscriptionHelper.CANCELLED);
            try {
                onError.accept(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(new CompositeException(t, e));
            }
        } else {
            RxJavaPlugins.onError(t);
        }
    }

    @Override
    public void onComplete() {
        if (get() != SubscriptionHelper.CANCELLED) {
            lazySet(SubscriptionHelper.CANCELLED);
            try {
                onComplete.run();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
        }
    }

```


这里面有4个角色：
1. 内容提供者它能提供内容给订阅者
1. 订阅者会有几个消费者，订阅者处理这个商品会有些细节，处理完了会告诉相应的消费者
1. 消费者目前看有两种，一种是消费异常，一种是开发人员定制的消费者。
1. Action即整件事情做完的回调




