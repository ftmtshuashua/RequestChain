package com.acap.rc.annotation;

/**
 * <pre>
 * Tip:
 *  配置一个固定的 URL 作为服务的地址. 如果服务地址是动态的,请使用 @{@link ApiVariableUrl}
 *
 *  可配合{@link ApiOkHttpConfig}与{@link ApiRetrofitConfig}使用
 * @author A·Cap
 * @date 2021/11/18 14:12
 * </pre>
 */
public @interface ApiUrl {
    /**
     * 指定服务的 URL
     *
     * @return 服务的 URL
     */
    String value();
}
