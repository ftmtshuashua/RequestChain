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
public class RcAdapter<R> implements CallAdapter<R, Rc<R>> {
    private Type mResponseType;

    public RcAdapter(Type mResponseType) {
        this.mResponseType = mResponseType;
    }

    @Override
    public Type responseType() {
        return mResponseType;
    }

    @Override
    public Rc<R> adapt(Call<R> call) {
        return new Rc(call);
    }
}
