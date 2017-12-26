# AsyncSubject
AsyncSubject只发射onComplete之前的一次数据

## demo
```java
public class AsyncSubjectDemo {

    public static void main(String args[]) {
        AsyncSubject<Integer> asyncSubject = AsyncSubject.create();
        asyncSubject.subscribe(new MyDisposableObserver("asyncSubject"));
        asyncSubject.onNext(1);
        asyncSubject.onNext(2);
        asyncSubject.onNext(3);
        asyncSubject.onNext(4);
        System.out.println("onComplete前");
        asyncSubject.onComplete();
        System.out.println("onCompleter后");

    }
}


```
## 输出：
```Console
onComplete前
asyncSubject:4
asyncSubject:onComplete
onCompleter后

Process finished with exit code 0

```