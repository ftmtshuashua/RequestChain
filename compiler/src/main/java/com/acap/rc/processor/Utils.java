package com.acap.rc.processor;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/8/18 14:25
 * </pre>
 */
class Utils {
    /**
     * 属于Object,并且不能被操作的方法
     */
    public static final List<String> OBJECT_METHOD = Arrays.asList("getClass", "notify", "notifyAll", "wait", "finalize");

    /**
     * 获得元素的Class类型
     *
     * @param element
     * @return
     */
    public static TypeName getType(Element element) {
        return TypeName.get(element.asType());
    }

    public static TypeName getType(TypeMirror returnType) {
        return TypeName.get(returnType);
    }

    /**
     * 获得名称
     *
     * @param element
     * @return
     */
    public static String getName(Element element) {
        return element.getSimpleName().toString();
    }

    /**
     * 获得包名
     *
     * @param mElements
     * @param element
     * @return
     */
    public static String getPackageName(Elements mElements, Element element) {
        return mElements.getPackageOf(element).getQualifiedName().toString();
    }

    /**
     * 获得属性的值
     *
     * @param element
     * @return
     */
    public static Object getValue(VariableElement element) {
        return element.getConstantValue();
    }

    /**
     * 获得属性的修饰符
     *
     * @param element
     * @return
     */
    public static Modifier[] getModifiers(Element element) {
        Set<Modifier> modifiers = element.getModifiers();
        return modifiers.toArray(new Modifier[modifiers.size()]);
    }

    /**
     * 判断是否包含 final 修饰器
     *
     * @param element
     * @return
     */
    public static boolean isFinal(Element element) {
        Set<Modifier> modifiers = element.getModifiers();
        return modifiers.contains(Modifier.FINAL);
    }

    /**
     * 属于Object,并且不能被操作的方法
     *
     * @param method
     * @return
     */
    public static boolean isObjectMethod(ExecutableElement method) {
        Name simpleName = method.getSimpleName();
        for (String s : OBJECT_METHOD) {
            if (simpleName.contentEquals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVoid(TypeName type) {
        return "void".equals(type.toString());
    }

    /**
     * 判断是否包含 static 修饰器
     *
     * @param element
     * @return
     */
    public static boolean isStatic(Element element) {
        Set<Modifier> modifiers = element.getModifiers();
        return modifiers.contains(Modifier.STATIC);
    }

    public static String toStringNotnull(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    /**
     * 获得参数
     *
     * @param parameters
     * @return
     */
    public static List<ParameterSpec> getParams(List<? extends VariableElement> parameters) {
        List<ParameterSpec> array = new ArrayList<>();
        for (VariableElement parameter : parameters) {
            array.add(ParameterSpec.builder(getType(parameter), getName(parameter), getModifiers(parameter)).build());
        }
        return array;
    }


    /**
     * 将参数列表转为传输字符串
     *
     * @param params
     * @return
     */
    public static String transferParams(List<ParameterSpec> params) {
        StringBuilder sb = new StringBuilder();
        for (ParameterSpec param : params) {
            sb.append(",").append(param.name);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * 将参数列表转为字符串的参数列表
     *
     * @return
     */
    public static String flatParams(List<ParameterSpec> params) {
        StringBuilder sb = new StringBuilder();
        for (ParameterSpec param : params) {
            String modifiers = flatModifiers(param.modifiers);
            sb.append(",").append(modifiers).append(param.type.toString()).append(" ").append(param.name);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * 将修饰器列表转为字符串列表
     *
     * @param modifiers
     * @return
     */
    public static String flatModifiers(Set<Modifier> modifiers) {
        StringBuilder sb = new StringBuilder();
        for (Modifier modifier : modifiers) {
            sb.append(modifier.toString()).append(" ");
        }
        return sb.toString();
    }


    /**
     * 获得异常类型列表
     *
     * @param method
     * @return
     */
    public static Iterable<? extends TypeName> getExceptions(ExecutableElement method) {
        List<? extends TypeMirror> thrownTypes = method.getThrownTypes();
        List<TypeName> array = new ArrayList<>();
        for (TypeMirror thrownType : thrownTypes) {
            array.add(getType(thrownType));
        }
        return array;
    }

}