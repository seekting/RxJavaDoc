# FlowableFromArray


来看看提供一个数组：
```java
Flowable<String> j = Flowable.just("1", "2", "3");

```


```java
public static <T> Flowable<T> just(T item1, T item2, T item3) {
    ObjectHelper.requireNonNull(item1, "The first item is null");
    ObjectHelper.requireNonNull(item2, "The second item is null");
    ObjectHelper.requireNonNull(item3, "The third item is null");

    return fromArray(item1, item2, item3);
}


public static <T> Flowable<T> fromArray(T... items) {
    ObjectHelper.requireNonNull(items, "items is null");
    if (items.length == 0) {
        return empty();
    }
    if (items.length == 1) {
        return just(items[0]);
    }
    return RxJavaPlugins.onAssembly(new FlowableFromArray<T>(items));
}
```


```java
       public static void main(String args[]) {
           Flowable<String> flowable = Flowable.just("1", "2", "3");
           Consumer<String> consumer = new Consumer<String>() {
               @Override
               public void accept(String s) throws Exception {
                   System.out.println("s=" + s);
               }
           };
           flowable.subscribe(consumer);

       }

```
输出的终于不一样了,它调用了3次accept
```Console
s=1
s=2
s=3

Process finished with exit code 0

```

而它的订阅细节又是什么？

```java
public void subscribeActual(Subscriber<? super T> s) {
    if (s instanceof ConditionalSubscriber) {
        s.onSubscribe(new ArrayConditionalSubscription<T>(
                (ConditionalSubscriber<? super T>)s, array));
    } else {
        s.onSubscribe(new ArraySubscription<T>(s, array));//走的这个分支
    }
}

```
结果它new ArraySubscription对像,也就是LambdaSubscriber调用onSubscribe(ArraySubscription)
LambdaSubscriber调用自己的onSubscribe.accept(this)
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

前面分析了LambdaSubscriber的onSubscribe默认是一个单例:
```java
   public enum RequestMax implements Consumer<Subscription> {
        INSTANCE;
        @Override
        public void accept(Subscription t) throws Exception {
            t.request(Long.MAX_VALUE);
        }
    }

```

从而调用LambdaSubscriber.request，而LambdaSubscriber的requset调用的是get().request()

```java
  @Override
    public void request(long n) {
        get().request(n);
    }

```

前面说了SubscriptionHelper.setOnce(this, s)这里的s就是get()的返回，所以最终走到ArraySubscription的request()

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

request会根据n来判断走fast还是slow

```java
 void fastPath() {
            T[] arr = array;
            int f = arr.length;
            Subscriber<? super T> a = actual;

            for (int i = index; i != f; i++) {
                if (cancelled) {
                    return;
                }
                T t = arr[i];
                if (t == null) {
                    a.onError(new NullPointerException("array element is null"));
                    return;
                } else {
                    a.onNext(t);
                }
            }
            if (cancelled) {
                return;
            }
            a.onComplete();
        }

```
可以看到它是遍历数组走onNext()，也就是走LambdaSubscriber的onNext，最终调到consumer.onAccept方法

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

