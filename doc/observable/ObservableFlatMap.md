# ObservableFlatMap

## 简单用法
FlatMap把一种内容提供者，变成另外一种内容提供者
```java
   private static void demo1() {
        Observable<String> observable = Observable.just(1, 2).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                return Observable.just(integer + "-a", integer + "-b", integer + "-c");
            }
        });

        observable.subscribe(new MyDisposableObserver("flap_map"));
    }


```

```Console
flap_map:1-a
flap_map:1-b
flap_map:1-c
flap_map:2-a
flap_map:2-b
flap_map:2-c
flap_map:onComplete

Process finished with exit code 0


```


还可以传三个参数，分别代表next,error,complete
```java
private static void demo2() {
    Observable<String> fromCallable = Observable.fromCallable(new Callable<String>() {
        @Override
        public String call() throws Exception {
            return "1";
//                throw new NullPointerException("xx");
        }
    });
    Observable<Integer> o = fromCallable.flatMap(t -> Observable.just(Integer.parseInt(t) + 2), e -> Observable.just(4), () -> Observable.just(42));
    o.subscribe(new MyDisposableObserver("flatmap2"));

}

```

```Console
flatmap2:3
flatmap2:42
flatmap2:onComplete

Process finished with exit code 0


```

## 高阶用法

```java
 private static void demo3() {
        Function function = new Function<Integer, Observable<String>>() {
            @Override
            public Observable<String> apply(Integer o) throws Exception {
                return Observable.just("Jim", "Lucy", "Lily");
            }
        };

        BiFunction biFunction = new BiFunction<Integer, String, Person>() {
            @Override
            public Person apply(Integer o, String o2) throws Exception {
                Person person = new Person();
                person.age = o;
                person.name = o2;
                return person;
            }
        };
        Observable<Integer> observable = Observable.just(23, 24).flatMap(function, biFunction);
        observable.subscribe(new MyDisposableObserver("demo3"));

    }

```


```console
demo3:Person{name='Jim', age=23}
demo3:Person{name='Lucy', age=23}
demo3:Person{name='Lily', age=23}
demo3:Person{name='Jim', age=24}
demo3:Person{name='Lucy', age=24}
demo3:Person{name='Lily', age=24}
demo3:onComplete

Process finished with exit code 0

```