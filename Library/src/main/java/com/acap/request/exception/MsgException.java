package com.acap.request.exception;

/**
 * <pre>
 * Tip:
 *      消息异常
 *
 * Function:
 *
 * Created by LiFuPing on 2019/6/26 16:48
 * </pre>
 */
public class MsgException extends RuntimeException {
    public MsgException(String message) {
        super(message);
    }
}
