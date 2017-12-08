# FlowableFlatMap

## 例子

```java
package com.seekting.rxjava;


import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/12/5.
 */

public class FlowableFlatMapDemo {
    public static void main(String args[]) {
        Flowable.range(7, 13).flatMap(new Function<Integer, Publisher<Person>>() {
            @Override
            public Publisher<Person> apply(Integer s) throws Exception {
                System.out.print("s=" + s);
                return Flowable.just(new Person(s));
            }
        }).flatMap(new Function<Person, Publisher<School>>() {
            @Override
            public Publisher<School> apply(Person person) throws Exception {
                Publisher publisher = null;
                if (person.age < 11) {
                    publisher = Flowable.just(new School("small School"));
                } else if (person.age < 18) {
                    publisher = Flowable.just(new School("mid School"));

                } else if (person.age < 21) {
                    publisher = Flowable.just(new School("big School"));
                } else {
                    publisher = Flowable.just(new School("big School"));
                }
                return publisher;
            }
        }).subscribe(new Consumer<School>() {
            @Override
            public void accept(School school) throws Exception {
                System.out.println("school=" + school);
            }
        });
    }

    static class Person {
        int age;

        public Person(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age='" + age + '\'' +
                    '}';
        }
    }

    static class School {
        String name;

        public School(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "School{" +
                    "name='" + name + '\'' +
                    '}';
        }


    }

}


```
输出：
```Console
s=7school=School{name='small School'}
s=8school=School{name='small School'}
s=9school=School{name='small School'}
s=10school=School{name='small School'}
s=11school=School{name='mid School'}
s=12school=School{name='mid School'}
s=13school=School{name='mid School'}
s=14school=School{name='mid School'}
s=15school=School{name='mid School'}
s=16school=School{name='mid School'}
s=17school=School{name='mid School'}
s=18school=School{name='big School'}
s=19school=School{name='big School'}

Process finished with exit code 0

```
通过demo可以得出，flatMap可以把之前提供的商品转成另外一个商品，而新的商品提供者可以自己定义
## 源码分析

```java
public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper) {
    return flatMap(mapper, false, bufferSize(), bufferSize());
}



```
返回的是FlowableFlatMap
```java
public final <R> Flowable<R> flatMap(
        Function<? super T, ? extends Publisher<? extends R>> mapper,
        boolean delayErrors, int maxConcurrency, int bufferSize) {
        //...ignore code
    return RxJavaPlugins.onAssembly(new FlowableFlatMap<T, R>(this, mapper, delayErrors, maxConcurrency, bufferSize));
}

```

FlowableFlatMap.java订阅方法执行后会走subscribeActual方法

```java
@Override
protected void subscribeActual(Subscriber<? super U> s) {
    if (FlowableScalarXMap.tryScalarXMapSubscribe(source, s, mapper)) {
        return;
    }
    source.subscribe(subscribe(s, mapper, delayErrors, maxConcurrency, bufferSize));
}

```
FlowableFlatMap的static subscribe方法返回了一个MergeSubscriber对象

```java
public static <T, U> FlowableSubscriber<T> subscribe(Subscriber<? super U> s,
    Function<? super T, ? extends Publisher<? extends U>> mapper,
    boolean delayErrors, int maxConcurrency, int bufferSize) {
return new MergeSubscriber<T, U>(s, mapper, delayErrors, maxConcurrency, bufferSize);
}


```

source调用subscribe后会走source的subscribeActual方法，这里的source.subscribeActual是FlowableRange

回顾FlowableRange的订阅逻辑

```java
  @Override
    public void subscribeActual(Subscriber<? super Integer> s) {
        if (s instanceof ConditionalSubscriber) {
            s.onSubscribe(new RangeConditionalSubscription(
                    (ConditionalSubscriber<? super Integer>)s, start, end));
        } else {
            s.onSubscribe(new RangeSubscription(s, start, end));
        }
    }

```
那就是调用MergeSubscriber.onSubscribe(new RangeSubscription(s, start, end))
这里的actual就是LambdaSubscriber，s是RangeSubscription
```java
@Override
public void onSubscribe(Subscription s) {
    if (SubscriptionHelper.validate(this.s, s)) {
        this.s = s;
        actual.onSubscribe(this);
        if (!cancelled) {
            if (maxConcurrency == Integer.MAX_VALUE) {
                s.request(Long.MAX_VALUE);
            } else {
                s.request(maxConcurrency);
            }
        }
    }
}

```
 actual.onSubscribe(this);就是LambdaSubscriber.onSubscribe(this)然后LambdaSubscriber会调用MergeSubscriber.request

 ```java
@Override
 public void request(long n) {
     if (SubscriptionHelper.validate(n)) {
         BackpressureHelper.add(requested, n);
         drain();
     }
 }

 ```
 没做什么实质性的东西

 再看  s.request(Long.MAX_VALUE);也就是RangeSubscription.request(maxConcurrency);<br>
 RangeSubscription会回调MergeSubscriber.onNext，最终会调用mapper.apply(t)来得到Publisher<br>
 如果publisher是Callable就直接调call

 ```java
public void onNext(T t) {
     //...ignore code
        Publisher<? extends U> p;
        try {
            p = ObjectHelper.requireNonNull(mapper.apply(t), "The mapper returned a null Publisher");
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            s.cancel();
            onError(e);
            return;
        }
        if (p instanceof Callable) {
            U u;

            try {
                u  = ((Callable<U>)p).call();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                errs.addThrowable(ex);
                drain();
                return;
            }

            if (u != null) {
                tryEmitScalar(u);
            } else {
                if (maxConcurrency != Integer.MAX_VALUE && !cancelled
                        && ++scalarEmitted == scalarLimit) {
                    scalarEmitted = 0;
                    s.request(scalarLimit);
                }
            }
        } else {
            InnerSubscriber<T, U> inner = new InnerSubscriber<T, U>(this, uniqueId++);
            if (addInner(inner)) {
                p.subscribe(inner);
            }
        }
    }

 ```
 然后调用tryEmitScalar，再调用  actual.onNext(value);这里的actual是LambdaSubscriber
 ```java
void tryEmitScalar(U value) {
     if (get() == 0 && compareAndSet(0, 1)) {
         long r = requested.get();
         SimpleQueue<U> q = queue;
         if (r != 0L && (q == null || q.isEmpty())) {
             actual.onNext(value);
             if (r != Long.MAX_VALUE) {
                 requested.decrementAndGet();
             }
             if (maxConcurrency != Integer.MAX_VALUE && !cancelled
                     && ++scalarEmitted == scalarLimit) {
                 scalarEmitted = 0;
                 s.request(scalarLimit);
             }
         } else {
             if (q == null) {
                 q = getMainQueue();
             }
             if (!q.offer(value)) {
                 onError(new IllegalStateException("Scalar queue full?!"));
                 return;
             }
         }
         if (decrementAndGet() == 0) {
             return;
         }
     } else {
         SimpleQueue<U> q = getMainQueue();
         if (!q.offer(value)) {
             onError(new IllegalStateException("Scalar queue full?!"));
             return;
         }
         if (getAndIncrement() != 0) {
             return;
         }
     }
     drainLoop();
 }


 ```

