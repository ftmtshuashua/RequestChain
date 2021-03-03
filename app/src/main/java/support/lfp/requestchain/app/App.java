package support.lfp.requestchain.app;

import android.app.Application;
import android.util.Log;

import com.acap.request.RequestChainConfig;
import com.acap.request.interior.IReqeuestEvent;
import com.acap.request.interior.Logger;
import com.acap.request.listener.OnRequestListener;

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
        public void onStart(IReqeuestEvent event) {
            RequestChainConfig.getLogger().e("全局请求监听器:onStart()");
        }

        @Override
        public void onSucceed(IReqeuestEvent event,Object o) {
            RequestChainConfig.getLogger().e("全局请求监听器:onSucceed()");
//            event.complete();
        }

        @Override
        public void onFailure(IReqeuestEvent event, Throwable ex) {
            RequestChainConfig.getLogger().e("全局请求监听器:onFailure()");
        }

        @Override
        public void onEnd(IReqeuestEvent event) {
            RequestChainConfig.getLogger().e("全局请求监听器:onEnd()");
        }
    }

}
