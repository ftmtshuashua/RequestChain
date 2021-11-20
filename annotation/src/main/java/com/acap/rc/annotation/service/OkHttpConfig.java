package com.acap.rc.annotation.service;

import okhttp3.OkHttpClient;

/**
 * <pre>
 * Tip:
 *      OkHttp的配置提供器
 *
 * @author A·Cap
 * @date 2021/11/18 14:12
 * </pre>
 */
public interface OkHttpConfig {
    /**
     * {@link OkHttpClient} 的配置接口
     *
     * @param builder 配置之前的 {@link OkHttpClient.Builder}
     * @return 配置完成的  {@link OkHttpClient.Builder}
     */
    OkHttpClient.Builder builder(OkHttpClient.Builder builder);
}
