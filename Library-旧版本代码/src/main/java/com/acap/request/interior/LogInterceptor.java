package com.acap.request.interior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;

public class LogInterceptor implements Interceptor {

    public static class OkLogger {
        static void e(Exception e) {
            if (e == null) return;
            e.printStackTrace();
        }
    }

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private volatile LogInterceptor.Level level = LogInterceptor.Level.NONE;
    private Logger logger;

    public enum Level {
        NONE,       //不打印log
        BASIC,      //只打印 请求首行 和 响应首行
        HEADERS,    //打印请求和响应的所有 Header
        BODY        //所有数据全部打印
    }

    private void log(String message) {
        logger.log(java.util.logging.Level.INFO, message);
    }

    public LogInterceptor(String tag) {
        logger = Logger.getLogger(tag);
    }

    public void setLevel(LogInterceptor.Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (level == LogInterceptor.Level.NONE) {
            return chain.proceed(request);
        }

        //打印请求日志
        String requestMessage = "   --> " + request.method() + " Start " + request.url();
        log(requestMessage);

        //执行请求，计算请求时间
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            log("<-- HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        //响应日志拦截
        return logForResponse(request, response, chain.connection(), tookMs);
    }

    private Response logForResponse(Request request, Response response, Connection connection, long tookMs) {
        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();
        ResponseBody responseBody = clone.body();
        boolean logBody = (level == LogInterceptor.Level.BODY);
        boolean logHeaders = (level == LogInterceptor.Level.BODY || level == LogInterceptor.Level.HEADERS);
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        try {
            log("╔═══════════════════════════════════════════════════");
            String requestMsg = request.method() + ' ' + protocol + ' ' + removeToken(request);
            log("║ " + requestMsg);

            if (logHeaders) {
                Headers headers = request.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
                    log("║ " + headers.name(i) + ": " + headers.value(i));
                }

                if (logBody && hasRequestBody) {
                    bodyToString(request);
                }
            }
            log("║ END OF  ===》》》" + request.method() + " Request ");
            log("║ ");
            log("║ Response Code=" + clone.code() + ' ' + clone.message() + " 耗时(" + tookMs + "ms)");
            if (logHeaders) {
                Headers headers = clone.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
                    log("║ " + headers.name(i) + ": " + headers.value(i));
                }
                if (responseBody != null && logBody && HttpHeaders.hasBody(clone)) {
                    if (isPlaintext(responseBody.contentType())) {
                        String body = responseBody.string();
                        printJson(body);
                        responseBody = ResponseBody.create(responseBody.contentType(), body);
                        return response.newBuilder().body(responseBody).build();
                    } else {
                        log("║ LogInterceptor: maybe responseBody==null , or contentType isn't json type");
                    }
                }
            }
        } catch (Exception e) {
            LogInterceptor.OkLogger.e(e);
        } finally {
            log("╚═══════════════════════════════════════════════════");
        }
        return response;
    }

    private String removeToken(Request request) {
        String url = request.url().toString();
        int index = url.indexOf("access_token");
        if (index != -1)
            return url.substring(0, index);
        return url;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private boolean isPlaintext(MediaType mediaType) {
        if (mediaType == null) return false;
        if (mediaType.type() != null && mediaType.type().equals("text")) {
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

    private void bodyToString(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = copy.body().contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            printJson(buffer.readString(charset));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final String LINE_SEPARATOR = System.getProperty("line.separator");

    private void printJson(String msg) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                //最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
                message = jsonObject.toString(4);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        //message = headString + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            log("║ " + line);
        }
    }

}