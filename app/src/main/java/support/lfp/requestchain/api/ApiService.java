package support.lfp.requestchain.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import support.lfp.requestchain.api.model.ModelWeatherList;

/**
 * <pre>
 * Tip:
 *      基础服务
 * Function:
 *
 * Created by LiFuPing on 2019/6/4 16:52
 * </pre>
 */
public interface ApiService {

    /**
     * 获得可用城市列表
     */
    @GET("/v2/TAkhjf8d1nlSlspN/121.6544,25.1552/realtime.json")
    Observable<ModelWeatherList> getWeatherList();

}
