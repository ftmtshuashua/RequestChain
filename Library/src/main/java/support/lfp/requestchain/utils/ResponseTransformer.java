package support.lfp.requestchain.utils;


import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.lfp.eventtree.excption.MultiException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.MessageFormat;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import support.lfp.requestchain.exception.RequestException;

/**
 * <pre>
 * Tip:
 *      请求异常处理,发生异常的时候
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/17 16:39
 * </pre>
 */
public class ResponseTransformer {


    /**
     * 用于请求异常的统一处理,所有异常都会被转换为RequestEvent
     *
     * @param <T>
     * @return
     */
    public static final <T> ObservableTransformer<T, T> handle() {
        return upstream -> upstream
                //本地异常处理 - 通过 CustomExceptionUtils 将异常转换成 ApiException
                .onErrorResumeNext(throwable -> {
                    return Observable.error(transformerException(throwable));
                })
                .flatMap((Function<T, ObservableSource<T>>) result -> {
                    if (result instanceof IResponseException) {
                        IResponseException response = (IResponseException) result;
                        if (response.isSucceed()) {
                            return Observable.just(result);
                        } else {
                            return Observable.error(new RequestException(response.getMsg(), response.getCode()));
                        }
                    }
                    return Observable.just(result);
                });
    }


    /**
     * 将任何异常转换为 RequestException
     *
     * @param thr
     * @return
     */
    public static final RequestException transformerException(Throwable thr) {
        if (thr == null) return new RequestException("未知异常!", RequestException.NULL_CODE);
        if (thr instanceof RequestException) return (RequestException) thr;

        if (thr instanceof MultiException) { /*并发事件抛出多个异常*/
            MultiException e = (MultiException) thr;
            int size = e.size();
            if (size == 0) {
                return new RequestException(e.getMessage(), e, RequestException.NULL_CODE);
            } else if (size == 1) {
                return transformerException(e.getFirst());
            } else { /*多个异常*/

            }
        } else if (thr instanceof retrofit2.HttpException) { /*网络错误*/
            retrofit2.HttpException exc = (retrofit2.HttpException) thr;
            final HttpCodeMsg byCode = HttpCodeMsg.findByCode(exc.code());
            if (byCode == HttpCodeMsg.UNKNOWN) {
                return new RequestException(MessageFormat.format("请求错误码:[{0}]", exc.code()), exc, exc.code());
            } else {
                return new RequestException(byCode.msg, exc, exc.code());
            }
        } else if (thr instanceof JsonParseException || thr instanceof JSONException || thr instanceof ParseException) {
            return new RequestException("数据解析失败!", thr, RequestException.NULL_CODE);
        } else if (thr instanceof ConnectException) {
            return new RequestException("无法连接服务器!", thr, RequestException.NULL_CODE);
        } else if (thr instanceof UnknownHostException || thr instanceof SocketTimeoutException || thr instanceof SocketException) {
            return new RequestException("网络异常!", thr, RequestException.NULL_CODE);
        }

        return new RequestException(thr, RequestException.NULL_CODE);
    }


    /**
     * 用于处理自定义服务器回复异常。RequestChain有能力处理网络相关异常情况，但是对于服务器返回错误码是无能为力的.
     * 该类专门处理这种情况，让获取服务器数据的Bean实现该接口，将获得友好且统一的异常处理体验
     */
    public interface IResponseException {
        /**
         * 判断请求是否成功
         *
         * @return true:请求成功   false:请求失败
         */
        boolean isSucceed();


        /**
         * 获得回复编码
         */
        int getCode();

        /**
         * 获得请求信息
         */
        String getMsg();

    }

    /**
     * Http code 信息
     */
    public enum HttpCodeMsg {
        _400(400, "请求语法错误!"),
        _401(401, "授权失败!"),
        _403(403, "请求被服务器拒绝!"),
        _406(406, "请求被服务器拒绝!"),
        _404(404, "请求地址未找到!"),
        _405(405, "请求方法被禁用!"),
        _414(414, "请求的URI过长,服务器无法处理!"),
        _500(500, "服务器内部错误!"),
        _502(502, "服务器网关错误!"),
        _503(503, "服务器无可用!"),
        _504(504, "服务器网关超时!"),
        _505(505, "服务器不支持当前HTTP版本!"),
        /*特殊*/
        _429(429, "网络拥挤,请求失败!"),
        _431(431, "请求头字段太大!"),
        _511(511, "当前网络需要进行认证!"),
        UNKNOWN(-1, "");
        int code;
        String msg;

        HttpCodeMsg(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public static final HttpCodeMsg findByCode(int code) {
            final HttpCodeMsg[] values = values();
            for (HttpCodeMsg item : values) {
                if (item.code == code) return item;
            }
            return UNKNOWN;
        }
    }
}
