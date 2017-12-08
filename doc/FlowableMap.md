# FlowableMap


## 例子
FlowableMap能把一种类型的数据转换成另一种数据，以下代码是将String转换成Person

```java
class Person {
    String name;

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}

```
```java


 Flowable.just("kate")
                .map(new Function<String, Person>() {
                    @Override
                    public Person apply(String s) throws Exception {
                        return new Person(s);
                    }
                }).subscribe(new Consumer<Person>() {
            @Override
            public void accept(Person s) throws Exception {
                System.out.println("s=" + s);
            }
        });

```

```Console
s=Person{name='kate'}

```

## 源码分析
创建FlowableMap
```java
public final <R> Flowable<R> map(Function<? super T, ? extends R> mapper) {
    ObjectHelper.requireNonNull(mapper, "mapper is null");
    return RxJavaPlugins.onAssembly(new FlowableMap<T, R>(this, mapper));
}

```
subscribeActual方法创建了一个MapSubscriber的订阅者，并和源Flowable关联
```java
protected void subscribeActual(Subscriber<? super U> s) {
    if (s instanceof ConditionalSubscriber) {
        source.subscribe(new MapConditionalSubscriber<T, U>((ConditionalSubscriber<? super U>)s, mapper));
    } else {
        source.subscribe(new MapSubscriber<T, U>(s, mapper));
    }
}

```
Flowable走的是FlowableJust的subscribeActual方法

这里的s是MapSubscriber
FlowableJust.java

```java
 @Override
    protected void subscribeActual(Subscriber<? super T> s) {
        s.onSubscribe(new ScalarSubscription<T>(s, value));
    }

```
也就是调用MapSubscriber.onSubscribe(new ScalarSubscription)
```java
public final void onSubscribe(Subscription s) {
    if (SubscriptionHelper.validate(this.s, s)) {

        this.s = s;
        if (s instanceof QueueSubscription) {
            this.qs = (QueueSubscription<T>)s;
        }

        if (beforeDownstream()) {

            actual.onSubscribe(this);//actual是LambdaSubscriber

            afterDownstream();
        }

    }
}

```
LambdaSubscriber.onSubscribe会走MapSubscriber的request最后会走到
```java
   @Override
    public void request(long n) {
        s.request(n);//这里的s是ScalarSubscription
    }


```
ScalarSubscription.request调用的是MapSubscriber的onNext
```java
@Override
public void request(long n) {
    if (!SubscriptionHelper.validate(n)) {
        return;
    }
    if (compareAndSet(NO_REQUEST, REQUESTED)) {
        Subscriber<? super T> s = subscriber;

        s.onNext(value);
        if (get() != CANCELLED) {
            s.onComplete();
        }
    }

}

```
```java
static final class MapSubscriber<T, U> extends BasicFuseableSubscriber<T, U> {
    final Function<? super T, ? extends U> mapper;

    MapSubscriber(Subscriber<? super U> actual, Function<? super T, ? extends U> mapper) {
        super(actual);
        this.mapper = mapper;
    }

    @Override
    public void onNext(T t) {
        if (done) {
            return;
        }

        if (sourceMode != NONE) {
            actual.onNext(null);
            return;
        }

        U v;

        try {
            v = ObjectHelper.requireNonNull(mapper.apply(t), "The mapper function returned a null value.");
        } catch (Throwable ex) {
            fail(ex);
            return;
        }
        actual.onNext(v);
    }

```

mapper.apply(t)就是调用apply转换把String转换成Person
```java
@Override
public Person apply(String s) throws Exception {
    return new Person(s);
}

```

