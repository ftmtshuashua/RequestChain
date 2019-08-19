package support.lfp.requestchain.listener;

import com.lfp.eventtree.EventChain;
import com.lfp.eventtree.EventChainObserver;

import support.lfp.requestchain.RequestChainConfig;

/**
 * <pre>
 * Tip:
 *      事件链条日志
 *
 * Function:
 *
 * Created by LiFuPing on 2019/1/12 16:38
 * </pre>
 */
public class OnEventChainLogShow implements EventChainObserver {
    String tab = "A chain";

    public OnEventChainLogShow() {
    }

    public OnEventChainLogShow(String tab) {
        this.tab = tab;
    }


    @Override
    public void onChainStart() {
        RequestChainConfig.getLogger().e(tab + " onChainStart()");
    }

    @Override
    public void onStart(EventChain event) {
        RequestChainConfig.getLogger().e(tab + " onStart：" + event);
    }

    @Override
    public void onError(EventChain event, Throwable e) {
        RequestChainConfig.getLogger().e(tab + " onError：" + event + ",Throwable:" + e.getMessage());
    }

    @Override
    public void onNext(EventChain event) {
        RequestChainConfig.getLogger().e(tab + " onNext：" + event);
    }

    @Override
    public void onChainComplete() {
        RequestChainConfig.getLogger().e(tab + " onChainComplete()");
    }
}
