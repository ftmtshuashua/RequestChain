package com.acap.rc.processor;

import com.acap.rc.annotation.Api;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
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

//@AutoService(Processor.class)
@SupportedAnnotationTypes("com.acap.rc.annotation.Api")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ApiProcessor extends AbstractProcessor {
    private static final List<String> objectMethod = Arrays.asList("getClass", "hashCode", "equals", "toString", "notify", "notifyAll", "wait");
    private static final String REQUEST_CLASS_TYPE = "com.acap.rc.adapter.Request";


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

    private TypeMirror getTypeMirror(Runnable runnable) {
        try {
            runnable.run();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null;
    }

    //获得泛型的类型
    private String getGenericType(String classtype) {

        Matcher matcher = Pattern.compile("<(.*?)>$").matcher(classtype);
        boolean isHas = matcher.find();
        if (isHas) {
            String group = matcher.group(0);
            String substring = group.substring(1, group.length() - 1);
            return substring;
        }
        return classtype;
    }

    //获得方法的参数
    private List<ApiClassModel.MethodParams> getMethodParams(ExecutableElement method) {
        List<ApiClassModel.MethodParams> array = new ArrayList<>();

        List<ParameterSpec> params = Utils.getParams(method.getParameters());
        for (ParameterSpec param : params) {
            array.add(new ApiClassModel.MethodParams(param.type.toString(), param.name));
        }
        return array;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            for (Element element : roundEnvironment.getElementsAnnotatedWith(Api.class)) {
                final Api annotation = element.getAnnotation(Api.class);
                String apiclassname = element.getSimpleName().toString();
                PackageElement classPackageName = mElements.getPackageOf(element);
                String packagName = classPackageName.getQualifiedName().toString();
                List<ExecutableElement> methods = ElementFilter.methodsIn(mElements.getAllMembers((TypeElement) element));


                ApiClassModel mModel = new ApiClassModel();
                mModel.setPackage(packagName);
                mModel.setApiClass(apiclassname);
                mModel.setUrl(annotation.url());
                mModel.setOkHttpConfigClass(getTypeMirror(() -> annotation.okhttpConfig()).toString());
                mModel.setRetrofitConfigClass(getTypeMirror(() -> annotation.retrofitConfig()).toString());

                for (ExecutableElement method : methods) {
                    String simpleMethodName = method.getSimpleName().toString();
                    if (objectMethod.contains(simpleMethodName)) continue;

                    String returnType = method.getReturnType().toString(); //com.acap.rc.adapter.Request<com.acap.rc.bean.BeanData1>
                    ApiClassModel.Method mMethod;
                    if (returnType.startsWith(REQUEST_CLASS_TYPE)) {
                        mMethod = new ApiClassModel.RequestMethod(simpleMethodName, getGenericType(returnType));
                    } else {
                        mMethod = new ApiClassModel.OtherMethod(simpleMethodName, returnType);
                    }

                    List<ApiClassModel.MethodParams> params = getMethodParams(method);
                    for (ApiClassModel.MethodParams param : params) {
                        mMethod.addParams(param);
                    }
                    mModel.addMethod(mMethod);
                }
                ProcessorUtils.writer(mFiler, ApiClassModel.getClassAllName(packagName, apiclassname), mModel.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    private void processApi(Element element) throws IOException {
        String name_cls = Utils.getName(element) + "Service";
        String name_pkg = Utils.getPackageName(mElements, element);

        TypeName type = Utils.getType(element);

        //class
        TypeSpec.Builder builder = TypeSpec.classBuilder(name_cls);
        builder.addModifiers(Modifier.FINAL, Modifier.PUBLIC);
        builder.addJavadoc("proxy {@link $L}", type);

        //Field
        builder.addField(FieldSpec.builder(type, "mApi", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
//                .initializer("$L",ServiceGenerator.)
                .build());


        //Java file
        JavaFile.Builder builder_java = JavaFile.builder(name_pkg, builder.build());

        print("---------------------------------------------");
        print(builder_java.build().toString());
        print("---------------------------------------------");

        builder_java.build().writeTo(mFiler);
    }


}
