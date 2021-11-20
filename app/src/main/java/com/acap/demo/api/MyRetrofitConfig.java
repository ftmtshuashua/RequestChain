package com.acap.demo.api;

import com.acap.rc.annotation.service.RetrofitConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/9/17 14:37
 * </pre>
 */
public final class MyRetrofitConfig implements RetrofitConfig {

    @Override
    public Retrofit.Builder builder(Retrofit.Builder builder, String url, OkHttpClient client) {
        builder.baseUrl(url).client(client);
        return builder;
    }
}
