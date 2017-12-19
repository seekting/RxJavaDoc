# ObservableError

## demo
```java
public class ObservableErrorDemo {

    public static void main(String args[]) {

        Observable.error(new NullPointerException("xx")).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

                System.out.println("o=" + o);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

                throwable.printStackTrace();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {

                System.out.println("end!");
            }
        });
    }
}

```

## 输出
```Console
java.lang.NullPointerException: xx
	at com.seekting.rxjava.observable.ObservableErrorDemo.main(ObservableErrorDemo.java:15)

Process finished with exit code 0

```

## 源码解析

```java
public static <T> Observable<T> error(final Throwable exception) {
    ObjectHelper.requireNonNull(exception, "e is null");
    return error(Functions.justCallable(exception));
}


```

```java
   public static <T> Observable<T> error(final Throwable exception) {
        ObjectHelper.requireNonNull(exception, "e is null");
        return error(Functions.justCallable(exception));
    }

```
生成ObservableError
```java
public static <T> Observable<T> error(Callable<? extends Throwable> errorSupplier) {
    ObjectHelper.requireNonNull(errorSupplier, "errorSupplier is null");
    return RxJavaPlugins.onAssembly(new ObservableError<T>(errorSupplier));
}

```

调用EmptyDisposable.error
```java
    public void subscribeActual(Observer<? super T> s) {
        Throwable error;
        try {
            error = ObjectHelper.requireNonNull(errorSupplier.call(), "Callable returned null throwable. Null values are generally not allowed in 2.x operators and sources.");
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            error = t;
        }
        EmptyDisposable.error(error, s);
    }

```

调用订阅者的onError
```java
public static void error(Throwable e, Observer<?> s) {
    s.onSubscribe(INSTANCE);
    s.onError(e);
}


```
