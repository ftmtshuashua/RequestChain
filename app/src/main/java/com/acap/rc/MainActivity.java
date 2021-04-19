package com.acap.rc;

import android.app.Activity;
import android.os.Bundle;

import com.acap.ec.listener.OnEventFailureListener;
import com.acap.ec.listener.OnEventSucceedListener;
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


        //----------- Request<> 对象具有EventChain特性 ------------
        DemoApiProvider.mapApi()
                .addOnEventListener((OnEventSucceedListener<String>) result -> System.out.println("API请求的结果:" + result))
                .addOnEventListener((OnEventFailureListener) e -> {
                    System.err.println("请求失败");
                    e.printStackTrace();
                })
                .chain(DemoApiProvider.mapApi())
                .start();
    }
}