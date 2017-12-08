# FlowableSubscribeOn

异步订阅方式
```java
  Flowable<String> flowable = Flowable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000);
                System.out.println("call:" + Thread.currentThread());
                return "hello";
            }
        });
        FlowableSubscribeOn<String> flowable1 = (FlowableSubscribeOn<String>) flowable.subscribeOn(Schedulers.io());
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
输出结果:
```Console
Thread[main,5,main]
call:Thread[RxCachedThreadScheduler-1,5,main]
accept:Thread[RxCachedThreadScheduler-1,5,main]
s=hello

Process finished with exit code 0


```
这里面发现，不管订阅逻辑和accept方法的回调都是在异步的。来看看代码
```java
  public final Flowable<T> subscribeOn(@NonNull Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return subscribeOn(scheduler, !(this instanceof FlowableCreate));
    }

```

## 源码解析
返回一个FlowableSubscribeOn对象
```java
  public final Flowable<T> subscribeOn(@NonNull Scheduler scheduler, boolean requestOn) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new FlowableSubscribeOn<T>(this, scheduler, requestOn));
    }

```

订阅方法:
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

```java
 public final void subscribe(FlowableSubscriber<? super T> s) {
        ObjectHelper.requireNonNull(s, "s is null");
        try {
            Subscriber<? super T> z = RxJavaPlugins.onSubscribe(this, s);

            ObjectHelper.requireNonNull(z, "Plugin returned null Subscriber");

            subscribeActual(z);
            //...ignore code

```

调用FlowableSubscribe的subscribeActual方法
```java
    @Override
    public void subscribeActual(final Subscriber<? super T> s) {
        Scheduler.Worker w = scheduler.createWorker();
        final SubscribeOnSubscriber<T> sos = new SubscribeOnSubscriber<T>(s, w, source, nonScheduledRequests);
        //这里的s就是LambdaSubscriber
        s.onSubscribe(sos);

        w.schedule(sos);
    }

```
subscribeActual分两步
1. s.onSubscribe(sos):不做任何有意义的事情
1. w.schedule(sos):异步去实现scribe细节

## 重点分析w.schedule(sos)
首先是调试器,这里的调试器用的是IO;Schedulers.java
```java
 IO = RxJavaPlugins.initIoScheduler(new IOTask());

```

可以看出来它只是一个工厂，能返回一个Scheduler
```java
 static final class IOTask implements Callable<Scheduler> {
        @Override
        public Scheduler call() throws Exception {
            return IoHolder.DEFAULT;
        }
    }

```

创建一个Worker
```java
  Scheduler.Worker w = scheduler.createWorker();//通过scheduler创建Worker

```
IOScheduler里的worker是EventLoopWorker
```java
 public Worker createWorker() {
        return new EventLoopWorker(pool.get());
    }

```
EventLoopWorker里代理ThreadWorker实现schedule方法，代理的目的是维护一个Worker池，值得学习！

```java
static final class EventLoopWorker extends Scheduler.Worker {
        private final CompositeDisposable tasks;
        private final CachedWorkerPool pool;
        private final ThreadWorker threadWorker;

        final AtomicBoolean once = new AtomicBoolean();

        EventLoopWorker(CachedWorkerPool pool) {
            this.pool = pool;
            this.tasks = new CompositeDisposable();
            this.threadWorker = pool.get();
        }

        @Override
        public void dispose() {
            if (once.compareAndSet(false, true)) {
                tasks.dispose();

                // releasing the pool should be the last action
                pool.release(threadWorker);
            }
        }

        @Override
        public boolean isDisposed() {
            return once.get();
        }

        @NonNull
        @Override
        public Disposable schedule(@NonNull Runnable action, long delayTime, @NonNull TimeUnit unit) {
            if (tasks.isDisposed()) {
                // don't schedule, we are unsubscribed
                return EmptyDisposable.INSTANCE;
            }

            return threadWorker.scheduleActual(action, delayTime, unit, tasks);
        }
    }

```

而这里的threadWorker是ThreadWorker它的父类是NewThreadWorker，有兴趣可以看看CachedWorkerPool.java，这里不深入了

NewThreadWorker里最终调用的是executor.submit(..)
> 这里又出现一个RxJavaPlugins让开发者可以把run转换成自己定制的runnable,值得学习!

```java
@NonNull
public ScheduledRunnable scheduleActual(final Runnable run, long delayTime, @NonNull TimeUnit unit, @Nullable DisposableContainer parent) {
    Runnable decoratedRun = RxJavaPlugins.onSchedule(run);

    ScheduledRunnable sr = new ScheduledRunnable(decoratedRun, parent);

    if (parent != null) {
        if (!parent.add(sr)) {
            return sr;
        }
    }

    Future<?> f;
    try {
        if (delayTime <= 0) {
            f = executor.submit((Callable<Object>)sr);
        } else {
            f = executor.schedule((Callable<Object>)sr, delayTime, unit);
        }
        sr.setFuture(f);
    } catch (RejectedExecutionException ex) {
        if (parent != null) {
            parent.remove(sr);
        }
        RxJavaPlugins.onError(ex);
    }

    return sr;
}

```
NewThreadWorker.java
```java
 public ScheduledRunnable scheduleActual(final Runnable run, long delayTime, @NonNull TimeUnit unit, @Nullable DisposableContainer parent) {
        Runnable decoratedRun = RxJavaPlugins.onSchedule(run);

        ScheduledRunnable sr = new ScheduledRunnable(decoratedRun, parent);

        if (parent != null) {
            if (!parent.add(sr)) {
                return sr;
            }
        }

        Future<?> f;
        try {
            if (delayTime <= 0) {
                f = executor.submit((Callable<Object>)sr);
            } else {
                f = executor.schedule((Callable<Object>)sr, delayTime, unit);
            }
            sr.setFuture(f);
        } catch (RejectedExecutionException ex) {
            if (parent != null) {
                parent.remove(sr);
            }
            RxJavaPlugins.onError(ex);
        }

        return sr;
    }

```

ScheduledRunnable.java里的run其实就是调用了actual.run怕里的actual就是SubscribeOnSubscriber
```java
public void run() {
    lazySet(THREAD_INDEX, Thread.currentThread());
    try {
        try {
            actual.run();
        } catch (Throwable e) {
            // Exceptions.throwIfFatal(e); nowhere to go
            RxJavaPlugins.onError(e);
        }
    } finally {
        lazySet(THREAD_INDEX, null);
        Object o = get(PARENT_INDEX);
        if (o != PARENT_DISPOSED && compareAndSet(PARENT_INDEX, o, DONE) && o != null) {
            ((DisposableContainer)o).delete(this);
        }

        for (;;) {
            o = get(FUTURE_INDEX);
            if (o == SYNC_DISPOSED || o == ASYNC_DISPOSED || compareAndSet(FUTURE_INDEX, o, DONE)) {
                break;
            }
        }
    }
}

```
SubscribeOnSubscriber的run方法调的是src.subscribe()，src就是FlowableCallable<String> flowable也就是异步调用了FlowableCallable的subscribe方法</br>
所以实现逻辑都变成异步了，包括onSubscribe回调
```java


public void run() {
        lazySet(Thread.currentThread());
        Publisher<T> src = source;
        source = null;
        src.subscribe(this);
    }

```


