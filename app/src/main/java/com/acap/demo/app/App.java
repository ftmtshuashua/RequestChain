package com.acap.demo.app;

import android.app.Application;

import com.acap.rc.BuildConfig;
import com.acap.rc.RequestChain;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/9/17 15:15
 * </pre>
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RequestChain.setDebug(BuildConfig.DEBUG);
    }
}
