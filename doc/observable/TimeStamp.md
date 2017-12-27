# TimeStamp
## demo
```java
public class TimeStampDemo {

    public static void main(String args[]) {
        Observable<Timed<Timed<Integer>>> timedObservable = Observable.just(1, 2, 3).timestamp().timeInterval();
        timedObservable.subscribe(new MyDisposableObserver("timeStamp"));

    }
}
```
## 输出：
```Console

timeStamp:Timed[time=1, unit=MILLISECONDS, value=Timed[time=1514343563878, unit=MILLISECONDS, value=1]]
timeStamp:Timed[time=0, unit=MILLISECONDS, value=Timed[time=1514343563878, unit=MILLISECONDS, value=2]]
timeStamp:Timed[time=0, unit=MILLISECONDS, value=Timed[time=1514343563878, unit=MILLISECONDS, value=3]]
timeStamp:onComplete

Process finished with exit code 0

```