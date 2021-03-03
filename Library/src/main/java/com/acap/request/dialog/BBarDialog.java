package com.acap.request.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.acap.request.R;

/**
 * <pre>
 * Tip:
 *      进度Dialog基类
 *
 * Function:
 *
 * Created by LiFuPing on 2019/7/15 17:54
 * </pre>
 */
public class BBarDialog extends Dialog implements LifecycleObserver {

    public BBarDialog(Context context) {
        super(context, R.style.BBarDialogStyle);

        if (context instanceof LifecycleOwner) { /*生命周期观察*/
            ((LifecycleOwner) context).getLifecycle().addObserver(this);
        }

        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        if (isShowing()) {
            cancel();
        }
    }
}
