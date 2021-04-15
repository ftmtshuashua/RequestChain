package com.acap.rc.processor;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * <pre>
 * Tip:
 *      注解API的生成器
 *
 * Created by ACap on 2021/4/15 16:40
 * </pre>
 */

@SupportedAnnotationTypes("com.acap.rc.annotation.Api")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ApiProcessor extends AbstractProcessor {

    private Elements mElements;
    private Messager mMessager;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElements = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {


        return true;
    }


}
