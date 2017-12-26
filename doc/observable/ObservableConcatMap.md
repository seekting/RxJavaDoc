# ObservableConcatMap
## Demo

与flatMap类似，只不过比flapMap有顺序
```java
public class ObservableConcatMapDemo {

    public static void main(String args[]) throws Throwable {

        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        Observable.fromArray(array).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                return Observable.just(integer + "_a", integer + "_b").subscribeOn(Schedulers.io());
            }
        }).subscribe(new MyDisposableObserver("concatMap"));

        Observable.fromArray(array).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                return Observable.just(integer + "_a", integer + "_b").subscribeOn(Schedulers.io());
            }
        }).subscribe(new MyDisposableObserver("flatMap"));

        Thread.sleep(2000);
    }
}

```

## 输出
```Console
concatMap:1_a
concatMap:1_b
concatMap:2_a
concatMap:2_b
concatMap:3_a
concatMap:3_b
concatMap:4_a
concatMap:4_b
concatMap:5_a
concatMap:5_b
concatMap:6_a
concatMap:6_b
concatMap:7_a
concatMap:7_b
concatMap:8_a
concatMap:8_b
concatMap:9_a
concatMap:9_b
concatMap:onComplete
flatMap:1_a
flatMap:1_b
flatMap:4_a
flatMap:4_b
flatMap:2_a
flatMap:2_b
flatMap:7_a
flatMap:7_b
flatMap:5_a
flatMap:5_b
flatMap:3_a
flatMap:6_a
flatMap:6_b
flatMap:8_a
flatMap:8_b
flatMap:9_a
flatMap:9_b
flatMap:3_b
flatMap:onComplete

Process finished with exit code 0

```