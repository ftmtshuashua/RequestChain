package com.acap.rc.annotation.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * <pre>
 * Tip:
 *      Retrofit的配置提供器
 *
 * @author A·Cap
 * @date 2021/11/18 14:12
 * </pre>
 */
public interface RetrofitConfig {
    /**
     * {@link Retrofit} 的配置接口
     *
     * @param builder 配置之前的 {@link Retrofit.Builder}
     * @return 配置完成的  {@link Retrofit.Builder}
     */
    Retrofit.Builder builder(Retrofit.Builder builder, String url, OkHttpClient client);
}
