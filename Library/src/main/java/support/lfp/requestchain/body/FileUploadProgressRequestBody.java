package support.lfp.requestchain.body;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import support.lfp.requestchain.RequestChainConfig;
import support.lfp.requestchain.listener.OnProgressListener;

/**
 * <pre>
 * Tip:
 *      可观测进度的文件上传Body
 *
 *      A example:
 *          FileUploadProgressRequestBody body = new FileUploadProgressRequestBody(file,  MediaType.parse("multipart/form-data"));
 *          MultipartBody.Part bodyPart = MultipartBody.Part.createFormData("file", file.getName(), body);
 *
 *
 *       More:https://www.jianshu.com/p/1043b8998ac3
 *
 * Function:
 *
 * Created by LiFuPing on 2019/6/27 18:46
 * </pre>
 */
public class FileUploadProgressRequestBody extends RequestBody {
    public static final int SEGMENT_SIZE = 4 * 1024;

    File file;
    OnProgressListener listener;
    MediaType contentType;
    long length; //文件长度

    public FileUploadProgressRequestBody(File file, MediaType contentType) {
        this.file = file;
        this.contentType = contentType;
        this.length = file.length();
    }

    public void setOnProgressListener(OnProgressListener l) {
        listener = l;
    }

    @Override
    public long contentLength() {
        return length;
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(file);
            long total = contentLength();
            long process = 0;
            long read;

            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                process += read;
                sink.flush();
                if (RequestChainConfig.isDebug()) {
                    String msg = MessageFormat.format("上传进度:{0,number,0}/{1,number,0} {2,number,0,00}%", process, total, (process / (float) total) * 10000);
                    RequestChainConfig.getLogger().i(msg);
                }

                if (this.listener != null) this.listener.progress(false, total, process);
            }
            if (this.listener != null) this.listener.progress(true, total, total);
        } finally {
            try {
                source.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
