package com.acap.rc;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acap.ec.EventChain;
import com.acap.ec.action.Apply;
import com.acap.ec.listener.OnChainListener;
import com.acap.rc.api.XXApiProvider;
import com.acap.rc.bean.BeanData1;
import com.google.gson.Gson;

import java.text.MessageFormat;

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
        XXApiProvider.getJson()
                .clip(params -> "--Clip--")
                .chain(XXApiProvider.getJson())
                .chain(XXApiProvider.getJson2())
                .addOnChainListener(new OnChainListener() {
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
                        Log.e("XXXX", MessageFormat.format("请求数据：{0} -> {1}", node, new Gson().toJson(result)));
                    }

                    @Override
                    public void onChainComplete() {

                    }
                })
                .start();
    }
}
