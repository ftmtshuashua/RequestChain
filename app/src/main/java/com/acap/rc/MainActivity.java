package com.acap.rc;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acap.ec.EventChain;
import com.acap.ec.OnEventLogListener;
import com.acap.ec.listener.OnChainListener;
import com.acap.rc.api.DemoApi;
import com.acap.rc.api.DemoApiProvider;
import com.acap.rc.bean.BeanData;
import com.google.gson.Gson;

import java.text.MessageFormat;

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
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Call<BeanData> json2 = DemoApiProvider.getJson2("", 1);

        json2.enqueue(new Callback<BeanData>() {
            @Override
            public void onResponse(Call<BeanData> call, Response<BeanData> response) {

            }

            @Override
            public void onFailure(Call<BeanData> call, Throwable t) {

            }
        });

        DemoApiProvider.getJson("")
                .chain(DemoApiProvider.getJson2("").chain(DemoApiProvider.getJson3()))
                .chain(DemoApiProvider.getJson3())
                .addOnChainListener(new OnChainLogListener())
                .start();
    }

    private static final class OnChainLogListener implements OnChainListener {
        @Override
        public void onChainStart() {

        }

        @Override
        public void onStart(EventChain node) {

        }

        @Override
        public void onError(EventChain node, Throwable throwable) {

        }

        @Override
        public void onNext(EventChain node, Object result) {
            android.util.Log.e("XXXX", MessageFormat.format("请求数据：{0} -> {1}", node, new Gson().toJson(result)));
        }

        @Override
        public void onChainComplete() {

        }
    }
}
