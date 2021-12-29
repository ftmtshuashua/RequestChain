package com.acap.rc.processor;


import com.acap.rc.annotation.ApiOkHttpConfig;
import com.acap.rc.annotation.ApiRetrofitConfig;
import com.acap.rc.annotation.ApiUrl;
import com.acap.rc.annotation.ApiVariableUrl;
import com.acap.rc.annotation.service.DefaultOkHttpConfig;
import com.acap.rc.annotation.service.DefaultRetrofitConfig;
import com.acap.rc.annotation.service.SimpleVariableUrl;
import com.acap.rc.annotation.service.VariableUrl;
import com.acap.rc.service.ServiceCreator;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sun.source.doctree.DocCommentTree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.api.JavacTrees;

import java.lang.reflect.Field;
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
import javax.lang.model.type.TypeMirror;
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
    private static final boolean LOGS_DEBUG = false;
    private static final List<String> mObjectMethod = Arrays.asList("getClass", "hashCode", "equals", "toString", "notify", "notifyAll", "wait");

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(ApiUrl.class.getName());
        set.add(ApiVariableUrl.class.getName());
        return set;
    }

    private Elements mElements;
    private Messager mMessager;
    private Filer mFiler;
    private JavacTrees mJavacTrees;

    private void print(CharSequence msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElements = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();

        try {
            Field delegate = processingEnvironment.getClass().getDeclaredField("delegate");
            delegate.setAccessible(true);
            Object o = delegate.get(processingEnvironment);
            mJavacTrees = JavacTrees.instance((ProcessingEnvironment) o);
        } catch (Exception e) {
        }
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

    private final String mUrlFieldName = "mUrl";
    private final String mServiceMethodName = "get";

    private TypeSpec.Builder getClassBuilder(Element element) {
        TypeSpec.Builder cls = TypeSpec.classBuilder(getGenerateClassName(element));
        cls.addModifiers(Modifier.FINAL, Modifier.PUBLIC);
        cls.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build());
        cls.addMethod(MethodSpec.methodBuilder(mServiceMethodName)
                .returns(Utils.getType(element))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("return $T.getAndCreate($T.class, $L, $T.class, $T.class)", ServiceCreator.class, Utils.getType(element), mUrlFieldName, getOKConfig(element), getRtConfig(element))
                .build());

        List<ExecutableElement> methods = Utils.getMethods(mElements, element);
        for (ExecutableElement method : methods) {
            String methodName = method.getSimpleName().toString();
            if (mObjectMethod.contains(methodName)) {
                continue;
            }
            MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
            builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            builder.returns(Utils.getType(method.getReturnType()));

            //args
            StringBuilder parameters = new StringBuilder();
            for (VariableElement parameter : method.getParameters()) {
                String argsName = parameter.getSimpleName().toString();
                builder.addParameter(Utils.getType(parameter.asType()), argsName);
                parameters.append(",").append(argsName);
            }
            if (parameters.length() > 0) {
                parameters.deleteCharAt(0);
            }
            builder.addStatement("return $L().$L($L)", mServiceMethodName, methodName, parameters.toString());

            //doc
            if (mJavacTrees != null) {
                TreePath path = mJavacTrees.getPath((Element) method);
                if (path != null) {
                    DocCommentTree docCommentTree = mJavacTrees.getDocCommentTree(path);
                    if (docCommentTree != null) {
                        String doc = Utils.unicode2String(docCommentTree.toString());
                        if (doc != null) {
                            builder.addJavadoc(doc);
                        }
                    }
                }
            }
            cls.addMethod(builder.build());
        }
        return cls;
    }

    private void ApiUrl(RoundEnvironment environment) throws Exception {
        for (Element element : environment.getElementsAnnotatedWith(ApiUrl.class)) {
            if (null != element.getAnnotation(ApiVariableUrl.class)) {
                throw new Exception(String.format("服务类 %s 中只允许配置 @ApiUrl 或 @ApiVariableUrl 其中一个!", Utils.getFullName(element)));
            }
            print(String.format("@ApiUrl -> %s", Utils.getFullName(element)));

            TypeSpec.Builder cls = getClassBuilder(element);
            cls.addField(FieldSpec.builder(VariableUrl.class, mUrlFieldName, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).initializer("new $T($S)", SimpleVariableUrl.class, element.getAnnotation(ApiUrl.class).value()).build());

            JavaFile.Builder builder = JavaFile.builder(Utils.getPackageName(mElements, element), cls.build());
            if (LOGS_DEBUG) {
                print("---------------------------------------------");
                print(builder.build().toString());
                print("---------------------------------------------");
            }
            builder.build().writeTo(mFiler);
        }
    }

    private void ApiDynamicUrl(RoundEnvironment environment) throws Exception {
        for (Element element : environment.getElementsAnnotatedWith(ApiVariableUrl.class)) {
            if (null != element.getAnnotation(ApiUrl.class)) {
                throw new Exception(String.format("服务类 %s 中只允许配置 @ApiUrl 或 @ApiVariableUrl 其中一个!", Utils.getFullName(element)));
            }
            print(String.format("@ApiVariableUrl -> %s", Utils.getFullName(element)));

            TypeSpec.Builder cls = getClassBuilder(element);
            TypeMirror typeMirror = Utils.getTypeMirror(() -> element.getAnnotation(ApiVariableUrl.class).value());
            cls.addField(FieldSpec.builder(VariableUrl.class, mUrlFieldName, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).initializer("new $T()", typeMirror).build());

            JavaFile.Builder builder = JavaFile.builder(Utils.getPackageName(mElements, element), cls.build());
            if (LOGS_DEBUG) {
                print("---------------------------------------------");
                print(builder.build().toString());
                print("---------------------------------------------");
            }
            builder.build().writeTo(mFiler);
        }

    }


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

}
