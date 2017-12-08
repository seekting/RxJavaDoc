# FlowableObserveOn

## 例子
通过ObserveOn实现accept回调的时候进行线程切换
```java
 Flowable<String> flowable = Flowable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000);
                System.out.println("call:" + Thread.currentThread());
                return "hello";
            }
        });
        FlowableObserveOn<String> flowable1 = (FlowableObserveOn<String>) flowable.observeOn(Schedulers.io());
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept:" + Thread.currentThread());
                System.out.println("s=" + s);

            }
        };
        flowable1.subscribe(consumer);
        System.out.println(Thread.currentThread());


        Thread.sleep(6000);

```


```Console
call:Thread[main,5,main]
Thread[main,5,main]
accept:Thread[RxCachedThreadScheduler-1,5,main]
s=hello

```

## 源码分析


### subscribeActual
前面已经讲过，subscribe最终会走到子类的subscribeActual方法,我们看FlowableObserveOn的subscribeActual方法

```java
  @Override
    public void subscribeActual(Subscriber<? super T> s) {
        Worker worker = scheduler.createWorker();

        if (s instanceof ConditionalSubscriber) {
            source.subscribe(new ObserveOnConditionalSubscriber<T>(
                    (ConditionalSubscriber<? super T>) s, worker, delayError, prefetch));
        } else {
            source.subscribe(new ObserveOnSubscriber<T>(s, worker, delayError, prefetch));
        }
    }


```

1. 首先通过createWorker创建Worker(这个Worker能通过schedule去运行一个callable)
1. 然后调用source.subscribe()这里面的订阅者是自己new 出来的ObserveOnSubscriber

### ObserveOnSubscriber
ObserveOnSubscriber的onSubscribe方法
```java
 @Override
public void onSubscribe(Subscription s) {
    if (SubscriptionHelper.validate(this.s, s)) {
        this.s = s;

        if (s instanceof QueueSubscription) {
            @SuppressWarnings("unchecked")
            QueueSubscription<T> f = (QueueSubscription<T>) s;

            int m = f.requestFusion(ANY | BOUNDARY);

            if (m == SYNC) {
                sourceMode = SYNC;
                queue = f;
                done = true;

                actual.onSubscribe(this);
                return;
            } else
            if (m == ASYNC) {
                sourceMode = ASYNC;
                queue = f;

                actual.onSubscribe(this);

                s.request(prefetch);

                return;
            }
        }

        queue = new SpscArrayQueue<T>(prefetch);

        actual.onSubscribe(this);

        s.request(prefetch);
    }
}

```
这里的m=Async<br>
然后走 actual.onSubscribe(this);这里的actual就是LambdaSubscriber，LambdaSubscriber.onSubscribe最终会走到ObserveOnSubscriber的request方法
```java
 @Override
    public final void request(long n) {
        if (SubscriptionHelper.validate(n)) {
            BackpressureHelper.add(requested, n);
            trySchedule();
        }
    }

```

request里走trySchedule
```java
final void trySchedule() {
    if (getAndIncrement() != 0) {
        return;
    }
    worker.schedule(this);
}

```
schedule方法调用之后会走run方法
```java
@Override
public final void run() {
    if (outputFused) {
        runBackfused();
    } else if (sourceMode == SYNC) {
        runSync();
    } else {
        runAsync();
    }
}

```

```java
@Override
void runAsync() {
    int missed = 1;

    final Subscriber<? super T> a = actual;
    final SimpleQueue<T> q = queue;

    long e = produced;

    for (;;) {

        long r = requested.get();

        while (e != r) {
            boolean d = done;
            T v;

            try {
                v = q.poll();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);

                s.cancel();
                q.clear();

                a.onError(ex);
                worker.dispose();
                return;
            }

            boolean empty = v == null;

            if (checkTerminated(d, empty, a)) {
                return;
            }

            if (empty) {
                break;
            }

            a.onNext(v);

            e++;
            if (e == limit) {
                if (r != Long.MAX_VALUE) {
                    r = requested.addAndGet(-e);
                }
                s.request(e);
                e = 0L;
            }
        }

        if (e == r && checkTerminated(done, q.isEmpty(), a)) {
            return;
        }

        int w = get();
        if (missed == w) {
            produced = e;
            missed = addAndGet(-missed);
            if (missed == 0) {
                break;
            }
        } else {
            missed = w;
        }
    }
}

```
 a.onNext(v) 这里的a是LambdaSubscriber，v是"hello"在异步线程里实现了onNext的回调

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




