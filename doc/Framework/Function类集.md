# Function类集

```java
public interface Func0<R> extends Function, Callable<R> {
    @Override
    R call();
}
```

此接口有一个call方法，返回一个对象，在生产者-消费者里充当生产者的角色，通过返回值向外提供内容

此方法已经被Kotlin启用

```kotlin

public interface Function<out R>

public interface Function0<out R> : Function<R> {
    public operator fun invoke(): R
}
public interface Function1<in P1, out R> : Function<R> {
    public operator fun invoke(p1: P1): R
}

public interface Function2<in P1, in P2, out R> : Function<R> {
    public operator fun invoke(p1: P1, p2: P2): R
}
```
Function0有一个返回,而Function1一个输入P1,一个返回R，而Function2两个输入P1,P2，一个返回R

不难推出，Function1,Function2它就是生产者和消费者的结合体

它消费了P1，而又生成R，典型的生产流水线里的中间车间。

来看看在RxJava里的用例


```java
Observable.just(1).map(new Function<Integer, String>() {
        @Override
        public String apply(Integer integer) throws Exception {
            return integer + "";
        }
    }).subscribe(new Consumer<String>() {
        @Override
        public void accept(String s) throws Exception {
            System.out.println("s= " + s);
        }
    });

```

1->Function->Consumer

产品1被Function转成字符"1"，最终通过Consumer输出

这里的Function就是充当消费者和生产者


