package com.acap.rc.annotation;

/**
 * <pre>
 * Tip:
 *   可配合 {@link ApiOkHttpConfig} 使用
 * @author A·Cap
 * @date 2021/11/18 14:12
 * </pre>
 */
public @interface ApiUrl {
    /**
     * 指定 API 的 URL
     */
    String value();
}
