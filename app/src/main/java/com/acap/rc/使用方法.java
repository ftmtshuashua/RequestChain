package com.acap.rc;

import com.acap.rc.adapter.Rc;
import com.acap.rc.adapter.RcAdapterFactory;
import com.acap.rc.api.XXApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/4/15 15:32
 * </pre>
 */
class 使用方法 {
    public 使用方法() {

        XXApi xxApi = getRetrofit("", getOkHttp()).create(XXApi.class);
       xxApi.getJson();

    }


    public static final OkHttpClient getOkHttp() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);

//        builder.addInterceptor( new LogInterceptor("OHC").setLevel(LogInterceptor.Level.BODY));
        return builder.build();
    }

    public static final Retrofit getRetrofit(String url, OkHttpClient client) {
        Retrofit.Builder build = new Retrofit.Builder();
        build.baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new RcAdapterFactory())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        ;
        return build.build();
    }
}
