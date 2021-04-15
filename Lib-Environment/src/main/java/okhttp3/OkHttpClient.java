package okhttp3;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/4/15 16:56
 * </pre>
 */
public class OkHttpClient {

    public static class Builder {
        public Builder connectTimeout(long timeout, TimeUnit unit) {
            return this;
        }

        public Builder readTimeout(long timeout, TimeUnit unit) {
            return this;
        }

        public Builder writeTimeout(long timeout, TimeUnit unit) {
            return this;
        }

    }
}
