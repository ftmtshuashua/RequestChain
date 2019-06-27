package support.lfp.requestchain.api;


import support.lfp.requestchain.utils.ApiHelper;

public class ApiManager {


    public static ApiService getApi() {
        return ApiHelper.getApi(ApiHelper.getRetrofit("https://api.caiyunapp.com"), ApiService.class);
    }

}
