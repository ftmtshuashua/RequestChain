package com.acap.request;


import com.acap.request.interior.Logger;
import com.acap.request.interior.OnOkHttpBuilder;
import com.acap.request.interior.OnRetrofitBuilder;
import com.acap.request.interior.SimpleLogger;
import com.acap.request.listener.OnRequestListener;
import com.acap.request.listener.RequestListenerGroup;

/**
 * <pre>
 * Tip:
 *      请求连配置
 *
 * Function:
 *
 * Created by LiFuPing on 2019/6/26 14:32
 * </pre>
 */
public final class RequestChainConfig {

    static boolean isDebug = false;
    static Logger mLogger = new SimpleLogger();
    static Logger mNullLogger = new NullLogger();
    static OnOkHttpBuilder mOnOkHttpBuilder;
    static OnRetrofitBuilder mOnRetrofitBuilder;
    static RequestListenerGroup mGlobalRequestListener; /*全局请求监听器*/


    private RequestChainConfig() {}


    /**
     * 配置全局Retrofit构建
     *
     * @param builder
     */
    public static final void setOnRetrofitBuilder(OnRetrofitBuilder builder) {
        mOnRetrofitBuilder = builder;
    }

    /**
     * 获得全局Retrofit构建器
     *
     * @return
     */
    public static final OnRetrofitBuilder getOnRetrofitBuilder() {return mOnRetrofitBuilder;}


    /**
     * 配置全局OkHttp构建
     *
     * @param builder
     */
    public static final void setOnOkHttpBuilder(OnOkHttpBuilder builder) {
        mOnOkHttpBuilder = builder;
    }

    /**
     * 获得全局OkHttp构建器
     *
     * @return
     */
    public static final OnOkHttpBuilder getOnOkHttpBuilder() {return mOnOkHttpBuilder;}


    /*------------------------------- Log -----------------------------*/

    /***设置日志处理器*/
    public static final void setLogger(Logger logger) {
        mLogger = logger;
    }


    /***获得日志处理器*/
    public static final Logger getLogger() {
        if (!isDebug()) return mNullLogger;
        return mLogger;
    }

    /**
     * 判断是否为Debug模式
     */
    public static boolean isDebug() {
        return isDebug;
    }

    /** 设置Debug模式，如果不是处于Debug模式则不能收到调试信息 */
    public static void setDebug(boolean isDebug) {
        RequestChainConfig.isDebug = isDebug;
    }


    /**
     * 无任何实现
     */
    private static final class NullLogger extends Logger {

        @Override
        public void i(String msg) {

        }

        @Override
        public void e(String msg) {

        }
    }


    /*------------------------------- Log -----------------------------*/


    /**
     * 添加全局请求事件监听 ,所求请求事件
     *
     * @param l
     * @return
     */
    public static void addOnRequestListenerGlobal(OnRequestListener<?> l) {
        if (mGlobalRequestListener == null)
            mGlobalRequestListener = new RequestListenerGroup();
        mGlobalRequestListener.add(l);

    }

    /**
     * 移除全局请求事件监听
     *
     * @param l
     * @return
     */
    public static void removeRequestListenerGlobal(OnRequestListener<?> l) {
        if (mGlobalRequestListener != null) mGlobalRequestListener.remove(l);
    }

    /**获得全局请求监听器,该监听器应该在所有请求自己的监听器之前被执行*/
    public static RequestListenerGroup getGlobalRequestListener() {
        return mGlobalRequestListener;
    }

}
