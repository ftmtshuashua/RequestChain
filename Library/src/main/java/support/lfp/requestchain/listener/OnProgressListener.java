package support.lfp.requestchain.listener;

/**
 * <pre>
 * Tip:
 *      进度监听器
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/26 16:24
 * </pre>
 */
@FunctionalInterface
public interface OnProgressListener {
    /**
     * 当进度发生变化的时候回调该方法
     *
     * @param total    总进度
     * @param progress 当前进度
     */
    void progress(long total, long progress);
}
