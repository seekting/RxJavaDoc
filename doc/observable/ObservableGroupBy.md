# ObservableGroupBy


## demo

看输出它能装每个内容标识出是哪一组
```java
public class ObservableGroupByDemo {

    public static void main(String args[]) throws Throwable {
        List<String> albums = Arrays.asList(
                "The Piper at the Gates of Dawn",
                "A Saucerful of Secrets",
                "More", "Ummagumma", "Atom Heart Mother",
                "Meddle", "Obscured by Clouds",
                "The Dark Side of the Moon",
                "Wish You Were Here", "Animals", "The Wall"
        );
        Function<String, Integer> groupby = new Function<String, Integer>() {
            @Override
            public Integer apply(String str) throws Exception {
                return str.split(" ").length;
            }
        };
        Observable
                .fromIterable(albums)
                .groupBy(groupby)
                .subscribe(new DisposableObserver<GroupedObservable<Integer, String>>() {
                    @Override
                    public void onNext(GroupedObservable<Integer, String> integerStringGroupedObservable) {
                        Integer key = integerStringGroupedObservable.getKey();
                        integerStringGroupedObservable.subscribe(new MyDisposableObserver(key + ":"));

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}

```


```Console
7::The Piper at the Gates of Dawn
4::A Saucerful of Secrets
1::More
1::Ummagumma
3::Atom Heart Mother
1::Meddle
3::Obscured by Clouds
6::The Dark Side of the Moon
4::Wish You Were Here
1::Animals
2::The Wall
1::onComplete
2::onComplete
3::onComplete
4::onComplete
6::onComplete
7::onComplete

Process finished with exit code 0

```