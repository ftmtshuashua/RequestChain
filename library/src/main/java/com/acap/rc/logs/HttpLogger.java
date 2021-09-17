package com.acap.rc.logs;

/**
 * <pre>
 * Tip:
 *      请求日志打印器
 *
 * Created by ACap on 2021/5/10 16:03
 * </pre>
 */
public interface HttpLogger {
    /**
     * 日志打印
     * @param tag 日志 Tag
     * @param msg 日志 数据
     */
    void print(String tag, String msg);
}
