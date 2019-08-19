package support.lfp.requestchain.listener;


import support.lfp.requestchain.interior.IReqeuestEvent;

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
public interface OnRequestEndListener<T> extends OnRequestListener<T> {


    @Override
    default void onStart(IReqeuestEvent event) {}

    @Override
    default void onSucceed(IReqeuestEvent event, T t) {
    }

    @Override
    default void onFailure(IReqeuestEvent event, Throwable ex) {}

    /**
     * 当请求完成的时候调用
     */
    void onEnd(IReqeuestEvent event);
}
