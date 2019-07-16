package support.lfp.requestchain.simple;

import android.app.Dialog;
import android.content.Context;

import support.lfp.requestchain.dialog.WaitDialog;

/**
 * <pre>
 * Tip:
 *       在事件开始之后100毫秒之内没有结束将显示一个进度Dialog用于显示事件的进度，
 *       当事件完成之后隐藏这个进度Dialog
 *
 * Function:
 *
 * Created by LiFuPing on 2019/7/15 17:44
 * </pre>
 */
public class OnEventDelayWaitBar<T> extends OnEventWaitBar<T> {
    long mDelayTime = 100; //ms

    public OnEventDelayWaitBar(Context context) {
        super(context);
    }

    /**
     * 设置这个Dialog延时显示的时间
     *
     * @param delay Dialog 将在delay毫秒之后显示(如果这个事件没有结束的话)
     * @return context
     */
    public OnEventDelayWaitBar<T> setDelayTime(long delay) {
        mDelayTime = delay;
        return this;
    }

    @Override
    public OnEventDelayWaitBar<T> setShowThrowable(boolean isShowThrowable) {
        super.setShowThrowable(isShowThrowable);
        return this;
    }

    @Override
    protected Dialog onGenerateDialog(Context context) {
        WaitDialog dialog = new WaitDialog(context);
        dialog.setDelayShow(mDelayTime);
        return dialog;
    }
}
