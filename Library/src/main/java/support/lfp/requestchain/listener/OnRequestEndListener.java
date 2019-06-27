package support.lfp.requestchain.listener;


import support.lfp.requestchain.RequestEvent;

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
    default void onStart(RequestEvent<T> event) {}

    @Override
    default void onSucceed(T t) {
    }

    @Override
    default void onFailure(Throwable ex) {}

    /**
     * 当请求完成的时候调用
     */
    void onEnd();
}
