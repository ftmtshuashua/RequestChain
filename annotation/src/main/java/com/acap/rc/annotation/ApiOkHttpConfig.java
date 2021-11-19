package com.acap.rc.annotation;

import com.acap.rc.annotation.service.OkHttpConfig;

/**
 * <pre>
 * Tip:
 *      OkHttp 配置,可选的配置
 *
 * @author A·Cap
 * @date 2021/11/18 14:12
 * </pre>
 */
public @interface ApiOkHttpConfig {

    /**
     * 指定OkHttp的配置
     */
    Class<? extends OkHttpConfig> value();

}
