package com.acap.request.listener;


import com.acap.request.interior.IReqeuestEvent;

/**
 * <pre>
 * Tip:
 *      请求监听
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/18 11:48
 * </pre>
 */
public interface OnRequestListener<T> {

    /**
     * 请求发起的时候
     */
    void onStart(IReqeuestEvent event);

    /**
     * 请求成功
     */
    void onSucceed(IReqeuestEvent event, T t);

    /**
     * 访问失败，你包括本地失败和服务器失败
     */
    void onFailure(IReqeuestEvent event, Throwable ex);

    /**
     * 请求结束之后
     */
    void onEnd(IReqeuestEvent event);
}
