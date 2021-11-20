package com.acap.rc.annotation;

import com.acap.rc.annotation.service.RetrofitConfig;

/**
 * <pre>
 * Tip:
 *  {@link retrofit2.Retrofit} 配置,这是一个可选的配置
 *
 * @author A·Cap
 * @date 2021/11/18 14:12
 * </pre>
 */
public @interface ApiRetrofitConfig {

    /**
     * 指定服务的 {@link retrofit2.Retrofit} 配置
     *
     * @return 服务的配置类
     */
    Class<? extends RetrofitConfig> value();

}
