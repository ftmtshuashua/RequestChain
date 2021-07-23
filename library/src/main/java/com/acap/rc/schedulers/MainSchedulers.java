package com.acap.rc.schedulers;

import android.os.Handler;
import android.os.Looper;

/**
 * <pre>
 * Tip:
 *      主线程调度
 *
 * Created by ACap on 2021/7/23 15:50
 * </pre>
 */
public final class MainSchedulers {
    private static final class MainHolder {
        private static final Handler mHandler = new Handler(Looper.getMainLooper());
    }

    public static final void run(Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            MainHolder.mHandler.post(runnable);
        }
    }
}
