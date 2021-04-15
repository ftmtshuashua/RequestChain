package com.acap.rc.api;

import com.acap.rc.adapter.Rc;
import com.acap.rc.annotation.Api;
import com.acap.rc.bean.BeanData1;

import retrofit2.http.GET;

/**
 * <pre>
 * Tip:
 *      用于测试的API
 *
 * Created by ACap on 2021/4/15 15:30
 * </pre>
 */
@Api(
        url = "https://api.caiyunapp.com"
)
public interface XXApi {

    @GET("/")
    Rc<BeanData1> getJson();  //{"status": "ok"}


    @GET("/v2/TAkhjf8d1nlSlspN/121.6544,25.1552/realtime.json")
    Rc<BeanData1> getJson2();  //{"status":"failed", "error":"'API quota is exhausted'", "api_version":"v2"}
}
