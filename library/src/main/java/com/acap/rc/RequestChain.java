package com.acap.rc;

import com.acap.rc.logs.HttpLogInterceptor;

/**
 * <pre>
 * Tip:
 *      RequestChain的配置
 *
 * Created by ACap on 2021/9/17 14:29
 * </pre>
 */
public class RequestChain {
    private static String LOG_TAG = "API";
    private static boolean IsDebug = false;
    private static boolean IsLogBody = true;
    private static boolean IsLogHeader = false;


    /**
     * 设置 RequestChain 的 Debug 模式
     * <p>
     * 当 Debug 模式启用时，会打印 API 的请求与响应.可在 Logcat 中使用关键字 "API" 进行查看
     *
     * @param isDebug
     */
    public static void setDebug(boolean isDebug) {
        IsDebug = isDebug;
    }

    /**
     * RequestChain 的 Debug 模式
     * <p>
     * 当 Debug 模式启用时，会打印 API 的请求与响应.可在 Logcat 中使用关键字 "API" 进行查看
     */
    public static boolean isDebug() {
        return IsDebug;
    }

    /**
     * 设置日志的Tag
     */
    public static void setLogTag(String tag) {
        LOG_TAG = tag;
    }

    /**
     * 设置是否打印 Body 日志 ，默认启用
     */
    public static void setLogBody(boolean enable) {
        IsLogBody = enable;
    }

    /**
     * 设置是否打印 Header 日志 ，默认禁用
     */
    public static void setLogHeader(boolean enable) {
        IsLogHeader = enable;
    }


    public static HttpLogInterceptor getLog() {
        HttpLogInterceptor interceptor = new HttpLogInterceptor(RequestChain.LOG_TAG);
        interceptor.setEnableLog(isDebug());
        interceptor.setEnableBodyLog(IsLogBody);
        interceptor.setEnableHeaderLog(IsLogHeader);
        return interceptor;
    }

}
