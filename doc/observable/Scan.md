# Scan

把上一次计算结果，扔给下次当参数
```java
public class ScanDemo {

    public static void main(String args[]) {

        Observable.just(1, 2, 3, 4, 5).scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer sum, Integer value) throws Exception {
                return sum + value;
            }
        }).subscribe(new MyDisposableObserver("scan"));
    }
}

```

```Console
scan:1
scan:3
scan:6
scan:10
scan:15
scan:onComplete

Process finished with exit code 0

```