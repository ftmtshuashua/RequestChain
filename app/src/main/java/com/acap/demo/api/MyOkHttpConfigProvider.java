package com.acap.demo.api;

import com.acap.rc.annotation.provider.OkHttpConfigProvider;
import com.acap.rc.tool.HttpLogInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/7/23 18:18
 * </pre>
 */
public class MyOkHttpConfigProvider implements OkHttpConfigProvider {
    @Override
    public OkHttpClient.Builder builder(OkHttpClient.Builder builder) {
        builder.connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        builder.addInterceptor(new HttpLogInterceptor("API"));
        return builder;
    }
}
