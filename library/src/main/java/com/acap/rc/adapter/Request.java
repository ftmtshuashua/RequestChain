package com.acap.rc.adapter;

import com.acap.ec.BaseEvent;
import com.acap.ec.Event;
import com.acap.rc.schedulers.RequestThreadHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * <pre>
 * Tip:
 *      适配Event的请求
 *
 * Created by ACap on 2021/4/15 15:07
 * </pre>
 */
public class Request<T> extends BaseEvent<Object, T> implements Callback<T> {
    private Call<T> mCall;


    public Request(Call<T> call) {
        this.mCall = call;
    }

    @Override
    protected void onCall(Object params) {
        mCall.enqueue(this);
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        RequestThreadHelper.main(() -> {
            if (response.isSuccessful()) {
                T body = response.body();
                if (body instanceof ApiBody) {
                    ApiBody apiBody = (ApiBody) body;
                    if (apiBody.isSuccessful()) {
                        next(body);
                    } else {
                        error(apiBody.getError());
                    }
                } else {
                    next(body);
                }
            } else {
                error(new HttpException(response));
            }
        });
    }


    @Override
    public void onFailure(Call<T> call, Throwable t) {
        RequestThreadHelper.main(() -> error(t));
    }

    /**
     * 转换为Event对象。由于Request的入参固定为Object，导致某些时候必须进行强转，极其不便。使用该方法可以直接将Request对象强转为不关心入参的Event对象。
     *
     * @param <R> Request 对象不关心入参的类型
     * @return Event对象
     */
    @SuppressWarnings("unchecked")
    public <R> Event<R, T> toEvent() {
        return (Event<R, T>) this;
    }
}
