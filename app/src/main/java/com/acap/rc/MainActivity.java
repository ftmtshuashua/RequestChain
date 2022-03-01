package com.acap.rc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.acap.ec.Event;
import com.acap.ec.Events;
import com.acap.ec.action.Apply;
import com.acap.rc.adapter.Request;
import com.acap.rc.event.OnEventDialog;
import com.acap.ec.listener.OnEventLogListener;
import com.acap.rc.model.BaseModel;

import okhttp3.ResponseBody;

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
        MyApiService.getResponseBody()
                .lazy(params -> MyApiService.getModelError().toEvent()) //延迟初始化,可利用前一个事件的返回值创建当前事件
                .chain(MyApiService.getModelError())
//                .chain(MyApiService.getModel())
                .listener(new OnEventDialog<>(this))  //监听事件的开始与结束
                .listener(new OnEventLogListener<>("请求日志"))
                .start();
    }
}