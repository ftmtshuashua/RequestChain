package com.acap.rc.annotation.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * <pre>
 * Tip:
 *
 *
 * @author A·Cap
 * @date 2021/11/18 14:12
 * </pre>
 */
public class DefaultRetrofitConfig implements RetrofitConfig {

    @Override
    public Retrofit.Builder builder(Retrofit.Builder builder, String url, OkHttpClient client) {
        return builder;
    }
}
