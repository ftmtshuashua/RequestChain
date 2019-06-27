package support.lfp.requestchain.listener;


import support.lfp.requestchain.RequestChainConfig;
import support.lfp.requestchain.RequestEvent;
import support.lfp.requestchain.listener.OnRequestListener;

/**
 * <pre>
 * Tip:
 *      事件日志显示
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/21 14:36
 * </pre>
 */
public class OnRequestLogShow implements OnRequestListener {
    String tab = "A request";

    public OnRequestLogShow() {
    }

    public OnRequestLogShow(String tab) {
        this.tab = tab;
    }

    @Override
    public void onStart(RequestEvent event) {
        RequestChainConfig.getLogger().i(tab + " onStart：" + event.toString());
    }

    @Override
    public void onSucceed(Object o) {
        RequestChainConfig.getLogger().i(tab + " onSucceed：" + o.toString());
    }

    @Override
    public void onFailure(Throwable ex) {
        RequestChainConfig.getLogger().e(tab + " onFailure：" + ex.toString());
    }

    @Override
    public void onEnd() {
        RequestChainConfig.getLogger().i(tab + " onEnd()");
    }
}
