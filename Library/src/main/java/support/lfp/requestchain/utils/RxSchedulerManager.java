package support.lfp.requestchain.utils;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 * Tip:
 *      RX线程调度管理器
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/17 16:55
 * </pre>
 */
public final class RxSchedulerManager {
    private RxSchedulerManager() {
    }

    /**
     * 在IO线程中执行，在Main线程中接收
     */
    public final static <T> ObservableTransformer<T, T> io2main() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
