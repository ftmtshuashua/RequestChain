package com.acap.rc.processor;


import com.acap.rc.annotation.ApiDynamicUrl;
import com.acap.rc.annotation.ApiOkHttpConfig;
import com.acap.rc.annotation.ApiRetrofitConfig;
import com.acap.rc.annotation.ApiUrl;
import com.acap.rc.annotation.service.DefaultOkHttpConfig;
import com.acap.rc.annotation.service.DefaultRetrofitConfig;
import com.acap.rc.service.ServiceGenerator;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * <pre>
 * Tip:
 *      注解API的生成器
 *
 * Created by ACap on 2021/4/15 16:40
 * </pre>
 */

@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ApiProcessor extends AbstractProcessor {
    private static final List<String> mObjectMethod = Arrays.asList("getClass", "hashCode", "equals", "toString", "notify", "notifyAll", "wait");
    private static final String REQUEST_CLASS_TYPE = "com.acap.rc.adapter.Request";

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(ApiUrl.class.getName());
        set.add(ApiDynamicUrl.class.getName());
        return set;
    }

    private Elements mElements;
    private Messager mMessager;
    private Filer mFiler;

    private void print(CharSequence msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElements = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment environment) {
        try {
            ApiUrl(environment);
            ApiDynamicUrl(environment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private final String mApiFieldName = "mApi";

    /* 获得 OkHttp 配置 */
    private Object getOKConfig(Element element) {
        ApiOkHttpConfig a = element.getAnnotation(ApiOkHttpConfig.class);
        if (a != null) {
            return Utils.getTypeMirror(() -> a.value());
        }
        return DefaultOkHttpConfig.class;
    }

    /* 获得 Retrofit 配置 */
    private Object getRtConfig(Element element) {
        ApiRetrofitConfig a = element.getAnnotation(ApiRetrofitConfig.class);
        if (a != null) {
            return Utils.getTypeMirror(() -> a.value());
        }
        return DefaultRetrofitConfig.class;
    }

    /* 获得生成类的名称 */
    private String getGenerateClassName(Element element) {
        return String.format("%sService", Utils.getName(element));
    }

    private void ApiUrl(RoundEnvironment environment) throws Exception {
        for (Element element : environment.getElementsAnnotatedWith(ApiUrl.class)) {
            print("ApiUrl:" + Utils.getName(element));

            TypeSpec.Builder cls = TypeSpec.classBuilder(getGenerateClassName(element));
            cls.addModifiers(Modifier.FINAL, Modifier.PUBLIC);
            cls.addField(FieldSpec.builder(Utils.getType(element), mApiFieldName, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$T.generator($T.class,$S,$T.class,$T.class)", ServiceGenerator.class, Utils.getType(element), element.getAnnotation(ApiUrl.class).value(), getOKConfig(element), getRtConfig(element))
                    .build());
            cls.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build());

            List<ExecutableElement> methods = Utils.getMethods(mElements, element);
            for (ExecutableElement method : methods) {
                String methodName = method.getSimpleName().toString();
                if (mObjectMethod.contains(methodName)) continue;


                MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
                builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
                builder.returns(Utils.getType(method.getReturnType()));

                StringBuilder parameters = new StringBuilder();
                for (VariableElement parameter : method.getParameters()) {
                    String argsName = parameter.getSimpleName().toString();
                    builder.addParameter(Utils.getType(parameter.asType()), argsName);
                    parameters.append(",").append(argsName);
                }
                if (parameters.length() > 0) parameters.deleteCharAt(0);
                builder.addStatement("return $L.$L($L)", mApiFieldName, methodName, parameters.toString());

                cls.addMethod(builder.build());
            }

            JavaFile.Builder builder_java = JavaFile.builder(Utils.getPackageName(mElements, element), cls.build());
            print("---------------------------------------------");
            print(builder_java.build().toString());
            print("---------------------------------------------");
            builder_java.build().writeTo(mFiler);
        }
    }

    private void ApiDynamicUrl(RoundEnvironment environment) throws Exception {
        for (Element element : environment.getElementsAnnotatedWith(ApiDynamicUrl.class)) {
            print("ApiDynamicUrl:" + Utils.getName(element));

        }

    }

}
