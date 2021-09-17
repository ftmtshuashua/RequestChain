package com.acap.rc.annotation.service;

import okhttp3.OkHttpClient;

/**
 * <pre>
 * Tip:
 *      OkHttp的配置提供器
 *
 * Created by ACap on 2021/4/15 17:08
 * </pre>
 */
public interface OkHttpConfig {
    OkHttpClient.Builder builder(OkHttpClient.Builder builder);
}
