package com.acap.rc.annotation;

import com.acap.rc.annotation.service.VariableUrl;

/**
 * <pre>
 * Tip:
 *  通过 @{@link ApiUrl} 只能配置编译时固定的 URL 作为服务的地址，此配置允许配置一个动态的 URL 作为服务的地址
 *
 *  可配合 {@link ApiOkHttpConfig} 与 {@link ApiRetrofitConfig} 使用
 * @author A·Cap
 * @date 2021/11/18 14:12
 * </pre>
 */
public @interface ApiVariableUrl {

    /**
     * 获得动态 URL 的配置类
     *
     * @return 动态URL的配置类
     */
    Class<? extends VariableUrl> value();
}
