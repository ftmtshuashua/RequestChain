package com.acap.demo.utils;

import com.google.gson.Gson;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/9/17 15:52
 * </pre>
 */
public class GsonUtils {
    private static Gson mGson = new Gson();

    public static String toJson(Object o) {
        return mGson.toJson(o);
    }
}
