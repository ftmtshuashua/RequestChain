package com.acap.rc.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * <pre>
 * Tip:
 *
 *      Event解析器
 *
 *
 * Created by ACap on 2021/4/15 15:07
 * </pre>
 */
public class RequestAdapterFactory extends CallAdapter.Factory {

//    private Map<Type, RequestAdapter> mCache;

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != Request.class) {
            return null;
        }
        return get(getParameterUpperBound(0, (ParameterizedType) returnType));
    }

    private RequestAdapter get(Type innerType) {
        /*
        if (mCache == null) {
            mCache = new HashMap<>();
        }
        RequestAdapter rcAdapter = mCache.get(innerType);
        if (rcAdapter == null) {
            rcAdapter = new RequestAdapter(innerType);
            mCache.put(innerType, rcAdapter);
        }
        return rcAdapter;*/
        return new RequestAdapter(innerType);
    }
}
