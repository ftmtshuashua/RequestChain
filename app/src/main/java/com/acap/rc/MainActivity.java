package com.acap.rc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.acap.rc.event.OnEventDialog;
import com.acap.ec.listener.OnEventLogListener;

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
        TdApiService.getDevice( "xx", "xx", "cq", "dev", "android")
//        MyApiService.getResponseBody()
//                .chain(MyApiService.getModelError())
//                .chain(MyApiService.getModel())
                .listener(new OnEventDialog<>(this))
                .listener(new OnEventLogListener<>("请求日志"))
                .start();
    }
}