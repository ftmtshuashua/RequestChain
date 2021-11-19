package com.acap.rc.schedulers;

import android.os.Handler;
import android.os.Looper;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * <pre>
 * Tip:
 *      主线程调度
 *
 * Created by ACap on 2021/7/23 15:50
 * </pre>
 */
public final class RequestThreadHelper {
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    private static final ExecutorService mRequest = Executors.newCachedThreadPool(new MyThreadFactory("Request"));

    /**主线程中运行*/
    public static final void main(Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }

    /**子线程中运行*/
    public static final void request(Runnable runnable) {
        mRequest.submit(runnable);
    }

    private static final class MyThreadFactory implements ThreadFactory {
        private String name;
        private int index = 0;

        public MyThreadFactory(String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, MessageFormat.format("{0}-{1,number,0}", name, index++));
        }
    }
}
