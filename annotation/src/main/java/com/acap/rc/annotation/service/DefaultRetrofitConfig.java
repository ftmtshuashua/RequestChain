package com.acap.rc.annotation.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/9/17 14:37
 * </pre>
 */
public final class DefaultRetrofitConfig implements RetrofitConfig {

    @Override
    public Retrofit.Builder builder(Retrofit.Builder builder, String url, OkHttpClient client) {
        builder.baseUrl(url).client(client);
        return builder;
    }
}
