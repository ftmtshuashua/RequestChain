package support.lfp.requestchain.api.model;

import support.lfp.requestchain.utils.ResponseTransformer;

/**
 * <pre>
 * Tip:
 *      天气列表
 *
 * Function:
 *
 * Created by LiFuPing on 2019/6/27 13:56
 * </pre>
 */
public class ModelWeatherList implements ResponseTransformer.IResponseException {
    String status;
    String lang;
    String unit;
    long server_time;
    float[] location;
    String api_status;
    long tzshift;
    String api_version;
    Result result;

    @Override
    public boolean isSucceed() {
        return false;
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getMsg() {
        return null;
    }

    public static final class Result {
        String status;
        float o3;
        float co;
        long temperature;
        float pm10;
        String skycon;
        float cloudrate;
        Precipitation precipitation;
        float dswrf;
        float visibility;
        float humidity;
        float so2;
        Ultraviolet ultraviolet;
        float pres;
        long aqi;
        float pm25;
        float no2;
        float apparent_temperature;
        Comfort comfort;
        Wind wind;

        public static final class Precipitation {
            Nearest nearest;
            Local local;

            public static final class Nearest {
                String status;
                float distance;
                float intensity;
            }

            public static final class Local {
                String status;
                float intensity;
                String datasource;
            }
        }

        public static final class Ultraviolet {
            long index;
            String desc;
        }

        public static final class Comfort {
            long index;
            String desc;
        }

        public static final class Wind {
            float direction;
            float speed;
        }

    }

}
