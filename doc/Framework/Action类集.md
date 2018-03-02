# Action类集

rx.functions下有两种类集，一种是Action家族，一种是Func家族，本节我们重点讲Action


头部类是Function，这里起标记作用
```java
public interface Function {

}

```

Action类没有申明方法，同样也属于标记作用

```java

public interface Action extends Function {

}

```


Action1有一个参数传递
```java

public interface Action1<T> extends Action {
    void call(T t);
}


```

Action1有两个参数传递
```java

public interface Action2<T1, T2> extends Action {
    void call(T1 t1, T2 t2);
}


```

有人会问它能干嘛，在编程当中你会遇到生产-消费模型，最简单的就是

```java
public Object provide(){

return xxx;
}

```
以上代码是一个生产者提供数据返回给消费者，但是这样写做不到异步操作，或是合适的机会返回

于是OnLoadListener，类似一个消费者，产品提供完给onLoadListener消费



然后我就写了第一代:
```java
interface OnLoadListener{
public void onLoad(Object o)
}

```

后来有了泛型，第二代出来了:
```java
interface OnLoadListener<T>{
public void onLoad(T t)
}

```

名字起成onLoad不通用啊，如果别人是onDownloadListener呢，干脆通用一点

```java
public interface Action <T> {
void onAction(T t)

}

```

于是在RxJava里的Action家族诞生了；同理还有一个Consumer也诞生了

```java
public interface Consumer<T> {
    void accept(T t) throws Exception;
}
```

如果要支持多个参数呢?

```java
public interface Action <T1,T2> {
void onAction(T1 t1,T2 t2)

}

```

如果用kotlin或是java1.8的lambda写，会突显它的意义：

```kotlin
load({item->
    println(item)
})
```
好吧，我个人觉得这就是Action家族的由来。


看看Action在RxJava里是怎么用的


Completable家族不提供数据的发射，只提供onComplete，里面用到了Action
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

而Action1和Consumer在RxJava里使用的地方:

Observable.java (旧版本)
```java
public final Disposable subscribe(Consumer<? super T> onNext) {
    return subscribe(onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, Functions.emptyConsumer());
}

```

Observable.java (1.3.0版)
```java
public final Subscription subscribe(final Action1<? super T> onNext) {
    if (onNext == null) {
        throw new IllegalArgumentException("onNext can not be null");
    }

    Action1<Throwable> onError = InternalObservableUtils.ERROR_NOT_IMPLEMENTED;
    Action0 onCompleted = Actions.empty();
    return subscribe(new ActionSubscriber<T>(onNext, onError, onCompleted));
}

```

其实它就是消费者回调


RxJava 里的Action2举例
```java
Observable.just(3, 4, 5, 6)
            .collect(new Func0<List<Integer>>() { //创建数据结构

                @Override
                public List<Integer> call() {
                    return new ArrayList<Integer>();
                }
            }, new Action2<List<Integer>, Integer>() { //收集器
                @Override
                public void call(List<Integer> integers, Integer integer) {
                    integers.add(integer);
                }
            })
            .subscribe(new Action1<List<Integer>>() {
                @Override
                public void call(List<Integer> integers) {

                    for (Integer integer : integers) {
                        System.out.println("integer" + integer);

                    }
                }
            });

```
Action2在这里充当内容提供者，也充当生产者，它是流水作业里的成员

流水作业是:(3,4,5,7)->list(空)->list(3,4,5,7)->print()消费

期间Func0提供了一个空列表，Action2因为支持两个参数，把空列表和3，4，5，7组装好

最终提供给Action1消费