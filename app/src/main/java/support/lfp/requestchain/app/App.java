package support.lfp.requestchain.app;

import android.app.Application;

import support.lfp.requestchain.RequestChainConfig;

/**
 * <pre>
 * Tip:
 *
 * Function:
 *
 * Created by LiFuPing on 2019/6/27 10:31
 * </pre>
 */
public class App  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RequestChainConfig.setDebug(true);

    }
}
