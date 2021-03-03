package com.acap.request.exception;


import androidx.annotation.Nullable;

/**
 * <pre>
 * Tip:
 *      请求异常
 *
 * Function:
 *
 * Created by LiFuPing on 2019/6/26 16:48
 * </pre>
 */
public class RequestException extends Throwable {
    public static final int NULL_CODE = -947725934;

    int code;

    public RequestException(int code) {
        this.code = code;
    }

    public RequestException(@Nullable String message, int code) {
        super(message);
        this.code = code;
    }

    public RequestException(@Nullable String message, @Nullable Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public RequestException(@Nullable Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    /*请求回复码*/
    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return super.toString() +" - Code:"+getCode();
    }
}
