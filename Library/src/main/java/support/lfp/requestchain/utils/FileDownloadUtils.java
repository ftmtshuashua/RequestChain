package support.lfp.requestchain.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import support.lfp.requestchain.listener.OnProgressListener;

/**
 * <pre>
 * Tip:
 *      文件下载工具
 *      More:https://www.jianshu.com/p/1043b8998ac3
 *
 * Function:
 *
 * Created by LiFuPing on 2019/6/27 19:24
 * </pre>
 */
public class FileDownloadUtils {

    /**
     * 下载文件到本地
     *
     * @param body     下载内容
     * @param dirPath  保存路径
     * @param fileName 保存文件名称
     * @param listener 下载进度监听
     * @return true:保存成功 | false:保存失败
     */
    public static boolean writeResponseBodyToDisk(ResponseBody body, String dirPath, String fileName, OnProgressListener listener) {
        try {
            //判断文件夹是否存在
            File files = new File(dirPath);//跟目录一个文件夹
            if (!files.exists()) files.mkdirs();
            File futureStudioIconFile = new File(dirPath + fileName);
            InputStream inputStream = null;  //初始化输入流
            OutputStream outputStream = null;  //初始化输出流
            try {

                byte[] fileReader = new byte[4096];  //设置每次读写的字节
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                if (listener != null) listener.progress(false, fileSizeDownloaded, fileSize);
                inputStream = body.byteStream();    //请求返回的字节流
                outputStream = new FileOutputStream(futureStudioIconFile); //创建输出流
                while (true) {   //进行读取操作
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    //进行写入操作
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;

                    if (listener != null) listener.progress(false, fileSizeDownloaded, fileSize);
                }
                //刷新
                outputStream.flush();

                if (listener != null) listener.progress(true, fileSize, fileSize);
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) inputStream.close();  //关闭输入流
                if (outputStream != null) outputStream.close();    //关闭输出流
            }
        } catch (IOException e) {
            return false;
        }
    }

}
