package com.acap.rc.annotation;


import com.acap.rc.annotation.provider.DefaultConfigProvider;
import com.acap.rc.annotation.provider.OkHttpConfigProvider;
import com.acap.rc.annotation.provider.RetrofitConfigProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * Tip:
 *      自动生成Api代码
 *
 * Created by ACap on 2021/4/15 15:17
 * </pre>
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Api {
    /**
     * 指定Api文件对应的服务器地址
     */
    String url();

    /**
     * 指定OkHttp的配置
     */
    Class<? extends OkHttpConfigProvider> configOkHttp() default DefaultConfigProvider.OkHttpDefaultConfigProvider.class;

    /**
     * 指定Retrofit的配置
     */
    Class<? extends RetrofitConfigProvider> configRetrofit() default DefaultConfigProvider.RetrofitDefaultConfigProvider.class;

}
