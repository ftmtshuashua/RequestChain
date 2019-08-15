package support.lfp.requestchain;

import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;

import com.lfp.eventtree.EventChain;
import com.lfp.eventtree.OnEventCompleteListener;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.LambdaObserver;
import support.lfp.requestchain.exception.MsgException;
import support.lfp.requestchain.listener.OnRequestListener;
import support.lfp.requestchain.listener.RequestListenerGroup;
import support.lfp.requestchain.utils.ResponseTransformer;
import support.lfp.requestchain.utils.RxSchedulerManager;

/**
 * <pre>
 * Tip:
 *      一个请求事件
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/21 10:40
 * </pre>
 */
public class RequestEvent<E> extends EventChain {
    /*Debug - 请求延时*/
    private static final int MASK_DEBUG_REQUEST_DELAY = 0xFFFFFF; // 24 bit  max:16777215
    private static final int MASK_DEBUG_ERROR = 0x01 << 24;

    private Observable<E> request;
    private Disposable mDisposable;
    private RequestListenerGroup mOnRequestListenerManager;
    private GenericLifecycleObserver mLifecycleObserver;//将请求绑定到生命周期上 - LifecycleEventObserver
    boolean isCall = false;
    long debug_flag;//Debug 相关标记

    /**
     * 通过Retrofit2构建的Observable创建请求事件
     *
     * @param request 请求
     */
    public RequestEvent(Observable<E> request) {
        this.request = request;
    }

    /**
     * 通过Retrofit2构建的Observable创建请求事件
     *
     * @param request   请求
     * @param lifecycle Activity生命周期管理,该参数可以为空。当他不会空的时候，在Activity关闭的时候可以自动结束请求事件
     */
    public RequestEvent(Observable<E> request, Lifecycle lifecycle) {
        this.request = request;
        if (lifecycle != null) setLifecycle(lifecycle);
    }


    public RequestEvent chain(RequestEvent chain) {
        return (RequestEvent) super.chain(chain);
    }


    /**
     * 设置生命周期管理 ， 在Activity关闭的时候可以自动结束请求事件
     *
     * @param lifecycle Activity 生命周期管理
     * @return
     */
    public RequestEvent<E> setLifecycle(final Lifecycle lifecycle) {
        if (mLifecycleObserver == null) {
            mLifecycleObserver = (source, event) -> {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    interrupt();
                    if (mDisposable != null) mDisposable.dispose();
                }
            };
        }
        lifecycle.addObserver(mLifecycleObserver);
        //请求完成之后从生命周期中移除
        addOnEventListener((OnEventCompleteListener) () -> lifecycle.removeObserver(mLifecycleObserver));
        return this;
    }

    /**
     * 设置请求事件的请求内容 , 该方法不可在已执行的请求事件调用
     *
     * @param request 具体的请求内容
     * @return
     */
    public RequestEvent<E> setRequest(Observable<E> request) {
        if (isCall()) throw new IllegalStateException("The request event is called!");
        this.request = request;
        return this;
    }

    /**
     * 设置Debug延时时间，单位毫秒
     *
     * @param delay 延时时间，最大值为16777215
     */
    public RequestEvent<E> setDebugRequestDelay(long delay) {
        if (!RequestChainConfig.isDebug()) return this;
        debug_flag &= ~MASK_DEBUG_REQUEST_DELAY;
        debug_flag |= (delay & MASK_DEBUG_REQUEST_DELAY);
        return this;
    }

    /**
     * 设置Debug 请求错误,当设置这个标志，当请求成功的之后会强制返回错误
     */
    public RequestEvent<E> setDebugRequestError() {
        if (!RequestChainConfig.isDebug()) return this;
        debug_flag |= MASK_DEBUG_ERROR;
        return this;
    }


    /**
     * 添加请求事件监听
     *
     * @param l
     * @return
     */
    public RequestEvent<E> addOnRequestListener(OnRequestListener<? super E> l) {
        if (mOnRequestListenerManager == null)
            mOnRequestListenerManager = new RequestListenerGroup();
        mOnRequestListenerManager.add(l);
        return this;
    }

    /**
     * 移除请求事件监听
     *
     * @param l
     * @return
     */
    public RequestEvent<E> removeRequestListener(OnRequestListener<? super E> l) {
        if (mOnRequestListenerManager != null) mOnRequestListenerManager.remove(l);
        return this;
    }

    @Override
    public void interrupt() {
        if (mDisposable != null && !mDisposable.isDisposed()) mDisposable.dispose();
        super.interrupt();
    }


    public boolean isCall() {
        return isCall;
    }

    @Override
    protected void call() throws Throwable {
        if (request != null) {
            isCall = true;
            LambdaObserver observer = new LambdaObserver<E>(t ->
                    setRequestSucceed(t)
                    , throwable -> setRequestError(throwable)
                    , () -> { }
                    , disposable -> setRequestStart()
            );


            mDisposable = observer;


            Observable<E> observable = request;
            long delay = debug_flag & MASK_DEBUG_REQUEST_DELAY;
            if (delay != 0) {
                RequestChainConfig.getLogger().i(MessageFormat.format("Debug：当前请求将在 {0,number,0}ms 后执行！", delay));
                observable = observable.delaySubscription(delay, TimeUnit.MILLISECONDS); //延迟加载测试
            }
            observable
                    .compose(ResponseTransformer.handle())
                    .compose(RxSchedulerManager.io2main())
                    .subscribe(observer);
        } else {
            next();
        }
    }

    /** 请求开始执行 */
    protected void setRequestStart() {
        listenerStart();
    }

    /** 请求开始执行 */
    protected void setRequestSucceed(E t) {
        if ((debug_flag & MASK_DEBUG_ERROR) != 0) {
            setRequestError(new MsgException("Debug：强制抛出异常测试!"));
        } else {
            try { /* 捕获数据处理异常，在数据处理错误的时候不至于导致程序崩溃。 */
                listenerSucceed(t);
            } catch (Exception e) {
                if (RequestChainConfig.isDebug()) e.printStackTrace();
                setRequestError(e);
                return;
            }

            listenerEnd();
            next();
        }
    }

    /** 请求开始执行 */
    protected void setRequestError(Throwable throwable) {
        listenerFailure(throwable);
        listenerEnd();
        error(throwable);
    }

    private void listenerStart() {
        if (RequestChainConfig.getGlobalRequestListener() != null)
            RequestChainConfig.getGlobalRequestListener().onStart(this);
        if (mOnRequestListenerManager != null)
            mOnRequestListenerManager.onStart(this);
    }

    private void listenerSucceed(E t) {
        if (RequestChainConfig.getGlobalRequestListener() != null)
            RequestChainConfig.getGlobalRequestListener().onSucceed(t);
        if (mOnRequestListenerManager != null) mOnRequestListenerManager.onSucceed(t);
    }

    private void listenerEnd() {
        if (RequestChainConfig.getGlobalRequestListener() != null)
            RequestChainConfig.getGlobalRequestListener().onEnd();
        if (mOnRequestListenerManager != null) mOnRequestListenerManager.onEnd();
    }

    private void listenerFailure(Throwable throwable) {
        if (RequestChainConfig.getGlobalRequestListener() != null)
            RequestChainConfig.getGlobalRequestListener().onFailure(throwable);
        if (mOnRequestListenerManager != null)
            mOnRequestListenerManager.onFailure(throwable);
    }
}
