package com.acap.demo;

import com.acap.demo.api.MyRetrofitConfig;
import com.acap.demo.model.BaseModel;
import com.acap.rc.adapter.Request;
import com.acap.rc.annotation.ApiRetrofitConfig;
import com.acap.rc.annotation.ApiUrl;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/9/16 18:11
 * </pre>
 */
@ApiUrl("https://getman.cn/")
//@ApiOkHttpConfig(MyOkHttpConfig.class)
@ApiRetrofitConfig(MyRetrofitConfig.class)
public interface MyApi {
    @GET("mock/route/to/demo")
    Request<ResponseBody> getResponseBody();

    @GET("mock/route/to/demo")
    Request<ResponseBody> getResponseBodyWithParams(@QueryMap Map<String, Object> arg);

    @GET("mock/acap/api/helloworld")
    Request<BaseModel<String>> getModel();

    @GET("/mock/acap/api/helloworld/error")
    Request<BaseModel<String>> getModelError();

    @GET("mock/route/to/demo")
    Call<ResponseBody> getCall(@Query("1") String str, @Query("2") String str2, @Query("3") String str3);
}
