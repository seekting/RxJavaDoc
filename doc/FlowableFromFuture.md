# FlowableFromFuture

```java
    FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "future";
            }
        });

        Flowable<String> flowable = Flowable.fromFuture(future);
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("s=" + s);
            }
        };

        Thread thread = new Thread(future);
        thread.start();
        flowable.subscribe(consumer);

    }

```

直接看FlowableFromFuture的subscribeActual方法，通过future去取。
```java
 @Override
    public void subscribeActual(Subscriber<? super T> s) {
        DeferredScalarSubscription<T> deferred = new DeferredScalarSubscription<T>(s);
        s.onSubscribe(deferred);

        T v;
        try {
            v = unit != null ? future.get(timeout, unit) : future.get();
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            if (!deferred.isCancelled()) {
                s.onError(ex);
            }
            return;
        }
        if (v == null) {
            s.onError(new NullPointerException("The future returned null"));
        } else {
            deferred.complete(v);
        }
    }

```