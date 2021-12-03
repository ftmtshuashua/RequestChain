package com.acap.rc;

import com.acap.rc.adapter.Request;
import com.acap.rc.annotation.ApiOkHttpConfig;
import com.acap.rc.annotation.ApiRetrofitConfig;
import com.acap.rc.annotation.ApiUrl;
import com.acap.rc.api.MyOkHttpConfig;
import com.acap.rc.api.MyRetrofitConfig;

import org.json.JSONObject;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/9/16 18:11
 * </pre>
 */
@ApiUrl("http://ta.ecsdk.com/")
//@ApiVariableUrl(RandomUrlConfig.class)
@ApiOkHttpConfig(MyOkHttpConfig.class)
@ApiRetrofitConfig(MyRetrofitConfig.class)
public interface TdApi {

    @GET("v2/device/get")
      Request<JSONObject> getDevice(
            /* 包名 */
            @Query("app_ident") String packageName,
            /* 设备信息 b201f3ff5d4592c1963aa1bfcbe9448de4ad6d10 */
            @Query("device_ident") String device_ident,
            /* 城市 */
            @Query("country") String country,

            @Query("alias") String alias,
            /* 平台 ios\android */
            @Query("platform") String platform
    );
}
