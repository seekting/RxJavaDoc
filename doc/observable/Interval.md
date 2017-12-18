# IntervalObservable

## demo
```java
public class IntervalObservableDemo {
    public static void main(String args[]) throws Throwable {

        Observable<Long> observable = Observable.interval(2000, TimeUnit.MILLISECONDS);
        observable.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("value=" + aLong);
            }
        });

        Thread.sleep(5000);
    }
}

```

## 输出
```Console
value=0
value=1

Process finished with exit code 0

```

从输出结果来看，它是每隔2秒时间调用一次accept方法，直到进程死亡。有点像定时任务。

## 源码解析

```java
  public static Observable<Long> interval(long period, TimeUnit unit) {
        return interval(period, period, unit, Schedulers.computation());
    }

```
Schedulers.computation调度用的是computation所在的线程，然后调用同名方法

```java
public static Observable<Long> interval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
    ObjectHelper.requireNonNull(unit, "unit is null");
    ObjectHelper.requireNonNull(scheduler, "scheduler is null");

    return RxJavaPlugins.onAssembly(new ObservableInterval(Math.max(0L, initialDelay), Math.max(0L, period), unit, scheduler));
}

```

其实就是创建ObservableInterval，RxJavaPlugin只是给一次机会给用户去自定义这个ObservableInterval特性。显得更灵活一点
```java
public final class ObservableInterval extends Observable<Long> {
    final Scheduler scheduler;
    final long initialDelay;
    final long period;
    final TimeUnit unit;


```
通过四个成员变量来记录定时任务的基本信息。当调用subscribe方法时，最终调用的是subscribeActual方法。

```java
 @Override
    public void subscribeActual(Observer<? super Long> s) {
        IntervalObserver is = new IntervalObserver(s);
        s.onSubscribe(is);

        Scheduler sch = scheduler;

        if (sch instanceof TrampolineScheduler) {
            Worker worker = sch.createWorker();
            is.setResource(worker);
            worker.schedulePeriodically(is, initialDelay, period, unit);
        } else {
            Disposable d = sch.schedulePeriodicallyDirect(is, initialDelay, period, unit);
            is.setResource(d);
        }
    }

```

创建了新的IntervalObserver，s是LambdaObserver.
LambdaObserver方法：
```java
    @Override
    public void onSubscribe(Disposable s) {
        if (DisposableHelper.setOnce(this, s)) {
            try {
                onSubscribe.accept(this);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                s.dispose();
                onError(ex);
            }
        }
    }

```
这里的onSubscribe是empty:
```java
static final class EmptyConsumer implements Consumer<Object> {
    @Override
    public void accept(Object v) { }

    @Override
    public String toString() {
        return "EmptyConsumer";
    }
}

```
也就是说调用 s.onSubscribe(is);不会执行什么有意义的代码。然后看：
```java
Scheduler sch = scheduler;

    if (sch instanceof TrampolineScheduler) {
        Worker worker = sch.createWorker();
        is.setResource(worker);
        worker.schedulePeriodically(is, initialDelay, period, unit);
    } else {
        Disposable d = sch.schedulePeriodicallyDirect(is, initialDelay, period, unit);
        is.setResource(d);
    }

```

demo环境会走
```java
Disposable d = sch.schedulePeriodicallyDirect(is, initialDelay, period, unit);
is.setResource(d);
```
其实就是schedule执行runnable的过程
```java
Future<?> f = executor.scheduleAtFixedRate(task, initialDelay, period, unit);
task.setFuture(f);

```
最终会走到executor.scheduleAtFixedRate，这里的executor是ScheduledExecutorService，当它在指定时间内就会执行run方法
```java
@Override
public void run() {
    if (get() != DisposableHelper.DISPOSED) {
        actual.onNext(count++);
    }
}

```
最终把count++传递给接收方