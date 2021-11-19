package com.acap.rc.annotation.service;

import okhttp3.OkHttpClient;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/9/17 14:37
 * </pre>
 */
public final class DefaultOkHttpConfig implements OkHttpConfig {

    @Override
    public OkHttpClient.Builder builder(OkHttpClient.Builder builder) {
        return builder;
    }
}
