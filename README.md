RequestChain
=====
[![](https://jitpack.io/v/ftmtshuashua/RequestChain.svg)](https://jitpack.io/#ftmtshuashua/RequestChain)
[![](https://img.shields.io/badge/android-5.0%2B-blue)]()
[![](https://img.shields.io/badge/jdk-1.8%2B-blue)]()
[![License Apache2.0](http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)

RequestChain是一个基于 EventChain + Retrofit + OkHttp 实现的网络请求框架,只需要简单的配置即可快速搭建请求环境

Download
-------
```
repositories {
    maven { url 'https://www.jitpack.io' }
}

dependencies {
    implementation 'com.github.ftmtshuashua:EventChain:2.0.2'
    implementation 'com.github.ftmtshuashua:RequestChain:2.0.3'
    annotationProcessor 'com.github.ftmtshuashua.RequestChain:compiler:2.0.3'

    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.okhttp3:okhttp:4.9.1'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'  //可选
}
```




Use
--------
```
// Debug 日志
RequestChain.setDebug(BuildConfig.DEBUG);
```
```
// 定义 API
import com.acap.rc.adapter.Request;
import com.acap.rc.annotation.Api;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.Call;

@Api(url = "https://getman.cn/", okhttpConfig = 可选配置, retrofitConfig = 可选配置)
public interface MyApi {// Build 之后自动生成 MyApiService

    @GET("mock/route/to/demo")
    Request<ResponseBody> getMan();
	
    @GET("mock/route/to/demo")
    Call<ResponseBody> getMan2();
}
```
```
// 使用 API ( EventChain 模式)
MyApiService.getMan()
        .listener((OnEventNextListener<Object, ResponseBody>) result -> {
            Log.i("API:打印请求结果", result.string());
        })
        .start();

// 使用 API ( retrofit2 模式)
Response<ResponseBody> execute = MyApiService.getMan2().execute();
```
```
// 输出日志
4301-4338/com.acap.rc I/API: --> GET Start https://getman.cn/mock/route/to/demo
4301-4338/com.acap.rc I/API: ╔═══════════════════════════════════════════════════
4301-4338/com.acap.rc I/API: ║ GET  http/1.1  https://getman.cn/mock/route/to/demo
4301-4338/com.acap.rc I/API: ║ END OF  --->>> GET Request
4301-4338/com.acap.rc I/API: ║ 
4301-4338/com.acap.rc I/API: ║ Response Code=200  耗时(920ms)
4301-4338/com.acap.rc I/API: ║ server: nginx
4301-4338/com.acap.rc I/API: ║ date: Fri, 17 Sep 2021 07:59:17 GMT
4301-4338/com.acap.rc I/API: ║ content-type: application/json
4301-4338/com.acap.rc I/API: ║ x-nws-uuid-verify: 1c422f2d98d13700223ae09c12aa919f
4301-4338/com.acap.rc I/API: ║ x-ratelimit-limit: 30
4301-4338/com.acap.rc I/API: ║ x-ratelimit-remaining: 28
4301-4338/com.acap.rc I/API: ║ x-daa-tunnel: hop_count=2
4301-4338/com.acap.rc I/API: ║ x-cache-lookup: Hit From Upstream
4301-4338/com.acap.rc I/API: ║ x-cache-lookup: Hit From Inner Cluster
4301-4338/com.acap.rc I/API: ║ last-modified: Fri, 17 Sep 2021 07:50:00 GMT
4301-4338/com.acap.rc I/API: ║ cache-control: private, no-cache
4301-4338/com.acap.rc I/API: ║ x-nws-log-uuid: 7413936374885786792
4301-4338/com.acap.rc I/API: ║ x-cache-lookup: Cache Miss
4301-4338/com.acap.rc I/API: ║ {
4301-4338/com.acap.rc I/API: ║     "message": "Hello world!"
4301-4338/com.acap.rc I/API: ║ }
4301-4338/com.acap.rc I/API: ╚═══════════════════════════════════════════════════
4301-4301/com.acap.rc I/API:打印请求结果: {"message":"Hello world!"}
```

相关资料
--------
> [OkHttp](https://github.com/square/okhttp)

> [Retrofit](https://github.com/square/retrofit)

> [EventChain](https://github.com/ftmtshuashua/EventChain)