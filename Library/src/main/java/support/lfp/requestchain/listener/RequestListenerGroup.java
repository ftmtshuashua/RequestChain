package support.lfp.requestchain.listener;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import support.lfp.requestchain.RequestEvent;
import support.lfp.requestchain.listener.OnRequestListener;


/**
 * <pre>
 * Tip:
 *     请求监听组管理
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/20 17:40
 * </pre>
 */
public class RequestListenerGroup<E> implements OnRequestListener<E> {

    List<OnRequestListener<? super E>> array;

    public RequestListenerGroup() {
        array = new ArrayList<>();
    }

    public void add(OnRequestListener l) {
        array.add(l);
    }

    public void remove(OnRequestListener l) {
        array.remove(l);
    }


    public void map(Action1<OnRequestListener<? super E>> action1) {
        Iterator<OnRequestListener<? super E>> array = this.array.iterator();
        while (array.hasNext()) {
            action1.call(array.next());
        }
    }

    @Override
    public void onStart(RequestEvent event) {
        map(onRequestListener -> onRequestListener.onStart(event));
    }

    @Override
    public void onSucceed(E e) {
        map(onRequestListener -> onRequestListener.onSucceed(e));
    }

    @Override
    public void onFailure(Throwable ex) {
        map(onRequestListener -> onRequestListener.onFailure(ex));
    }

    @Override
    public void onEnd() {
        map(onRequestListener -> onRequestListener.onEnd());
    }


    @FunctionalInterface
    private interface Action1<T> {
        void call(T t);
    }
}
