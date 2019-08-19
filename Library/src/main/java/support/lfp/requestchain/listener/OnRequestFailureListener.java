package support.lfp.requestchain.listener;


import support.lfp.requestchain.RequestEvent;
import support.lfp.requestchain.interior.IReqeuestEvent;
import support.lfp.requestchain.listener.OnRequestListener;

/**
 * <pre>
 * Tip:
 *      当请求失败
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/25 17:02
 * </pre>
 */
@FunctionalInterface
public interface OnRequestFailureListener<T> extends OnRequestListener<T> {

    @Override
    default void onStart(IReqeuestEvent event) {
    }

    @Override
    default void onSucceed(IReqeuestEvent event,T t) {
    }

    /**
     * 访问失败，你包括本地失败和服务器失败
     */
    void onFailure(IReqeuestEvent event, Throwable ex);

    @Override
    default void onEnd(IReqeuestEvent event) {

    }
}
