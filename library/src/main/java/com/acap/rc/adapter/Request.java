package com.acap.rc.adapter;

import com.acap.ec.Event;
import com.acap.rc.schedulers.MainSchedulers;

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
public class Request<T> extends Event<Object, T> implements Callback<T> {
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
        MainSchedulers.run(() -> {
            if (response.isSuccessful()) {
                next(response.body());
            } else {
                error(new HttpException(response));
            }
        });
    }


    @Override
    public void onFailure(Call<T> call, Throwable t) {
        MainSchedulers.run(() -> error(t));
    }

}
