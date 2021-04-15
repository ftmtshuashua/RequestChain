package com.acap.rc.adapter;

import com.acap.ec.EventChain;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * <pre>
 * Tip:
 *      适配EventChain的请求
 *
 * Created by ACap on 2021/4/15 15:07
 * </pre>
 */
public class Rc<T> extends EventChain<Object, T> implements Callback<T> {
    private Call<T> mCall;

    public Rc(Call<T> call) {
        this.mCall = call;
    }

    @Override
    protected void onCall(Object params) {
        mCall.enqueue(this);
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            next(response.body());
        } else {
            error(new HttpException(response));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        error(t);
    }

}
