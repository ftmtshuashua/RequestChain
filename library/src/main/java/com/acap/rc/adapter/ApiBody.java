package com.acap.rc.adapter;

/**
 * <pre>
 * Tip:
 *      Api请求的 Body
 *
 * @author A·Cap at 2021/11/19 17:59
 * </pre>
 */
public interface ApiBody {
    /**
     * 当Http请求成功时候 ，用来确定Body内容是否合法
     */
    boolean isSuccessful();

    /**
     * 当 {@link ApiBody#isSuccessful()} == false 的时候,返回失败的原因
     */
    Exception getError();
}
