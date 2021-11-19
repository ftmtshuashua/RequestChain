package com.acap.rc.annotation;

import com.acap.rc.annotation.service.DynamicUrl;

/**
 * <pre>
 * Tip:
 *      可配合 {@link ApiOkHttpConfig} 使用
 *
 *      通过注解配置的URL无法动态获取，可通过此方法配置动态的URL
 *
 * Created by A·Cap on 2021/11/18 13:53
 * </pre>
 */
public @interface ApiDynamicUrl {

    /**
     * 传入一个 DynamicUrl 的实现来获得 API 的 URL
     */
    Class<? extends DynamicUrl> value();
}
