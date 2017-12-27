# Combine

```java
public class CombineDemo {

    public static void main(String args[]) {
        Observable<Integer> intObservable = Observable.just(1, 2, 3, 4);
        Observable<String> strObservable = Observable.just("a", "b", "c", "d", "e");
        Observable.combineLatest(intObservable, strObservable, new BiFunction<Integer, String, Person>() {
            @Override
            public Person apply(Integer integer, String s) throws Exception {
                return new Person(s, integer);
            }
        }).subscribe(new MyDisposableObserver("combineLatest"));
    }

}

```

```Console
combineLatest:Person{name='a', age=4}
combineLatest:Person{name='b', age=4}
combineLatest:Person{name='c', age=4}
combineLatest:Person{name='d', age=4}
combineLatest:Person{name='e', age=4}
combineLatest:onComplete

Process finished with exit code 0


```