#RxJava的原原型

MyFlowable充当提供者，它提供value，当它调用订阅方法的时候，其实就是调用消费都的接受方法accept

```java
public class MyFlowable {
    String value;

    public MyFlowable setValue(String s) {
        this.value = s;
        return this;
    }

    public void subscribe(MyConsumer a) {
        a.accept(value);

    }

    static class MyConsumer {
        public void accept(String value) {
            System.out.println("value=" + value);

        }

    }

    public static void main(String args[]) {
        MyFlowable myFlowable = new MyFlowable();
        MyConsumer myConsumer = new MyConsumer();
        myFlowable.setValue("xxxx").subscribe(myConsumer);

    }
}
```

## 进化版

```java
 public interface Consumer1<T> {

        void accept(T t);
    }

    static abstract class Publisher1<T> {
        protected T t;

        public Publisher1(T t) {
            this.t = t;
        }

        abstract void provide(Consumer1 consumer1);
    }

    static class MyPublisher1 extends Publisher1<String> {
        public MyPublisher1(String s) {
            super(s);
        }

        @Override
        public void provide(Consumer1 consumer1) {
            consumer1.accept(t);

        }
    }

```

```java
   Consumer1<String> consumer1 = new Consumer1<String>() {
            @Override
            public void accept(String s) {

                System.out.println("s=" + s);
            }
        };
        Publisher1<String> myP = new MyPublisher1("ttt");
        myP.provide(consumer1);

```