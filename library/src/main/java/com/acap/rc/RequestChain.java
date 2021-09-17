package com.acap.rc;

/**
 * <pre>
 * Tip:
 *      RequestChain的配置
 *
 * Created by ACap on 2021/9/17 14:29
 * </pre>
 */
public class RequestChain {
    public static final String LOG_TAG = "API";

    private static boolean IsDebug = false;


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


}
