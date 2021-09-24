package com.acap.rc.adapter;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/4/15 15:07
 * </pre>
 */
public class RequestAdapter<R> implements CallAdapter<R, Request<R>> {
    private Type mResponseType;

    public RequestAdapter(Type mResponseType) {
        this.mResponseType = mResponseType;
    }

    @Override
    public Type responseType() {
        return mResponseType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Request<R> adapt(Call<R> call) {
        return new Request(call);
    }
}
