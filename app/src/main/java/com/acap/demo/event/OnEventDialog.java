package com.acap.demo.event;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import com.acap.ec.Event;
import com.acap.ec.listener.OnEventListener;

/**
 * <pre>
 * Tip:
 *
 * @author A·Cap at 2021/11/19 18:19
 * </pre>
 */
public class OnEventDialog<P, R> implements OnEventListener<P, R> {
    private Dialog mDialog;

    public OnEventDialog(Context context) {
        mDialog = new AlertDialog.Builder(context)
                .setMessage("事件正在执行..")
                .setCancelable(false)
                .create();
    }

    @Override
    public void onStart(Event<P, R> event, P params) {
        mDialog.show();
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(R result) {

    }

    @Override
    public void onComplete() {
        mDialog.dismiss();
    }
}
