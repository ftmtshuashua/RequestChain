package com.acap.rc.logs;

/**
 * <pre>
 * Tip:
 *      输出的日志
 *
 * @author A·Cap
 * @date 2021/12/29 10:48
 * </pre>
 */
public class HttpLogMessage {
    //日志等级
    final int mLevel;
    //日志消息
    final String mMsg;

    public HttpLogMessage(int mLevel, String mMsg) {
        this.mLevel = mLevel;
        this.mMsg = mMsg;
    }
}
