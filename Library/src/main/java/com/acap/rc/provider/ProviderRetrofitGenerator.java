package com.acap.rc.provider;

import com.acap.rc.adapter.RequestAdapterFactory;
import com.acap.rc.annotation.provider.OkHttpConfigProvider;
import com.acap.rc.annotation.provider.RetrofitConfigProvider;

import java.util.HashMap;
import java.util.Map;

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
public class ProviderRetrofitGenerator {
    private static final ProviderRetrofitGenerator mInstance = new ProviderRetrofitGenerator();

    private Map<Class<?>, Object> mApiCache = new HashMap<>();

    /**
     * 获得API
     *
     * @param service         Api的服务
     * @param url             Api的服务器地址
     * @param config_okhttp   Api绑定的OkHttp配置
     * @param config_retrofit Api绑定的Retrofit配置
     * @param <T>             Api服务类型
     * @return
     */
    public static final <T> T getApi(final Class<T> service, String url, Class<? extends OkHttpConfigProvider> config_okhttp, Class<? extends RetrofitConfigProvider> config_retrofit) {
        Map<Class<?>, Object> mApiCache = mInstance.mApiCache;
        Object api = mApiCache.get(service);
        if (api == null) {
            try {
                OkHttpConfigProvider okhttp = config_okhttp.newInstance();
                RetrofitConfigProvider retrofit = config_retrofit.newInstance();

                OkHttpClient.Builder builder_okhttp = okhttp.builder(new OkHttpClient.Builder());
                builder_okhttp = OkHttpGlobalConfig(builder_okhttp);

                Retrofit.Builder builder_retrofit = retrofit.builder(new Retrofit.Builder(), url, builder_okhttp.build());
                builder_retrofit = RetrofitGlobalConfig(builder_retrofit);

                api = builder_retrofit.build().create(service);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T) api;
    }

    //全局默认配置
    protected static final OkHttpClient.Builder OkHttpGlobalConfig(OkHttpClient.Builder builder) {

        return builder;
    }

    //全局默认配置
    protected static final Retrofit.Builder RetrofitGlobalConfig(Retrofit.Builder builder) {
        builder.addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new RequestAdapterFactory());
        return builder;
    }
}
