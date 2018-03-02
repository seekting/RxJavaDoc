# Observer

```java
public interface Observer<T> {


    void onSubscribe(@NonNull Disposable d);


    void onNext(@NonNull T t);


    void onError(@NonNull Throwable e);


    void onComplete();

}

```

来看看官方对它的描述


After an Observer calls an Observable's subscribe method, first the Observable calls onSubscribe(Disposable) with a Disposable that allows cancelling the sequence at any time, then the Observable may call the Observer's onNext method any number of times to provide notifications. A well-behaved Observable will call an Observer's onComplete method exactly once or the Observer's onError method exactly once.

当调用Observable.subscribe订阅的时候，首先调用onSubscribe，再调用onNext,再调用onComplete，或是中途出问题了调用onError

当拿到Disposable句柄的时候可以调用dispose取消

```sequence
User->Observable:subscribe()
Observable->Observer:onSubscribe()
Observable->Observer:onNext()
Observable->Observer:onComplete()
```
