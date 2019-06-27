package support.lfp.requestchain.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import support.lfp.requestchain.RequestChainConfig;
import support.lfp.requestchain.interior.LogInterceptor;

/**
 * <pre>
 * Tip:
 *      一个基础性的Api管理
 *
 * Function:
 *
 * Created by LiFuPing on 2019/6/27 10:49
 * </pre>
 */
public class ApiHelper {


    /*网络客户端,这里只提供一个简单的通用客户端*/
    static OkHttpClient mOkHttpClient;

    /*Retrofit客户端,缓存*/
    static final Map<String, Retrofit> mRetrofit = new HashMap<>();
    static final Map<String, Object> mApi = new HashMap<>();


    /** 获得OkHttp客户端 */
    public static final OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(8, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS);

            if (RequestChainConfig.isDebug()) {
                LogInterceptor interceptor = new LogInterceptor("OHC");
                interceptor.setLevel(LogInterceptor.Level.BODY);
                builder.addInterceptor(interceptor);
            }

            mOkHttpClient = builder.build();
        }
        return mOkHttpClient;
    }

    /** 获得Retrofit客户端 */
    public static final Retrofit getRetrofit(String url) {
        Retrofit retrofit = mRetrofit.get(url);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            mRetrofit.put(url, retrofit);
        }
        return retrofit;
    }

    /** 获得API */
    public static final <T> T getApi(Retrofit retrofit, Class<T> cls) {
        final String key = cls.getSimpleName() + "_" + retrofit;
        Object api = mApi.get(key);
        if (api == null) {
            api = retrofit.create(cls);
            mApi.put(key, api);
        }
        return (T) api;
    }

}
