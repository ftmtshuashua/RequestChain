package retrofit2;


import okhttp3.OkHttpClient;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/4/15 17:04
 * </pre>
 */
public final class Retrofit {

    public static final class Builder {

        public Builder baseUrl(String baseUrl) {
            return this;
        }

        public Builder client(OkHttpClient client) {
            return this;
        }

        public Builder addConverterFactory(Converter.Factory factory){
            return this;
        }

        public Builder addCallAdapterFactory(CallAdapter.Factory factory){
            return this;
        }
    }
}
