package com.acap.rc.annotation;

import com.acap.rc.annotation.service.OkHttpConfig;

/**
 * <pre>
 * Tip:
 *  {@link okhttp3.OkHttpClient} 配置,这是一个可选的配置
 *
 * @author A·Cap
 * @date 2021/11/18 14:12
 * </pre>
 */
public @interface ApiOkHttpConfig {

    /**
     * 指定服务的 {@link okhttp3.OkHttpClient} 配置
     *
     * @return 服务的配置类
     */
    Class<? extends OkHttpConfig> value();

}
