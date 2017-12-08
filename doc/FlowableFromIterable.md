# FlowableFromIterable

```java

public static void main(String args[]) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        Flowable<String> flowable = Flowable.fromIterable(list);
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("s=" + s);
            }
        };
        flowable.subscribe(consumer);


    }
```

```Console
s=1
s=2
s=3

Process finished with exit code 0

```

逻辑和array一样，只不过最后fastPath走的iterator


```java
 @Override
void fastPath() {
    Iterator<? extends T> it = this.it;
    Subscriber<? super T> a = actual;
    for (;;) {
        if (cancelled) {
            return;
        }

        T t;

        try {
            t = it.next();
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            a.onError(ex);
            return;
        }

        if (cancelled) {
            return;
        }

        if (t == null) {
            a.onError(new NullPointerException("Iterator.next() returned a null value"));
            return;
        } else {
            a.onNext(t);
        }

        if (cancelled) {
            return;
        }

        boolean b;

        try {
            b = it.hasNext();
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            a.onError(ex);
            return;
        }


        if (!b) {
            if (!cancelled) {
                a.onComplete();
            }
            return;
        }
    }
}

```