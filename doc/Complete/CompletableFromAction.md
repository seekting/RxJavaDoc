# Completable

Completable不管数据发射，只管onComplete

demo

```java
Completable
        .fromAction(new Action() {
            @Override
            public void run() throws Exception {

                System.out.println("run1" + Thread.currentThread());
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.newThread())
        .subscribe(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("run2" + Thread.currentThread());
            }
        });

try {
    Thread.sleep(3000);
} catch (InterruptedException e) {
    e.printStackTrace();
}

```

输出
```Console
run1Thread[RxCachedThreadScheduler-1,5,main]
run2Thread[RxNewThreadScheduler-1,5,main]

```