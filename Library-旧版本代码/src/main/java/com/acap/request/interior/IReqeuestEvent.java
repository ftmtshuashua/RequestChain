package com.acap.request.interior;

import com.lfp.eventtree.EventChain;

/**
 * <pre>
 * Tip:
 *
 * Function:
 *
 * Created by LiFuPing on 2019/8/19 10:08
 * </pre>
 */
public interface IReqeuestEvent {

    /**
     * 停止后续事件执行
     */
    void interrupt();

    /** 完成事件链 */
    void complete();

    /** 在当前事件后面接入一个新的事件 */
    EventChain chain(EventChain chain);

    /** 检查后续事件是否被中断，如果这条链被中断，后续所有事件都不应该被执行 */
    boolean isInterrupt();

    /** 检查事件链是否完成 */
    boolean isComplete();

    /** 验证后续事件是否可被执行 */
    boolean isProcess();
}
