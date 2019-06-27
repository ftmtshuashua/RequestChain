package support.lfp.requestchain.interior;

import android.util.Log;

/**
 * <pre>
 * Tip:
 *      简单日志处理
 *
 * Function:
 *
 * Created by LiFuPing on 2019/6/26 15:11
 * </pre>
 */
public class SimpleLogger extends Logger {
    public static final String TAG = "RequestChain";

    @Override
    public void i(String msg) {
        Log.i(TAG, msg);
    }

    @Override
    public void e(String msg) {
        Log.e(TAG, msg);
    }
}
