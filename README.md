
<h1 align="center">RequestChain</h1>
<div align="center">

![](https://img.shields.io/badge/android-5.0%2B-blue)
[![](https://jitpack.io/v/ftmtshuashua/RequestChain.svg)](https://jitpack.io/#ftmtshuashua/RequestChain)
[![License Apache2.0](http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)



</div>

>基于EventChain+Retrofit+OkHttp实现的网络请求框架

>链式请求

>并发请求

>事件链监听



## 配置依赖

```
allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.ftmtshuashua:RequestChain:version'
    annotationProcessor 'com.github.ftmtshuashua.RequestChain:compiler:version'
}
```



## 使用
```
//定义API接口

package com.acap.rc.api;

import com.acap.rc.adapter.Request;
import com.acap.rc.annotation.Api;
import com.acap.rc.annotation.provider.DefaultConfigProvider;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

@Api(
        //必填 - 定义API的服务器
        url = "http://www.baidu.com"
        //可选 - OkHttp的属性配置
        , configOkHttp = DefaultConfigProvider.OkHttpDefaultConfigProvider.class
        //可选 - Retrofit的属性配置
        , configRetrofit = DefaultConfigProvider.RetrofitDefaultConfigProvider.class
)
public interface DemoApi {
    /*
     * 这里使用retrofit2的语法定义接口
     * 使用方式与Retrofit2相同
     */

    @GET("/ApiPath")
    Request<String> mapApi();

    @POST("/ApiPath")
    Call<String> retrofit2Api();  //使用原生Retrofit2的Api

    // ... 其他：如 RxJava 的 Observer<String> rxjavaApi();
}

```
> 执行 Build -> Make Project
```
//使用APi
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //----------- 原生Retrofit2 ------------
        DemoApiProvider.retrofit2Api().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //请求结果
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //请求失败
            }
        });


        //----------- Request<> 对象具有EventChain特性 ------------
        DemoApiProvider.mapApi()
                .addOnEventListener((OnEventSucceedListener<String>) result -> System.out.println("API请求的结果:" + result))
                .addOnEventListener((OnEventFailureListener) e -> {
                    System.err.println("请求失败");
                    e.printStackTrace();
                })
                .chain(DemoApiProvider.mapApi())
                .start();
    }
}
```



## 相关资料
> Retrofit: https://square.github.io/retrofit/

> OkHttp: https://square.github.io/okhttp/

> EventCHain: https://github.com/ftmtshuashua/EventChain
