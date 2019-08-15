package support.lfp.requestchain.app;

import android.app.Application;
import android.util.Log;

import support.lfp.requestchain.RequestChainConfig;
import support.lfp.requestchain.RequestEvent;
import support.lfp.requestchain.interior.Logger;
import support.lfp.requestchain.listener.OnRequestListener;

/**
 * <pre>
 * Tip:
 *
 * Function:
 *
 * Created by LiFuPing on 2019/6/27 10:31
 * </pre>
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RequestChainConfig.setDebug(true);
        RequestChainConfig.setLogger(new Logger() {
            @Override
            public void i(String msg) {
                Log.i("Demo",msg);
            }

            @Override
            public void e(String msg) {
                Log.e("Demo",msg);
            }
        });


        RequestChainConfig.addOnRequestListenerGlobal(new GlobalRequestListener());

    }


    /*全局事件监听，所有请求事件都会先走该监听器*/
    private static final class GlobalRequestListener implements OnRequestListener {

        @Override
        public void onStart(RequestEvent event) {
            RequestChainConfig.getLogger().e("全局请求监听器:onStart()");
        }

        @Override
        public void onSucceed(Object o) {
            RequestChainConfig.getLogger().e("全局请求监听器:onSucceed()");
        }

        @Override
        public void onFailure(Throwable ex) {
            RequestChainConfig.getLogger().e("全局请求监听器:onFailure()");
        }

        @Override
        public void onEnd() {
            RequestChainConfig.getLogger().e("全局请求监听器:onEnd()");
        }
    }

}
