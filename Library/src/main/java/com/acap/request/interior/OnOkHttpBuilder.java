package com.acap.request.interior;

import okhttp3.OkHttpClient;

/**
 * <pre>
 * Tip:
 *      OkHttpClient构建器
 *
 * Function:
 *
 * Created by LiFuPing on 2019/7/22 12:10
 * </pre>
 */
@FunctionalInterface
public interface OnOkHttpBuilder {

    /**
     * OkHttpClient 开始构建
     *
     * @param builder 构建参数配置
     */
    void onBuilder(OkHttpClient.Builder builder);

}
