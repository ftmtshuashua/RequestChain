package support.lfp.requestchain.api.model

import com.google.gson.Gson
import support.lfp.requestchain.RequestEvent
import support.lfp.requestchain.api.ApiManager
import support.lfp.requestchain.listener.OnRequestSucceedListener
import support.lfp.requestchain.simple.OnEventDelayWaitBar

/**
 * <pre>
 * Tip:
 *
 * Function:
 *
 * Created by LiFuPing on 2019/7/17 11:40
 * </pre>
 */

fun main() {

    RequestEvent(ApiManager.getApi().weatherList, getLifecycle())
            .addOnRequestListener({ s -> mTV_Text.setText(Gson().toJson(s)) } as OnRequestSucceedListener<ModelWeatherList>) //请求成功监听
            .addOnEventListener(OnEventDelayWaitBar(this).setShowThrowable(true)) /*请求进度提示监听*/
            .start()

}