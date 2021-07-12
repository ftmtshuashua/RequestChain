package com.acap.rc;

import android.app.Activity;
import android.os.Bundle;

import com.acap.ec.listener.OnEventErrorListener;
import com.acap.ec.listener.OnEventNextListener;
import com.acap.rc.api.DemoApiProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/4/15 14:41
 * </pre>
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //----------- 原生Retrofit2 ------------
        DemoApiProvider.retrofit2Api().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //请求结果
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //请求失败
            }
        });

        DemoApiProvider.mapApi()
                .listener((OnEventNextListener<Object, String>) result -> {

                })
                .listener((OnEventErrorListener<Object, String>) e -> {


                })

                .merge(DemoApiProvider.mapApi(), DemoApiProvider.mapApi(), DemoApiProvider.mapApi())
                .chain(DemoApiProvider.mapApi())
                .start();


        //----------- Request<> 对象具有EventChain特性 ------------
        DemoApiProvider.mapApi()
                .listener((OnEventNextListener<Object, String>) result -> System.out.println("API请求的结果:" + result))
                .listener((OnEventErrorListener<Object, String>) e -> System.err.println("请求失败:" + e))
                .chain(DemoApiProvider.mapApi())
                .start();
    }
}