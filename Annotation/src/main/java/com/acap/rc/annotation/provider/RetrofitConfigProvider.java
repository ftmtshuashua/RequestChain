package com.acap.rc.annotation.provider;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * <pre>
 * Tip:
 *      Retrofit的配置提供器
 *
 * Created by ACap on 2021/4/15 17:08
 * </pre>
 */
public interface RetrofitConfigProvider {
    Retrofit.Builder builder(Retrofit.Builder builder, String url, OkHttpClient client);
}
