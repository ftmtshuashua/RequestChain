package com.acap.request.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import support.lfp.requestchain.R;

/**
 * <pre>
 * Tip:
 *          带进度的Dialog
 *
 * Function:
 *
 * Created by LiFuPing on 2019/7/15 18:05
 * </pre>
 */
public class WaitDialog extends BBarDialog {


    private final Handler mHandler = new Handler(Looper.getMainLooper());

    long mDelayTime = 0; //延时显示的时间 单位：ms
    boolean mDismiss = false;

    public WaitDialog(Context context) {
        super(context);
        setContentView(R.layout.layout_waitdialog);
    }

    /**
     * 设置Dialog延迟显示 ,当设置延迟时间之后，调用show()方法将不会直接显示Dialog，它会等待延时时间之后再显示Dialog.调用dismiss()可清楚延时启动事件
     *
     * @param delay 延迟的时间
     */
    public void setDelayShow(long delay) {
        mDelayTime = delay;
    }

    @Override
    public void show() {
        mDismiss = false;
        if (mDelayTime <= 0) {
            super.show();
        } else {
            mHandler.postDelayed(() -> {
                if (!mDismiss) super.show();
            }, mDelayTime);
        }
    }

    @Override
    public void dismiss() {
        mDismiss = true;
        super.dismiss();
    }

}
