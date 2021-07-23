package com.acap.demo.utils;

import android.util.Log;

import java.text.MessageFormat;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/7/23 16:17
 * </pre>
 */
public class LogUtils {
    public static final String TAG = "LogUtils";

    public static final void i(String msg) {
        Log.i(TAG, msg);
    }

    public static final void i(String pattern, Object... s) {
        i(MessageFormat.format(pattern, s));
    }

    public static final void e(Throwable e) {
        Log.e(TAG, "", e);
    }

    public static final void e(String msg) {
        Log.e(TAG, msg);
    }

    public static final void e(String pattern, Object... s) {
        e(MessageFormat.format(pattern, s));
    }
}
