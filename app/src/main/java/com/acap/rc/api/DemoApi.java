package com.acap.rc.api;

import com.acap.rc.adapter.Request;
import com.acap.rc.annotation.Api;
import com.acap.rc.annotation.provider.DefaultConfigProvider;
import com.acap.rc.model.BaseModel;
import com.acap.rc.model.Model_Data;

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
