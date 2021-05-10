package com.acap.rc.tool;

import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
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


    private static final Charset UTF8 = Charset.forName("UTF-8");
    private String TAG;
    private int mLevel = LEVEL_HEADERS | LEVEL_BODY | LEVEL_BASIC;
    private int LIMIT_MAX_BODY_LINES = 30; //限制Body显示的最大行数

    private HttpLogger mLogger = (tag, msg) -> Log.i(tag, msg);

    public HttpLogInterceptor(String TAG) {
        this.TAG = TAG;
    }

    /**
     * 设置日志启用状态
     *
     * @param enable
     * @return
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
     * @param enable
     * @return
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
     * @param enable
     * @return
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
     * @param logger
     * @return
     */
    public HttpLogInterceptor setHttpLogger(HttpLogger logger) {
        mLogger = logger;
        return this;
    }

    /**
     * 判断是否启用日志
     *
     * @return
     */
    public boolean isLogEnabled() {
        return (mLevel & LEVEL_BASIC) != 0 && mLogger != null;
    }

    /**
     * 打印日志
     *
     * @param level 打印日志的等级
     * @param msg   日志信息
     */
    private void print(int level, String msg) {
        if ((mLevel & level) == 0 || !isLogEnabled()) return;
        if (mLogger != null) mLogger.print(TAG, msg);
    }

    //日志输出
    private void print(List<PrintLog> logs) {
        if (logs != null && !logs.isEmpty()) {
            print(LEVEL_BASIC, "╔═══════════════════════════════════════════════════");
            for (PrintLog log : logs) {
                print(log.mLevel, "║ " + log.mMsg);
            }
            print(LEVEL_BASIC, "╚═══════════════════════════════════════════════════");
        }
    }


    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        //如果不显示日志，则直接跳过
        if (!isLogEnabled()) {
            return chain.proceed(request);
        }


        print(LEVEL_BASIC, MessageFormat.format("--> {0} Start {1}", request.method(), request.url()));

        List<PrintLog> logs = new ArrayList<>();
        assemblyRequest(logs, chain);

        long startNs = System.nanoTime(); //请求的开始时间
        Response mResponse;
        try {
            mResponse = chain.proceed(request);
        } catch (Exception e) {
            assemblyError(logs, chain, e, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs));
            print(logs);
            throw e;
        }
        mResponse = assemblyResponse(logs, chain, mResponse, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs));
        print(logs);
        return mResponse;
    }

    //日志装载：请求
    private void assemblyRequest(List<PrintLog> logs, Chain chain) {
        Request request = chain.request();
        Headers headers = request.headers();
        RequestBody body = request.body();

        logs.add(new PrintLog(LEVEL_BASIC, MessageFormat.format("{0}  {1}  {2}", request.method(), Utils.getProtocol(chain), Utils.getUrl(request))));
        for (int i = 0, size = headers.size(); i < size; i++) {
            logs.add(new PrintLog(LEVEL_HEADERS, MessageFormat.format("{0}: {1}", headers.name(i), headers.value(i))));
        }

        try {
            if (body != null) {
                Request build = request.newBuilder().build();

                Buffer buffer = new Buffer();
                build.body().writeTo(buffer);
                MediaType mediaType = build.body().contentType();

                String data;
                if (mediaType != null) {
                    data = buffer.readString(mediaType.charset(UTF8));
                } else {
                    data = buffer.readString(UTF8);
                }
                assemblyBody(logs, data);
            }
        } catch (Exception e) {
            logs.add(new PrintLog(LEVEL_BASIC, MessageFormat.format("Exception: {0}", e)));
            logs.add(new PrintLog(LEVEL_BASIC, ""));
        }
    }

    //日志装载：异常
    private void assemblyError(List<PrintLog> logs, Chain chain, Exception e, long elapsed) {
        Request request = chain.request();
        logs.add(new PrintLog(LEVEL_BASIC, MessageFormat.format("END OF  --->>> {0} Request 耗时({1}ms)", request.method(), elapsed)));
        logs.add(new PrintLog(LEVEL_BASIC, ""));
        logs.add(new PrintLog(LEVEL_BASIC, MessageFormat.format("Exception: {0}", e)));
    }

    //日志装载：结果
    private Response assemblyResponse(List<PrintLog> logs, Chain chain, Response mResponse, long elapsed) {
        Request request = chain.request();
        Response clone = mResponse.newBuilder().build(); //克隆的请求结果
        Headers headers = clone.headers();
        ResponseBody body = clone.body();

        logs.add(new PrintLog(LEVEL_BASIC, MessageFormat.format("END OF  --->>> {0} Request", request.method())));
        logs.add(new PrintLog(LEVEL_BASIC, ""));
        logs.add(new PrintLog(LEVEL_BASIC, MessageFormat.format("Response Code={0} {1} 耗时({2,number,0}ms)", clone.code(), clone.message(), elapsed)));
        for (int i = 0, size = headers.size(); i < size; i++) {
            logs.add(new PrintLog(LEVEL_HEADERS, MessageFormat.format("{0}: {1}", headers.name(i), headers.value(i))));
        }

        try {
            if (body != null) {
                if (Utils.isPlaintext(body.contentType())) {
                    String data = body.string();
                    assemblyBody(logs, data);
                    mResponse = mResponse.newBuilder().body(ResponseBody.create(body.contentType(), data)).build();
                } else {
                    logs.add(new PrintLog(LEVEL_BODY, "Exception: Body == null 或者 内容不是Json类型"));
                }
            }
        } catch (Exception e) {
            logs.add(new PrintLog(LEVEL_BASIC, MessageFormat.format("Exception: {0}", e)));
        }
        return mResponse;
    }

    //日志装载：JsonBody
    private void assemblyBody(List<PrintLog> logs, String json) {
        String LINE_SEPARATOR = System.getProperty("line.separator");
        String message;
        try {
            if (json.startsWith("{")) {
                message = new JSONObject(json).toString(4);
            } else if (json.startsWith("[")) {
                message = new JSONArray(json).toString(4);
            } else {
                message = json;
            }
        } catch (Exception e) {
            message = json;
        }

        if (!TextUtils.isEmpty(message)) {
            String[] split = message.split(LINE_SEPARATOR);
            for (int i = 0; i < split.length; i++) {
                logs.add(new PrintLog(LEVEL_BODY, split[i]));
                if (i > LIMIT_MAX_BODY_LINES) {
                    logs.add(new PrintLog(LEVEL_BODY, "..."));
                    break;
                }
            }
        }

    }


    //输出的日志
    private static final class PrintLog {
        //日志等级
        private int mLevel;
        //日志消息
        private String mMsg;

        public PrintLog(int mLevel, String mMsg) {
            this.mLevel = mLevel;
            this.mMsg = mMsg;
        }
    }

    private static final class Utils {

        //获得请求协议版本
        public static final Protocol getProtocol(Chain chain) {
            Connection connection = chain.connection();
            if (connection == null) {
                return Protocol.HTTP_1_1;
            }
            return connection.protocol();
        }


        public static final String getUrl(Request request) {
            return request.url().toString();
        }

        //判断是否为可打印的文本
        public static final boolean isPlaintext(MediaType mediaType) {
            if (mediaType == null) return false;
            if (mediaType.type() != null && mediaType.type() == "text") {
                return true;
            }
            String subtype = mediaType.subtype();
            if (subtype != null) {
                subtype = subtype.toLowerCase();
                return subtype.contains("x-www-form-urlencoded") ||
                        subtype.contains("json") ||
                        subtype.contains("xml") ||
                        subtype.contains("html");
            }
            return false;
        }
    }
}
