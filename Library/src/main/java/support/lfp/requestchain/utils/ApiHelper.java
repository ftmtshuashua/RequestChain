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
import support.lfp.requestchain.interior.OnOkHttpBuilder;
import support.lfp.requestchain.interior.OnRetrofitBuilder;

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
    static OkHttpClient mOkHttpClient = null;

    /*Retrofit客户端,缓存*/
    static final Map<String, Retrofit> mRetrofit = new HashMap<>();
    static final Map<String, Object> mApi = new HashMap<>();

    /** 获得默认OkHttp客户端 */
    public static final OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) mOkHttpClient = createOkHttpClient(null);
        return mOkHttpClient;
    }

    /** 获得默认Retrofit客户端 */
    public static final Retrofit getRetrofit(String url) {
        Retrofit retrofit = mRetrofit.get(url);
        if (retrofit == null) {
            retrofit = createRetrofit(url, getOkHttpClient(), null);
            mRetrofit.put(url, retrofit);
        }
        return retrofit;
    }

    /**
     * 创建OkHttp客户端
     *
     * @param onBuilder OkHttp构建器
     * @return OkHttpClient对象
     */
    public static final OkHttpClient createOkHttpClient(final OnOkHttpBuilder onBuilder) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);

        if (RequestChainConfig.isDebug()) {
            LogInterceptor interceptor = new LogInterceptor("OHC");
            interceptor.setLevel(LogInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        /*公共构建起*/
        final OnOkHttpBuilder onOkHttpBuilder = RequestChainConfig.getOnOkHttpBuilder();
        if (onOkHttpBuilder != null) onOkHttpBuilder.onBuilder(builder);

        /*细分构建器*/
        if (onBuilder != null) onBuilder.onBuilder(builder);

        return builder.build();
    }

    /**
     * 创建Retrofit客户端
     *
     * @param url    服务器地址
     * @param client 请求客户端
     * @return a Retrofit
     */
    public static final Retrofit createRetrofit(final String url, final OkHttpClient client, final OnRetrofitBuilder onRetrofitBuilder) {
        Retrofit.Builder build = new Retrofit.Builder();
        build.baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        final OnRetrofitBuilder onRetrofitBuilder1 = RequestChainConfig.getOnRetrofitBuilder();
        if (onRetrofitBuilder1 != null) onRetrofitBuilder1.onBuilder(build);

        if (onRetrofitBuilder != null) onRetrofitBuilder.onBuilder(build);

        return build.build();
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
