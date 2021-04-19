package com.acap.rc.processor;

import java.io.Writer;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

/**
 * <pre>
 * Tip:
 *      处理器工具
 *
 * Created by ACap on 2021/4/15 16:51
 * </pre>
 */
class ProcessorUtils {

    /**
     * 写入代码到文件中
     *
     * @param mFiler 环境
     * @param name   类的全量名称
     * @param java   类中包含的代码
     * @throws Exception
     */
    public static final void writer(Filer mFiler, String name, String java) throws Exception {
        JavaFileObject source = mFiler.createSourceFile(name);
        Writer writer = source.openWriter();
        writer.write(java);
        writer.flush();
        writer.close();
    }
}
