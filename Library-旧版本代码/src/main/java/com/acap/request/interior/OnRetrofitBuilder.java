package com.acap.request.interior;

import retrofit2.Retrofit;

/**
 * <pre>
 * Tip:
 *  Retrofit构建器
 * Function:
 *
 * Created by LiFuPing on 2019/7/22 14:18
 * </pre>
 */
@FunctionalInterface
public interface OnRetrofitBuilder {

    /**
     * Retrofit 开始构建
     *
     * @param builder 构建参数配置
     */
    void onBuilder(Retrofit.Builder builder);
}
