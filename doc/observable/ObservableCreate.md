# ObservableCreate


通过自己创建内容提供者ObservableOnSubscribe,这个内容提供者可以自己来实现onNext细节和onComplete细节
demo
```java
public static void main(String args[]) {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("haha");
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new MyDisposableObserver("create"));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

```

```Console
create:haha
create:onComplete

```