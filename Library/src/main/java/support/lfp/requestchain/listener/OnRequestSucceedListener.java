package support.lfp.requestchain.listener;

import support.lfp.requestchain.RequestEvent;
import support.lfp.requestchain.interior.IReqeuestEvent;
import support.lfp.requestchain.listener.OnRequestListener;

/**
 * <pre>
 * Tip:
 *      当请求成功
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/25 17:02
 * </pre>
 */
@FunctionalInterface
public interface OnRequestSucceedListener<T> extends OnRequestListener<T> {

    @Override
    default void onStart(IReqeuestEvent event) {
    }

    /**
     * 当请求成功
     */
    void onSucceed(IReqeuestEvent event, T t);

    @Override
    default void onFailure(IReqeuestEvent event, Throwable ex) {

    }

    @Override
    default void onEnd(IReqeuestEvent event) {

    }
}
