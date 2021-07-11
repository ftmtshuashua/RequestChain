package com.acap.rc.adapter;

import android.os.Handler;
import android.os.Looper;

import com.acap.ec.Event;

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

    //在主线程回调
    private Handler mHandler;

    public Request(Call<T> call) {
        this.mCall = call;
        Looper looper = Looper.myLooper();
        if (looper != null) {
            mHandler = new Handler(looper);
        }
    }

    @Override
    protected void onCall(Object params) {
        mCall.enqueue(this);
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (mHandler == null) {
            performOnResponse(call, response);
        } else {
            mHandler.post(() -> performOnResponse(call, response));
        }
    }

    private void performOnResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            next(response.body());
        } else {
            error(new HttpException(response));
        }
    }


    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (mHandler != null) {
            mHandler.post(() -> error(t));
        } else {
            error(t);
        }
    }

}
