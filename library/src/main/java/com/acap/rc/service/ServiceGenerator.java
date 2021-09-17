package com.acap.rc.service;

import com.acap.rc.RequestChain;
import com.acap.rc.adapter.RequestAdapterFactory;
import com.acap.rc.annotation.service.OkHttpConfig;
import com.acap.rc.annotation.service.RetrofitConfig;
import com.acap.rc.logs.HttpLogInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 * Tip:
 *      提供者生成器
 *
 * Created by ACap on 2021/4/15 18:51
 * </pre>
 */
public class ServiceGenerator {
    private static final ServiceGenerator mInstance = new ServiceGenerator();

    private Map<Class<?>, Object> mApiCache = new HashMap<>();

    /**
     * 获得API
     *
     * @param service        Api的服务
     * @param url            Api的服务器地址
     * @param okhttpConfig   Api绑定的OkHttp配置
     * @param retrofitConfig Api绑定的Retrofit配置
     * @param <T>            Api服务类型
     * @return
     */
    public static final <T> T generator(final Class<T> service, String url, Class<? extends OkHttpConfig> okhttpConfig, Class<? extends RetrofitConfig> retrofitConfig) {
        Map<Class<?>, Object> mApiCache = mInstance.mApiCache;
        Object api = mApiCache.get(service);
        if (api == null) {
            try {

                OkHttpClient.Builder ok_builder;
                ok_builder = new OkHttpClient.Builder();
                ok_builder = OkHttpGlobalConfig(ok_builder);
                ok_builder = okhttpConfig.newInstance().builder(ok_builder);
                OkHttpClient okhttp = ok_builder.build();

                Retrofit.Builder rt_builder;
                rt_builder = new Retrofit.Builder();
                rt_builder = RetrofitGlobalConfig(rt_builder, url, okhttp);
                rt_builder = retrofitConfig.newInstance().builder(rt_builder, url, okhttp);
                Retrofit retrofit = rt_builder.build();

                api = retrofit.create(service);
                mApiCache.put(service, api);

            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return (T) api;
    }

    //全局默认配置
    private static final OkHttpClient.Builder OkHttpGlobalConfig(OkHttpClient.Builder builder) {
        builder.connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        if (RequestChain.isDebug()) {
            builder.addInterceptor(new HttpLogInterceptor(RequestChain.LOG_TAG));
        }

        return builder;
    }

    //全局默认配置
    private static final Retrofit.Builder RetrofitGlobalConfig(Retrofit.Builder builder, String url, OkHttpClient client) {
        builder.baseUrl(url).client(client);
        builder.addCallAdapterFactory(new RequestAdapterFactory());

        try {
            builder.addConverterFactory(GsonConverterFactory.create());
        } catch (Throwable t) {
        }
        return builder;
    }
}
