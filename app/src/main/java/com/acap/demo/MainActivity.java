package com.acap.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.acap.ec.listener.OnEventLogListener;
import com.acap.ec.listener.OnEventNextListener;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
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
        button.setText("请求");
        button.setOnClickListener(v -> run());
        setContentView(button);
    }

    private void run() {
        MyApiService.getMan()
                .listener((OnEventNextListener<Object, ResponseBody>) result -> {
                    try { Log.i("API:打印请求结果", result.string()); } catch (IOException e) { }
                })
                .listener(new OnEventLogListener<>("APIS"))
                .start();

//        new Thread(() -> {
//            try {
//
//                Call<ResponseBody> man2 = MyApiService.getMan2();
//                Response<ResponseBody> execute = man2.execute();
//                Log.i("API:打印请求结果", execute.body().string());
//
//                execute = man2.execute();
//                Log.i("API:打印请求结果2", execute.body().string());
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
    }
}