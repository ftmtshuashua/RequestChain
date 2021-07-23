package com.acap.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.acap.demo.api.GetmanApiProvider;
import com.acap.demo.utils.LogUtils;
import com.google.gson.Gson;

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
        Button button = new Button(this);
        button.setOnClickListener(v -> run());
        setContentView(button);
    }

    private void run() {

        Call<String> apiCall = GetmanApiProvider.getApiCall();
        apiCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                LogUtils.i("请求成功:{0}", new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                LogUtils.e("请求失败:{0}", t);
            }
        });


//        GetmanApiProvider.getApiEvent()
//                .listener((OnEventNextListener<Object, GetmanDemo>) result -> {
//                    LogUtils.i("内容:{0}", new Gson().toJson(result));
//                })
//                .listener(new OnEventLogListener<>("Request"))
//                .chain(GetmanApiProvider.getApiEvent()) //再次请求
//                .start();

        //----------- 原生Retrofit2 ------------

    }
}