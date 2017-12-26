# ObservableSwitchMap
## demo:
```java
public class SwitchMapDemo {

    public static void main(String args[]) throws Throwable {
        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        Observable.fromArray(array).switchMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                return Observable.just(integer + "_a", integer + "_b").subscribeOn(Schedulers.io());
            }
        }).subscribe(new MyDisposableObserver("flatMap"));

        Thread.sleep(2000);
    }
}

```
输出:
```Console

flatMap:9_a
flatMap:9_b
flatMap:onComplete

Process finished with exit code 0
```
