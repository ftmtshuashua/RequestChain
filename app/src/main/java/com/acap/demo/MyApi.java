package com.acap.demo;

import com.acap.rc.adapter.Request;
import com.acap.rc.annotation.Api;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/9/16 18:11
 * </pre>
 */
@Api(url = "https://getman.cn/")
public interface MyApi {
    @GET("mock/route/to/demo")
    Request<ResponseBody> getMan();

    @GET("mock/route/to/demo")
    Call<ResponseBody> getMan2();

    @GET("mock/route/to/demo")
    Request<ResponseBody> test(@QueryMap Map<String, Object> arg);
}
