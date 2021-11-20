package com.acap.rc.service;

import com.acap.rc.RequestChain;
import com.acap.rc.adapter.RequestAdapterFactory;
import com.acap.rc.annotation.service.VariableUrl;
import com.acap.rc.annotation.service.OkHttpConfig;
import com.acap.rc.annotation.service.RetrofitConfig;
import com.acap.rc.logs.HttpLogInterceptor;

import org.jetbrains.annotations.Nullable;

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
public final class ServiceCreator {
    private static final ServiceCreator mInstance = new ServiceCreator();
    private Map<String, Object> mApiCache = new HashMap<>();

    private ServiceCreator() {
    }

    public static ServiceCreator getInstance() {
        return mInstance;
    }

    public static <T> T getAndCreate(Class<T> service, VariableUrl url, Class<? extends OkHttpConfig> okhttpConfig, Class<? extends RetrofitConfig> retrofitConfig) {
        return mInstance.get(service, url, okhttpConfig, retrofitConfig);
    }

    /**
     * 获得服务配置的唯一Key
     */
    public <T> String key(Class<T> service, String url, Class<? extends OkHttpConfig> okhttpConfig, Class<? extends RetrofitConfig> retrofitConfig) {
        return service.getName() + url + okhttpConfig.getName() + retrofitConfig.getName();
    }

    /**
     * 获取缓存 或 创建服务
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> service, VariableUrl dynamicUrl, Class<? extends OkHttpConfig> okhttpConfig, Class<? extends RetrofitConfig> retrofitConfig) {
        String url = dynamicUrl.url();
        String key = key(service, url, okhttpConfig, retrofitConfig);
        Object instance = mApiCache.get(key);
        if (instance == null) {
            instance = create(service, url, okhttpConfig, retrofitConfig);
            mApiCache.put(key, instance);
        }
        return (T) instance;
    }

    @Nullable
    private <T> T create(Class<T> service, String url, Class<? extends OkHttpConfig> okhttpConfig, Class<? extends RetrofitConfig> retrofitConfig) {
        try {
            OkHttpClient okhttp = builderOkHttp(okhttpConfig).build();
            Retrofit retrofit = builderRetrofit(url, okhttp, retrofitConfig).build();
            return retrofit.create(service);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private OkHttpClient.Builder builderOkHttp(Class<? extends OkHttpConfig> config) throws InstantiationException, IllegalAccessException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (RequestChain.isDebug()) {
            builder.addInterceptor(new HttpLogInterceptor(RequestChain.LOG_TAG));
        }
        config.newInstance().builder(builder);
        return builder;
    }

    private Retrofit.Builder builderRetrofit(String url, OkHttpClient client, Class<? extends RetrofitConfig> config) throws InstantiationException, IllegalAccessException {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(url).client(client);
        builder.addCallAdapterFactory(new RequestAdapterFactory());
        builder.addConverterFactory(GsonConverterFactory.create());
        config.newInstance().builder(builder, url, client);
        return builder;
    }

}
