package support.lfp.requestchain;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import support.lfp.requestchain.api.ApiManager;
import support.lfp.requestchain.api.listener.OnRequestShowDialog;
import support.lfp.requestchain.api.model.ModelWeatherList;
import support.lfp.requestchain.demo.R;
import support.lfp.requestchain.listener.OnRequestSucceedListener;

public class MainActivity extends AppCompatActivity {


    TextView mTV_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTV_Text = findViewById(R.id.view_Text);

        getWeatherList();
    }

    /*获得气象列表数据*/
    void getWeatherList() {
        new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
                .setDebugRequestDelay(1000) // 延时1000毫秒之后发起该请求
                .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) s -> mTV_Text.setText(new Gson().toJson(s))) //请求成功监听
                .addOnEventListener(new OnRequestShowDialog(this)) //全局事件监听
                .start();
    }

    void 链式请求() {
        RequestEvent.create(
                new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
                        .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) s -> mTV_Text.setText(new Gson().toJson(s)))
                        .chain(new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
                                .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) s -> mTV_Text.setText(new Gson().toJson(s)))
                              )
                           )
                .addOnEventListener(new OnRequestShowDialog(this)) //全局事件监听
                .start();
    }

    void 并发请求() {
        RequestEvent.create(
                new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
                        .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) s -> mTV_Text.setText(new Gson().toJson(s)))
                ,
                new RequestEvent<>(ApiManager.getApi().getWeatherList(), getLifecycle())
                        .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) s -> mTV_Text.setText(new Gson().toJson(s)))
                           )
                .addOnEventListener(new OnRequestShowDialog(this)) //全局事件监听
                .start();
    }
}
