# Timmer

## Demo
```java
public class ObservableTimerDemo {
public static void main(String args[]) throws Throwable {
    Observable<Long> timerObservable = Observable.timer(1000, TimeUnit.MILLISECONDS);
    System.out.println("begin");
    timerObservable.subscribe(new Consumer<Long>() {
        @Override
        public void accept(Long aLong) throws Exception {
            System.out.println("aLong=" + aLong);
        }
    });

    Thread.sleep(6000);
}
}


```

```Console
begin
aLong=0

Process finished with exit code 0

```

## 源码解析
调用重载函数:默认走的是computation线程
```java
public static Observable<Long> timer(long delay, TimeUnit unit) {
    return timer(delay, unit, Schedulers.computation());
}

```
创建ObservableTimer对象，确保delay大于0
```java
public static Observable<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
    ObjectHelper.requireNonNull(unit, "unit is null");
    ObjectHelper.requireNonNull(scheduler, "scheduler is null");

    return RxJavaPlugins.onAssembly(new ObservableTimer(Math.max(delay, 0L), unit, scheduler));
}

```
走subscribeActual方法
```java
@Override
public void subscribeActual(Observer<? super Long> s) {
    TimerObserver ios = new TimerObserver(s);
    s.onSubscribe(ios);//如果是LambdaObserver这里会是空实现

    Disposable d = scheduler.scheduleDirect(ios, delay, unit);

    ios.setResource(d);
}

```
然后走调度scheduleDirect这里的调度最终会在指定的时间调用TimerObserver.run方法,最终传了hardCode：0给订阅者

```java

@Override
public void run() {
    if (!isDisposed()) {
        actual.onNext(0L);
        lazySet(EmptyDisposable.INSTANCE);
        actual.onComplete();
    }
}

```