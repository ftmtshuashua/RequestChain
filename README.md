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
    implementation 'com.github.ftmtshuashua:RequestChain:2.0.4'
    annotationProcessor 'com.github.ftmtshuashua.RequestChain:compiler:2.0.4'
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
import com.acap.rc.annotation.ApiUrl;
import com.acap.rc.annotation.ApiVariableUrl;
import com.acap.rc.annotation.ApiOkHttpConfig;
import com.acap.rc.annotation.ApiRetrofitConfig;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.Call;

@ApiOkHttpConfig(MyOkHttpConfig.class)      // 可选
@ApiRetrofitConfig(MyRetrofitConfig.class)  // 可选
@ApiUrl("https://getman.cn/") or @ApiVariableUrl(MyVariableUrl.class)
public interface MyApi {// Build 之后自动生成 MyApiService

    @GET("mock/route/to/demo")
    Request<ResponseBody> getResponseBody();
    
    @GET("mock/route/to/demo")
    Request<ResponseBody> getResponseBodyWithParams(@QueryMap Map<String, Object> arg);
    
    @GET("mock/acap/api/helloworld")
    Request<BaseModel<String>> getModel();
    
    @GET("/mock/acap/api/helloworld/error")
    Request<BaseModel<String>> getModelError();
    
    @GET("mock/route/to/demo")
    Call<ResponseBody> getCall(@Query("arg") String str);
}
```

```
// 使用 API ( EventChain 模式)
MyApiService.getResponseBody()
                .chain(MyApiService.getModelError())
                .chain(MyApiService.getModel())
                .listener(new OnEventDialog(this))
                .start();

// 使用 API ( retrofit2 模式)
Response<ResponseBody> execute = MyApiService.getCall("s").execute();
```

返回数据的 Model 中可实现 ApiBody 的方法,实现请求异常的内部扭转。可以通过 OnEventErrorListener 监听失败信息

```
public class BaseModel<T> implements ApiBody {
    public int code;
    public T result;
    public String msg;

    @Override
    public boolean isSuccessful() {
        return code == 0;
    }

    @Override
    public Exception getError() {
        return new Exception(msg);
    }
}

```

```
// 输出日志
4190-4546/com.acap.rc I/API: --> GET Start https://getman.cn/mock/acap/api/helloworld
4190-4546/com.acap.rc I/API:  
    ╔═══════════════════════════════════════════════════
    ║ GET  http/1.1  https://getman.cn/mock/acap/api/helloworld
    ║ END OF  --->>> GET Request
    ║ 
    ║ Response Code=200  耗时(363ms)
    ║ server: nginx
    ║ date: Fri, 19 Nov 2021 11:01:23 GMT
    ║ content-type: text/html; charset=UTF-8
    ║ x-nws-uuid-verify: ab45a34d262924443991888575e68e08
    ║ vary: Accept-Encoding
    ║ access-control-allow-origin: *
    ║ x-daa-tunnel: hop_count=1
    ║ x-cache-lookup: Hit From Upstream
    ║ x-cache-lookup: Cache Miss
    ║ last-modified: Fri, 19 Nov 2021 11:00:00 GMT
    ║ cache-control: private, no-cache
    ║ x-nws-log-uuid: 2012738967934042215
    ║ x-cache-lookup: Hit From Inner Cluster
    ║ {
    ║     "code": 0,
    ║     "result": "HelloWorld",
    ║     "msg": ""
    ║ }
    ╚═══════════════════════════════════════════════════
```

相关资料
--------
> [OkHttp](https://github.com/square/okhttp)

> [Retrofit](https://github.com/square/retrofit)

> [EventChain](https://github.com/ftmtshuashua/EventChain)