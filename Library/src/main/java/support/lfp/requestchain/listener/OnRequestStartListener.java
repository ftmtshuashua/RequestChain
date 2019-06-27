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
public interface OnRequestStartListener<T> extends OnRequestListener<T> {


    /**
     * 当请求开始的时候调用
     *
     * @param event  正在执行的请求
     */
    void onStart(RequestEvent<T> event);

    @Override
    default void onSucceed(T t) {
    }

    @Override
    default void onFailure(Throwable ex) {}

    @Override
    default void onEnd() {

    }
}
