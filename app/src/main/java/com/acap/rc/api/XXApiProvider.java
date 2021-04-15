package com.acap.rc.api;

import com.acap.ec.EventChain;
import com.acap.rc.annotation.provider.DefaultConfigProvider;
import com.acap.rc.bean.BeanData1;
import com.acap.rc.provider.ProviderRetrofitGenerator;

import retrofit2.Retrofit;

/**
 * <pre>
 * Tip:
 *      生成的Api提供器
 *
 * Created by ACap on 2021/4/15 17:35
 * </pre>
 */
public class XXApiProvider {

    private static XXApiProvider mInstance = new XXApiProvider();
    private Retrofit mRetrofit;
    private XXApi mXXApi;

    private void init() {
//        OkHttpClient.Builder builder = new DefaultConfigProvider.OkHttpDefaultConfigProvider().builder(new OkHttpClient.Builder());
//        mRetrofit = new DefaultConfigProvider.RetrofitDefaultConfigProvider().builder(new Retrofit.Builder(), "https://api.caiyunapp.com", builder.build()).build();
//        mXXApi = mRetrofit.create(XXApi.class);

        String url = "https://api.caiyunapp.com";
        mXXApi = ProviderRetrofitGenerator.getApi(XXApi.class, url, DefaultConfigProvider.OkHttpDefaultConfigProvider.class, DefaultConfigProvider.RetrofitDefaultConfigProvider.class);
    }

    public XXApiProvider() {
        init();
    }

    public static final <P> EventChain<P, BeanData1> getJson() {
        return (EventChain) mInstance.mXXApi.getJson();
    }

    public static final <P> EventChain<P, BeanData1> getJson2() {
        return (EventChain) mInstance.mXXApi.getJson2();
    }

}
