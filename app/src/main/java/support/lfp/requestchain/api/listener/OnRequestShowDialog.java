package support.lfp.requestchain.api.listener;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.lfp.eventtree.OnEventListener;

/**
 * <pre>
 * Tip:
 *
 * Function:
 *
 * Created by LiFuPing on 2019/6/27 15:58
 * </pre>
 */
public class OnRequestShowDialog implements OnEventListener {

    ProgressDialog dialog;
    Context mContext;

    public OnRequestShowDialog(Context c) {
        this.dialog = new ProgressDialog(c);
        mContext = c;
    }

    @Override
    public void onStart() {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onComplete() {
        if (dialog.isShowing()) dialog.dismiss();
    }
}
