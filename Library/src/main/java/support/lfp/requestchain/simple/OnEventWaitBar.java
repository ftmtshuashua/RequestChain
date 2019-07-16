package support.lfp.requestchain.simple;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.lfp.eventtree.OnEventListener;

import support.lfp.requestchain.dialog.WaitDialog;

/**
 * <pre>
 * Tip:
 *       在事件开始之前显示一个进度Dialog用于显示事件的进度，
 *       当事件完成之后隐藏这个进度Dialog
 *
 *
 * Function:
 *
 * Created by LiFuPing on 2019/7/15 17:24
 * </pre>
 */
public class OnEventWaitBar<T> implements OnEventListener {

    Dialog mDialog;
    boolean mShowThrowable;
    Context mContext;

    public OnEventWaitBar(Context context) {
        mContext = context;
    }

    @Override
    public void onStart() {
        if (mDialog == null) mDialog = onGenerateDialog(mContext);
        if (mDialog != null) mDialog.show();
    }

    @Override
    public void onError(Throwable e) {
        if (mShowThrowable) onShowThrowable(mContext, e);
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onComplete() {
        if (mDialog != null) mDialog.dismiss();
    }


    /**
     * 设置是偶显示错误信息,当事件在执行过程中出现错误,可以让让用户收到错误提示信息
     * 重写方法 {@link OnEventWaitBar#onShowThrowable(Context, Throwable)}可以自定义异常处理方法
     *
     * @param isShowThrowable True or False
     * @return Context
     */
    public OnEventWaitBar<T> setShowThrowable(boolean isShowThrowable) {
        mShowThrowable = isShowThrowable;
        return this;
    }


    /**
     * 当事件抛出异常信息的时候接收消息的回调
     *
     * @param context A context
     * @param e       异常信息
     */
    public void onShowThrowable(Context context, Throwable e) {
        if (e != null) Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
    }


    /**
     * 生成进度Dialog
     *
     * @return The dialog
     */
    protected Dialog onGenerateDialog(Context context) {
        return new WaitDialog(context);
    }

}
