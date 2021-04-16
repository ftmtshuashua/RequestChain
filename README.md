
<h1 align="center">RequestChain</h1>
<div align="center">

![](https://img.shields.io/badge/android-5.0%2B-blue)
[![](https://jitpack.io/v/ftmtshuashua/RequestChain.svg)](https://jitpack.io/#ftmtshuashua/RequestChain)
[![License Apache2.0](http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)



</div>

>基于EventChain的Android请求事件链，请求通过Retrofit+RxJava+OkHttp实现

>链式请求

>并发请求

>事件链监听



## 使用
```
//定义API

@Api(url = "https://www.host.com" )
public interface DemoApi {
    @GET("/")
    Request<BeanData> getJson();
}
```

```
//使用APi
public void request(){
    DemoApiProvider.getJson().start();
}
```



## 相关资料
> Retrofit: https://square.github.io/retrofit/

> OkHttp: https://square.github.io/okhttp/

> Gson:

## 配置依赖

```
allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}
```
在Model的build.gradle中添加
```
dependencies {
    implementation 'com.github.ftmtshuashua:RequestChain:version'
    annotationProcessor 'com.github.ftmtshuashua:RequestChain-processor:version'
}
```
