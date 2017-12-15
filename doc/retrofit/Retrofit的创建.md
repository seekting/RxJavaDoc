# Retrofit的创建

## 应用篇
首先定义接口
```java
public interface HttpRequestApi {

    @GET(value = "http/doc.json")
    Observable<String> doc();

}


```
然后创建Retrofit
```java
  builder.baseUrl("http://192.168.1.108");
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        builder.addConverterFactory(HttpGsonConverterFactory.create(gson));
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        mRetrofit = builder.build();
        mHttpRequestApi = mRetrofit.create(HttpRequestApi.class);

```

得到的mHttpRequestApi就拥有访问网络的能力，好神奇

```kotlin
val observer = mHttpRequestApi.doc()

    observer.observeOn(Schedulers.io())
    .subscribeOn(Schedulers.io())
    .subscribe(Action1 {

        Log.d("seekting", "MainActivity.onCreate()" + it)

    }, Action1 {
        Log.d("seekting", "MainActivity.onCreate() error" + it, it)
    })


```
## 原理篇
