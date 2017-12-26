# ObservableMap
把一个内容的提供转换成另一种类型
```java
public class ObservableMapDemo {
    public static void main(String args[]) {

        Observable<Integer> observable = Observable.just(1);
        Observable mapObservable = observable.map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return String.valueOf(integer) + "hello!";
            }
        });
        mapObservable.subscribe(new MyDisposableObserver("map"));

    }
}

```

```Console
map:1hello!
map:onComplete

Process finished with exit code 0

```