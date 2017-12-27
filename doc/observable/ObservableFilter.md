# ObservableFilter
```java
public class ObservableFilterDemo {
    public static void main(String args[]) {
        Observable.just(1, 2, 3, 4, 5).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer % 2 == 0;
            }
        }).subscribe(new MyDisposableObserver("%2=0:"));
    }
}


```

```Console
%2=0::2
%2=0::4
%2=0::onComplete

Process finished with exit code 0

```