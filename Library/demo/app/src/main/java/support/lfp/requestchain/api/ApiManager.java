package support.lfp.requestchain.api;


import com.acap.request.utils.ApiHelper;

public class ApiManager {


    public static ApiService getApi() {
        return ApiHelper.getApi(ApiHelper.getRetrofit("https://api.caiyunapp.com"), ApiService.class);
    }

}
