package com.acap.rc.processor;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * Tip:
 *      标识Class的数据
 *
 * Created by ACap on 2021/4/15 17:43
 * </pre>
 */
class ApiClassModel {


    private static final String CLASS_SUFFIX = "Service";

    //获得类的全量名称
    public static String getClassAllName(String packagename, String apiname) {
        return packagename + "." + apiname + CLASS_SUFFIX;
    }

    //生成类的Body
    public static final String BODY_CLASS = "${package}\n" +
            "import com.acap.ec.Event;\n" +
            "import com.acap.rc.service.ServiceGenerator;\n" +
            "${import}\n" +
            "public class ${ApiClass}" + CLASS_SUFFIX + " {\n" +
            "    private static final ${ApiClass} mApi = ServiceGenerator.generator(${ApiClass}.class, \"${Url}\", ${OkHttpConfig}.class, ${RetrofitConfig}.class);\n" +
            "    private ${ApiClass}" + CLASS_SUFFIX + "() {}\n" +
            "${methods}\n" +
            "}";

    //如果返回类型为Request
    public static final String BODY_METHOD_REQUEST = "@SuppressWarnings(\"unchecked\")\n" +
            "public static final <P> Event<P, ${Void}> ${method}(${params}) {return (Event) mApi.${method}(${paramsvalue});}\n";
    //其他返回类型
    public static final String BODY_METHOD_OTHER = "public static final ${Void} ${method}(${params}) {return mApi.${method}(${paramsvalue});}\n";

    /**
     * 包名
     */
    private String mPackage;
    /**
     * 导入数据
     */
    private List<String> mImports = new ArrayList<>();
    /**
     * 类名
     */
    private String mApiClass;

    private String mUrl;
    private String mOkHttpConfigClass;
    private String mRetrofitConfigClass;

    private List<Method> mMethods = new ArrayList<>();

    public void setPackage(String mPackage) {
        this.mPackage = mPackage;
    }

    private String getPackage() {
        return "package " + mPackage + ";";
    }

    public void setApiClass(String mApiClass) {
        this.mApiClass = mApiClass;
    }

    public String getApiClass() {
        return mApiClass;
    }

    public void addImport(String imports) {
        mImports.add(imports);
    }

    public String getImports() {
        StringBuffer SB = new StringBuffer();
        for (String mImport : mImports) {
            SB.append("import ").append(mImport).append(";\n");
        }
        return SB.toString();
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setOkHttpConfigClass(String mOkHttpConfigClass) {
        this.mOkHttpConfigClass = mOkHttpConfigClass;
    }

    public String getOkHttpConfigClass() {
        return mOkHttpConfigClass;
    }

    public void setRetrofitConfigClass(String mRetrofitConfigClass) {
        this.mRetrofitConfigClass = mRetrofitConfigClass;
    }

    public String getRetrofitConfigClass() {
        return mRetrofitConfigClass;
    }

    public void addMethod(Method method) {
        mMethods.add(method);
    }

    public String getMethods() {
        StringBuffer SB = new StringBuffer();
        for (Method method : mMethods) {
            SB.append("    ").append(method).append("\n");
        }
        return SB.toString();
    }

    @Override
    public String toString() {
        return BODY_CLASS
                .replace("${package}", getPackage())
                .replace("${import}", getImports())
                .replace("${ApiClass}", getApiClass())
                .replace("${Url}", getUrl())
                .replace("${OkHttpConfig}", getOkHttpConfigClass())
                .replace("${RetrofitConfig}", getRetrofitConfigClass())
                .replace("${methods}", getMethods())
                ;
    }


    /**
     * 方法的参数
     */
    public static final class MethodParams {
        private String mType; //参数的类型
        private String mName; //参数的名称

        public MethodParams(String mType, String mName) {
            this.mType = mType;
            this.mName = mName;
        }

        @Override
        public String toString() {
            return String.format("Method(%s %s)", mType, mName);
        }
    }

    public static abstract class Method {
        protected String mName; //方法名
        protected List<MethodParams> mParams = new ArrayList<>();
        protected String mVoid; //方法返回值内容


        public Method(String mName, String mVoid) {
            this.mName = mName;
            this.mVoid = mVoid;
        }

        public Method addParams(MethodParams params) {
            mParams.add(params);
            return this;
        }

        //方法入参文本
        protected String getParamsEntry() {
            StringBuffer SB = new StringBuffer();
            for (MethodParams mParam : mParams) {
                SB.append(",").append(mParam.mType).append(" ").append(mParam.mName);
            }
            if (SB.length() > 0) {
                SB.deleteCharAt(0);
            }
            return SB.toString();
        }

        //方法传递参数文本
        protected String getParamsPass() {
            StringBuffer SB = new StringBuffer();
            for (MethodParams mParam : mParams) {
                SB.append(",").append(mParam.mName);
            }
            if (SB.length() > 0) {
                SB.deleteCharAt(0);
            }
            return SB.toString();
        }


        @Override
        public abstract String toString();
    }

    //Request的方法
    public static class RequestMethod extends Method {


        public RequestMethod(String mName, String mVoid) {
            super(mName, mVoid);
        }

        @Override
        public String toString() {
            return BODY_METHOD_REQUEST
                    .replace("${Void}", mVoid)
                    .replace("${method}", mName)
                    .replace("${params}", getParamsEntry())
                    .replace("${paramsvalue}", getParamsPass());
        }
    }

    //其他方法
    public static class OtherMethod extends Method {


        public OtherMethod(String mName, String mVoid) {
            super(mName, mVoid);
        }

        @Override
        public String toString() {
            return BODY_METHOD_OTHER
                    .replace("${Void}", mVoid)
                    .replace("${method}", mName)
                    .replace("${params}", getParamsEntry())
                    .replace("${paramsvalue}", getParamsPass());
        }
    }

}
