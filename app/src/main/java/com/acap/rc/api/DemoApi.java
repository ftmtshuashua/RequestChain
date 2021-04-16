package com.acap.rc.api;

import com.acap.rc.adapter.Request;
import com.acap.rc.annotation.Api;
import com.acap.rc.bean.BeanData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * <pre>
 * Tip:
 *      用于测试的API
 *
 * Created by ACap on 2021/4/15 15:30
 * </pre>
 */
@Api(url = "https://api.caiyunapp.com")
public interface DemoApi {

    @GET("/")
    Request<BeanData> getJson();

    @GET("/v2/TAkhjf8d1nlSlspN/121.6544,25.1552/realtime.json")
    Request<BeanData> getJson2();

    @GET("/xxx")
    Request<BeanData> getJson3();

    @GET("/v2/TAkhjf8d1nlSlspN/121.6544,25.1552/realtime.json")
    Call<BeanData> getTest(@Query("X") String data, @Query("2") int value);

}
