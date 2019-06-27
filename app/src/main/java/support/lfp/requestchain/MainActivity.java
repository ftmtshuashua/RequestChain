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
                .setDebugRequestDelay(1000)
                .addOnRequestListener((OnRequestSucceedListener<ModelWeatherList>) s -> mTV_Text.setText(new Gson().toJson(s)))
                .addOnEventListener(new OnRequestShowDialog(this))
                .start();
    }


}
