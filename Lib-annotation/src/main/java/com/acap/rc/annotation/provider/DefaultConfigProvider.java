package com.acap.rc.annotation.provider;

import com.acap.rc.adapter.RcAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 * Tip:
 *      配置提供器的默认配置
 *
 * Created by ACap on 2021/4/15 16:58
 * </pre>
 */
public class DefaultConfigProvider {

    /**
     * OkHttp默认配置提供器
     */
    public static final class OkHttpDefaultConfigProvider implements OkHttpConfigProvider {

        @Override
        public OkHttpClient.Builder builder(OkHttpClient.Builder builder) {
            builder.connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);
            return builder;
        }
    }

    /**
     * Retrofit默认配置提供器
     */
    public static final class RetrofitDefaultConfigProvider implements RetrofitConfigProvider {

        @Override
        public Retrofit.Builder builder(Retrofit.Builder builder, String url, OkHttpClient client) {
            builder.baseUrl(url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(new RcAdapterFactory());
            return builder;
        }
    }

}
