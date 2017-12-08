# FlowableBlockingSubscribe
FlowableBlockingSubscribe 等待订阅执行完

## 例子

```java
Flowable.fromCallable(new Callable<String>() {
    @Override
    public String call() throws Exception {
        System.out.println("begin call");
        Thread.sleep(2000);
        System.out.println("end call");
        return "hello";
    }
}).subscribeOn(Schedulers.io())
        .blockingSubscribe();

System.out.println("gogogo");

```

```Console
begin call
end call
gogogo
```

看log能看出来，call调用之后主线程代码才往下执行。

## 源码解析


```java
  public final void blockingSubscribe() {
        FlowableBlockingSubscribe.subscribe(this);
    }

```
LambdaSubscriber添加了一些BlockingIgnoringReceiver类型的callback,执行订阅逻辑(异步)，之后调用awaitForComplete
```java
public static <T> void subscribe(Publisher<? extends T> o) {
        BlockingIgnoringReceiver callback = new BlockingIgnoringReceiver();
        LambdaSubscriber<T> ls = new LambdaSubscriber<T>(Functions.emptyConsumer(),
        callback, callback, Functions.REQUEST_MAX);

        o.subscribe(ls);

        BlockingHelper.awaitForComplete(callback, ls);
        Throwable e = callback.error;
        if (e != null) {
            throw ExceptionHelper.wrapOrThrow(e);
        }
    }

```

awaitForComplete方法调用CountDownLatch.await阻塞当前线程，直到异步线程调用CountDownLatch的countDown,这里的CountDownLatch就是BlockingIgnoringReceiver
```java
public static void awaitForComplete(CountDownLatch latch, Disposable subscription) {
    if (latch.getCount() == 0) {
        return;
    }
    try {
        verifyNonBlocking();
        latch.await();
    } catch (InterruptedException e) {
        subscription.dispose();
        throw new IllegalStateException("Interrupted while waiting for subscription to complete.", e);
    }
}


```
BlockingIgnoringReceiver.java继承CountDownLatch,当accept被调用的时候就会调用countDown

```java
public final class BlockingIgnoringReceiver
extends CountDownLatch
implements Consumer<Throwable>, Action {
    public Throwable error;

    public BlockingIgnoringReceiver() {
        super(1);
    }

    @Override
    public void accept(Throwable e) {
        error = e;
        countDown();
    }

    @Override
    public void run() {
        countDown();
    }
}

```