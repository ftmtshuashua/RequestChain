package com.acap.rc.logs;

import android.text.TextUtils;
import android.util.Log;

import com.acap.rc.RequestChain;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * <pre>
 * Tip:
 *      Http请求日志
 *
 * Created by ACap on 2021/5/10 14:32
 * </pre>
 */
public class HttpLogInterceptor implements Interceptor {

    /**
     * 基础信息
     */
    private static final int LEVEL_BASIC = 0x1;
    /**
     * 显示Headers信息
     */
    public static final int LEVEL_HEADERS = 0x1 << 1;
    /**
     * 显示Body信息
     */
    public static final int LEVEL_BODY = 0x1 << 2;


    private final String TAG;
    private int mLevel = LEVEL_BASIC | LEVEL_BODY;

    //限制Body显示的最大行数
    private final int LIMIT_MAX_BODY_LINES = 30;

    private HttpLogger mLogger = Log::i;

    public HttpLogInterceptor(String TAG) {
        this.TAG = TAG;
    }


    /**
     * 设置日志启用状态
     *
     * @param enable is enable
     * @return this
     */
    public HttpLogInterceptor setEnableLog(boolean enable) {
        if (enable) {
            mLevel |= LEVEL_BASIC;
        } else {
            mLevel &= ~LEVEL_BASIC;
        }
        return this;
    }

    /**
     * 设置是否显示请求的Body信息
     *
     * @param enable is enable
     * @return this
     */
    public HttpLogInterceptor setEnableBodyLog(boolean enable) {
        if (enable) {
            mLevel |= LEVEL_BODY;
        } else {
            mLevel &= ~LEVEL_BODY;
        }
        return this;
    }

    /**
     * 设置是否显示请求的Header信息
     *
     * @param enable is enable
     * @return this
     */
    public HttpLogInterceptor setEnableHeaderLog(boolean enable) {
        if (enable) {
            mLevel |= LEVEL_HEADERS;
        } else {
            mLevel &= ~LEVEL_HEADERS;
        }
        return this;
    }

    /**
     * 设置日志显示
     *
     * @param logger is logger
     * @return this
     */
    public HttpLogInterceptor setHttpLogger(HttpLogger logger) {
        mLogger = logger;
        return this;
    }

    /**
     * 判断是否启用日志
     *
     * @return is enable log
     */
    public boolean isLogEnabled() {
        return (mLevel & LEVEL_BASIC) != 0 && mLogger != null;
    }

    private boolean isPrint(int level) {
        if ((mLevel & level) == 0 || !isLogEnabled()) {
            return false;
        }
        return true;
    }

    //打印日志
    private void print(int level, String msg) {
        if (isPrint(level)) {
            print(msg);
        }
    }

    //打印日志
    private void print(String msg) {
        if (mLogger != null) {
            mLogger.print(TAG, msg);
        }
    }

    //合并输出
    private void print(List<HttpLogMessage> logs) {
        if (logs != null && !logs.isEmpty()) {
            StringBuffer sb = new StringBuffer(" \n");
            if (isPrint(LEVEL_BASIC)) sb.append("╔═══════════════════════════════════════════════════\n");
            for (HttpLogMessage log : logs) {
                if (isPrint(log.mLevel)) {
                    if ((sb.length() + log.mMsg.length()) >= 4000) {
                        print(sb.toString());
                        sb = new StringBuffer(" \n");
                    }
                    sb.append("║ ").append(log.mMsg).append("\n");
                }
            }
            if (isPrint(LEVEL_BASIC)) sb.append("╚═══════════════════════════════════════════════════\n");

            print(sb.toString());
        }
    }

    //拆分输出
    private void print2(List<HttpLogMessage> logs) {
        if (logs != null && !logs.isEmpty()) {
            print(LEVEL_BASIC, "╔═══════════════════════════════════════════════════");
            for (HttpLogMessage log : logs) {
                print(log.mLevel, "║ " + log.mMsg);
            }
            print(LEVEL_BASIC, "╚═══════════════════════════════════════════════════");
        }
    }

    @NotNull
    public Response intercept(@NotNull Chain chain) throws IOException {
        //如果不显示日志，则直接跳过
        if (!isLogEnabled()) {
            return chain.proceed(chain.request());
        }

        Request request = chain.request();

        print(LEVEL_BASIC, MessageFormat.format("--> {0} Start {1}", request.method(), request.url()));

        List<HttpLogMessage> logs = new ArrayList<>();
        mUtils.printRequest(logs, chain);

        long startNs = System.nanoTime(); //请求的开始时间
        Response mResponse;
        try {
            mResponse = chain.proceed(request);
            mResponse = mUtils.printResponse(logs, chain, mResponse, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs));
        } catch (Throwable e) {
            mUtils.printError(logs, chain, e, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs));
            throw e;
        } finally {
            print(logs);
        }

        return mResponse;
    }

    private final Utils mUtils = new Utils();

    private class Utils {
        private final Charset UTF8 = StandardCharsets.UTF_8;

        //日志装载：请求
        private void printRequest(List<HttpLogMessage> logs, Chain chain) {
            Request request = chain.request();
            Headers headers = request.headers();
            RequestBody requestBody = request.body();

            logs.add(new HttpLogMessage(LEVEL_BASIC, MessageFormat.format("{0}  {1}  {2}", request.method(), getProtocol(chain), getUrl(request))));

            if ((mLevel & LEVEL_HEADERS) != 0) {
                for (int i = 0, size = headers.size(); i < size; i++) {
                    logs.add(new HttpLogMessage(LEVEL_HEADERS, MessageFormat.format("{0}: {1}", headers.name(i), headers.value(i))));
                }
            }
            if ((mLevel & LEVEL_BODY) != 0) {
                try {
                    if (requestBody != null) {
                        Request build = request.newBuilder().build();
                        RequestBody buildBody = build.body();

                        String data;
                        Buffer buffer = new Buffer();
                        Charset mCharset = null;

                        if (buildBody != null) {
                            buildBody.writeTo(buffer);
                            MediaType mediaType = buildBody.contentType();
                            if (mediaType != null) {
                                mCharset = mediaType.charset(UTF8);
                            }
                        }
                        if (mCharset == null) mCharset = UTF8;
                        data = buffer.readString(mCharset);

                        printBody(logs, data);
                    }
                } catch (Throwable e) {
                    logs.add(new HttpLogMessage(LEVEL_BASIC, MessageFormat.format("Body parser error >>> {0}", e)));
                    logs.add(new HttpLogMessage(LEVEL_BASIC, ""));
                }
            }
        }

        //日志装载：异常
        private void printError(List<HttpLogMessage> logs, Chain chain, Throwable e, long elapsed) {
            Request request = chain.request();
            logs.add(new HttpLogMessage(LEVEL_BASIC, MessageFormat.format("<END OF  {0} Request 耗时({1}ms)>", request.method(), elapsed)));
            logs.add(new HttpLogMessage(LEVEL_BASIC, ""));
            logs.add(new HttpLogMessage(LEVEL_BASIC, MessageFormat.format("Error >>> {0}", e)));
        }

        //日志装载：结果
        private Response printResponse(List<HttpLogMessage> logs, Chain chain, Response mResponse, long elapsed) {
            Request request = chain.request();
            Response clone = mResponse.newBuilder().build(); //克隆的请求结果
            Headers headers = clone.headers();
            ResponseBody body = clone.body();

            logs.add(new HttpLogMessage(LEVEL_BASIC, MessageFormat.format("<END OF {0} Request>", request.method())));
            logs.add(new HttpLogMessage(LEVEL_BASIC, ""));
            logs.add(new HttpLogMessage(LEVEL_BASIC, MessageFormat.format("Response Code={0} {1} 耗时({2,number,0}ms)", clone.code(), clone.message(), elapsed)));
            if ((mLevel & LEVEL_HEADERS) != 0) {
                for (int i = 0, size = headers.size(); i < size; i++) {
                    logs.add(new HttpLogMessage(LEVEL_HEADERS, MessageFormat.format("{0}: {1}", headers.name(i), headers.value(i))));
                }
            }
            if ((mLevel & LEVEL_BODY) != 0) {
                try {
                    if (body != null) {
                        if (isPlaintext(body.contentType())) {
                            String data = body.string();
                            printBody(logs, data);
                            mResponse = mResponse.newBuilder().body(ResponseBody.create(data, body.contentType())).build();
                        } else {
                            logs.add(new HttpLogMessage(LEVEL_BODY, "Body parser error >>> Body == null or Contents not printable"));
                        }
                    }
                } catch (Throwable e) {
                    logs.add(new HttpLogMessage(LEVEL_BASIC, MessageFormat.format("Body parser error >>> {0}", e)));
                }
            }
            return mResponse;
        }

        //日志装载：JsonBody
        private void printBody(List<HttpLogMessage> logs, String json) {
            if (json.length() <= 100) {
                //内容太短,没必要格式化
                logs.add(new HttpLogMessage(LEVEL_BODY, json));
                return;
            }

            String LINE_SEPARATOR = System.getProperty("line.separator");
            if (LINE_SEPARATOR == null) {
                LINE_SEPARATOR = "\n";
            }

            String message;
            try {
                if (json.startsWith("{")) {
                    message = new JSONObject(json).toString(4);
                } else if (json.startsWith("[")) {
                    message = new JSONArray(json).toString(4);
                } else {
                    message = json;
                }
            } catch (Throwable e) {
                message = json;
            }

            if (!TextUtils.isEmpty(message)) {
                String[] split = message.split(LINE_SEPARATOR);
                for (int i = 0; i < split.length; i++) {
                    logs.add(new HttpLogMessage(LEVEL_BODY, split[i]));
                    if (i > LIMIT_MAX_BODY_LINES) {
                        logs.add(new HttpLogMessage(LEVEL_BODY, "..."));
                        break;
                    }
                }
            }

        }

        //获得请求协议版本
        public Protocol getProtocol(Chain chain) {
            Connection connection = chain.connection();
            if (connection == null) {
                return Protocol.HTTP_1_1;
            }
            return connection.protocol();
        }

        public String getUrl(Request request) {
            return request.url().toString();
        }

        //判断是否为可打印的文本
        public boolean isPlaintext(MediaType mediaType) {
            /*if (mediaType == null) {
                return false;
            }
            String type = mediaType.type();
            if ("text".equals(type)) {
                return true;
            }
            String subtype = mediaType.subtype();
            subtype = subtype.toLowerCase();
            return subtype.contains("x-www-form-urlencoded") ||
                    subtype.contains("json") ||
                    subtype.contains("xml") ||
                    subtype.contains("html");*/
            return true;
        }
    }
}
