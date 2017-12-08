# FlowableFromPublisher

Flowable本就是Publisher，它本有提供数据的能力
```java
public abstract class Flowable<T> implements Publisher<T> {

```

但是也可以让别的Publisher来提供数据，它自己充当一个代理的作用。
```java
 Publisher<String> publisher = new Publisher<String>() {
            @Override
            public void subscribe(Subscriber s) {
                s.onNext("s");

            }
        };
        Flowable<String> flowable = Flowable.fromPublisher(publisher);
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("s=" + s);
            }
        };
        flowable.subscribe(consumer);

```

创建FlowableFromPublisher

```java
    public static <T> Flowable<T> fromPublisher(final Publisher<? extends T> source) {
        if (source instanceof Flowable) {
            return RxJavaPlugins.onAssembly((Flowable<T>)source);
        }
        ObjectHelper.requireNonNull(source, "publisher is null");

        return RxJavaPlugins.onAssembly(new FlowableFromPublisher<T>(source));
    }


```

subscribeActual方法直接调用publisher.subscribe(s);

```java

public final class FlowableFromPublisher<T> extends Flowable<T> {
    final Publisher<? extends T> publisher;

    public FlowableFromPublisher(Publisher<? extends T> publisher) {
        this.publisher = publisher;
    }

    @Override
    protected void subscribeActual(Subscriber<? super T> s) {
        publisher.subscribe(s);
    }
}

```
这里的publisher就是，走s.onNext()
```java
 Publisher<String> publisher = new Publisher<String>() {
            @Override
            public void subscribe(Subscriber s) {
                s.onNext("s");

            }
        };

```