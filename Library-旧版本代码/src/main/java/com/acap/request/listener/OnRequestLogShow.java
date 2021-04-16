package com.acap.request.listener;


import com.acap.request.RequestChainConfig;
import com.acap.request.interior.IReqeuestEvent;

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

    public OnRequestLogShow(IReqeuestEvent event,String tab) {
        this.tab = tab;
    }

    @Override
    public void onStart(IReqeuestEvent event) {
        RequestChainConfig.getLogger().i(tab + " onStart：" + event.toString());
    }

    @Override
    public void onSucceed(IReqeuestEvent event,Object o) {
        RequestChainConfig.getLogger().i(tab + " onSucceed：" + o.toString());
    }

    @Override
    public void onFailure(IReqeuestEvent event, Throwable ex) {
        RequestChainConfig.getLogger().e(tab + " onFailure：" + ex.toString());
    }

    @Override
    public void onEnd(IReqeuestEvent event) {
        RequestChainConfig.getLogger().i(tab + " onEnd()");
    }
}
