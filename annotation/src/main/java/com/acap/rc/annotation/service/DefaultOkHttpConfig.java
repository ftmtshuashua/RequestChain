package com.acap.rc.annotation.service;

import okhttp3.OkHttpClient;

/**
 * <pre>
 * Tip:
 *
 *
 * @author A·Cap
 * @date 2021/11/18 14:12
 * </pre>
 */
public class DefaultOkHttpConfig implements OkHttpConfig {

    @Override
    public OkHttpClient.Builder builder(OkHttpClient.Builder builder) {
        return builder;
    }
}
