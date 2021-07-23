package com.acap.demo.api;


import com.acap.demo.model.GetmanDemo;
import com.acap.rc.adapter.Request;
import com.acap.rc.annotation.Api;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * <pre>
 * Tip:
 *      利用 https://getman.cn/mock 生成的测试接口
 *
 * Created by ACap on 2021/7/23 15:34
 * </pre>
 */
@Api(url = "https://getman.cn/mock/", configOkHttp = MyOkHttpConfigProvider.class)
public interface GetmanApi {

    @GET("route/to/demo")
    Request<GetmanDemo> getApiEvent();

    @GET("route/to/demo")
    Call<String> getApiCall();
}
