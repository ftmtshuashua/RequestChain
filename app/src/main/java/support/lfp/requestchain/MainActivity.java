package support.lfp.requestchain;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lfp.eventtree.EventChain;

import support.lfp.requestchain.api.ApiManager;
import support.lfp.requestchain.api.model.ModelWeatherList;
import support.lfp.requestchain.demo.R;
import support.lfp.requestchain.listener.OnEventChainLogShow;
import support.lfp.requestchain.listener.OnRequestSucceedListener;
import support.lfp.requestchain.simple.OnEventDelayWaitBar;

public class MainActivity extends AppCompatActivity {


    TextView mTV_Text;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTV_Text = findViewById(R.id.view_Text);
        mTV_Text.setOnClickListener(view -> getWeatherList());

//        getWeatherList();
    }

    /*获得气象列表数据*/
    void getWeatherList() {
        mTV_Text.setText("正在加载..");
        new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
                .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) (e, s) -> mTV_Text.setText(new Gson().toJson(s))) //请求成功监听
                .addOnEventListener(new OnEventDelayWaitBar(this).setShowThrowable(true)) /*请求进度提示监听*/
                .start();
    }

    void 链式请求() {
        RequestEvent.create(
                new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
                        .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) (e, s) -> mTV_Text.setText(new Gson().toJson(s)))
                        .chain(new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
                                .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) (e, s) -> mTV_Text.setText(new Gson().toJson(s)))
                              )
                           )
                .addOnEventListener(new OnEventDelayWaitBar(this)) //全局事件监听
                .start();
    }

    void 并发请求() {
        RequestEvent.create(
                new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
                        .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) (e,s) -> mTV_Text.setText(new Gson().toJson(s)))
                ,
                new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
                        .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) (e,s)-> mTV_Text.setText(new Gson().toJson(s)))
                           )
                .addOnEventListener(new OnEventDelayWaitBar(this)) //全局事件监听
                .start();
    }


    void 测试_不回调问题() {
        Log.e("TestEvent", "正在测试不回调问题()");
        mTV_Text.setText("正在测试不回调问题..");
        RequestEvent.create(
                new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
                        .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) (e,s) -> mTV_Text.setText(new Gson().toJson(s))) //请求成功监听
                        .chain(new TestEvent(true))
                        .chain(new TestEvent(false))
                           )
                .addOnEventListener(new OnEventDelayWaitBar(this).setShowThrowable(true)) /*请求进度提示监听*/
                .addEventChainObserver(new OnEventChainLogShow())
                .start();


//        new TestEvent(true).start();

//        new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
//                .chain(new TestEvent(true))
//                .addEventChainObserver(new OnEventChainLogShow())
//                .start();

//        new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
//                .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) s -> mTV_Text.setText(new Gson().toJson(s))) //请求成功监听
//                .chain(new TestEvent(true))
//                .chain(new TestEvent(false))
//                .start();

    }


    private static final class TestEvent extends EventChain {
        boolean succenful;

        public TestEvent(boolean succenful) {
            this.succenful = succenful;
        }

        @Override
        protected void call() throws Throwable {
            if (succenful) {
                Log.e("TestEvent", "执行 next()");
                next();
            } else {
                Log.e("TestEvent", "执行 error()");
                error(new IllegalStateException("测试异常输出"));
            }

        }
    }


}
