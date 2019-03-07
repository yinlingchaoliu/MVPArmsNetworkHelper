package com.network.helper.config;

import com.network.helper.entity.NetWorkEntity;

/**
 * Arm Mvp 帮助工具
 *  @author chentong
 *  @date 2019-3-6
 */
public class ArmConstant {

    public static String selectText = "    public static final String DEPOSITORY_ACTIVEPAY_INTERFACENAME = \"/petty/borrow/activePay\";";

    public static final String DEPOSITORY_ACTIVEPAY_INTERFACENAME = "/petty/borrow/activePay";

    //Api 拼接内容
    public static String getApiString(NetWorkEntity entity) {
        String content = "";
        if (entity.getMethod().equalsIgnoreCase("post")) {
            content = "@POST(Urls." + entity.getUrl() + ") \n";
            content = content + "@FormUrlEncoded \n";
        } else {
            content = "@GET(Urls." + entity.getUrl() + ") \n";
        }

        return content + getObsApiInterface(entity);
    }

    //Contract
    //IView
    public static String getContractViewStr(NetWorkEntity entity) {
        return "void " + getContractViewInterface(entity);
    }

    //IModel
    public static String getContractModelStr(NetWorkEntity entity) {
        return getObsInterface(entity);
    }

    //Model
    public static String getModelStr(NetWorkEntity entity) {
        String content = "@Override\n";
        content = content + "public " + getObsResponse(entity) + entity.getRequestInterface() + "{\n";
        content = content + "\t" + "return " + "mRepositoryManager.obtainRetrofitService( Api.class )." + getInterfaceMethodParam(entity) + ";\n}";
        return content;
    }

    public static String getPresenterStr(NetWorkEntity entity) {
        String content = "public void " + entity.getRequestInterface() + "{\n";
        content = content + "\tmModel." + getInterfaceMethodParam(entity) + "\n";
        content = content + "\t\t.compose( RxUtils.apply( mRootView ) )\n";
        content = content + "\t\t.subscribe( new ErrorHandleSubscriber<" + getResponse(entity) + ">(mErrorHandler) {\n";
        content = content + "\t\t\t@Override\n";
        content = content + "\t\t\tpublic void onNext(" + getResponse(entity) + " dataBean) {\n";
        content = content + "\t\t\t\tif (dataBean.isSuccessful()){\n";
        if (!isObject(entity)) {
            content = content + "\t\t\t\t\tmRootView." + getContractViewCompleteMethod(entity) + "(dataBean.data);" + "\n";
        } else {
            content = content + "\t\t\t\t\tmRootView." + getContractViewCompleteMethod(entity) + "();" + "\n";
        }
        content = content + "\t\t\t\t}else {\n";
        content = content + "\t\t\t\t\tToastUtils.show(dataBean.msg);\n";
        content = content + "\t\t\t\t}\n";
        content = content + "\t\t\t}\n";
        content = content + "\t\t} );\n";
        content = content + "}\n";
        return content;
    }

    public static String getPageContractView(NetWorkEntity entity) {
        String content = "@Override\n";
        content = content + "public void " + getContractViewInterfaceClear(entity) + " { \n";
        if (!isObject(entity)){
            content = content + "\t//data数据判空校验\n";
            content = content + "\tif (null == data) return;\n";
        }
        content = content + "}\n";
        return content;
    }

    //IView方法
    private static String getContractViewInterface(NetWorkEntity entity) {
        return getContractViewInterfaceClear(entity) + ";";
    }

    private static String getContractViewInterfaceClear(NetWorkEntity entity) {
        if (isObject(entity)) {
            return getContractViewCompleteMethod(entity) + "()";
        } else {
            return getContractViewCompleteMethod(entity) + "(" + entity.getResponseBean() + " data )";
        }
    }

    private static String getContractViewCompleteMethod(NetWorkEntity entity) {
        return getInterfaceMethodName(entity) + "Complete";
    }

    //获得interface方法
    private static String getObsInterface(NetWorkEntity entity) {
        return getObsResponse(entity) + entity.getRequestInterface() + ";";
    }

    private static String getObsApiInterface(NetWorkEntity entity) {
        return getObsResponse(entity) + getInterfaceApiParam(entity) + ";";
    }


    private static String getObsResponse(NetWorkEntity entity) {
        return "Observable<" + getResponse(entity) + "> ";
    }

    private static String getResponse(NetWorkEntity entity) {
        if (isObject(entity)) {
            return "BaseDataBean";
        } else {
            return "BaseDataBean<" + entity.getResponseBean() + ">";
        }
    }

    //判断返回类是否为空
    private static boolean isObject(NetWorkEntity entity) {
        if (entity.getResponseBean() == null ||
                entity.equals("") ||
                entity.getResponseBean().equalsIgnoreCase("null") ||
                entity.getResponseBean().equalsIgnoreCase("object")) {
            return true;
        }
        return false;
    }

    private static String getInterfaceMethodParam(NetWorkEntity entity) {
        return getInterfaceMethodName(entity) + "(" + getParam(entity) + ")";
    }

    private static String getInterfaceApiParam(NetWorkEntity entity){
        return getInterfaceMethodName(entity) + "(" + getApiParam(entity) + ")";
    }

    public static String getInterfaceMethodName(NetWorkEntity entity) {
        String requestInterface = entity.getRequestInterface();
        int index = requestInterface.indexOf("(");
        String method = requestInterface.substring(0, index).trim();
        return method;
    }

    private static String getInterfaceParam(NetWorkEntity entity) {
        String requestInterface = entity.getRequestInterface();
        int index = requestInterface.indexOf("(");
        int last = requestInterface.indexOf(")");
        return requestInterface.substring(index + 1, last);
    }

    private static String getParam(NetWorkEntity entity) {
        String content = getInterfaceParam(entity).trim();
        if (content.length() == 0) return "";
        String param = "";
        if (content.contains(",")) {
            String[] args = content.split(",");
            for (String str : args) {
                if (param.trim().length() == 0) {
                    param = getRightParam(str);
                } else {
                    param = param + "," + getRightParam(str);
                }
            }

        } else {
            param = getRightParam(content);
        }
        return param;
    }

    private static String getApiParam(NetWorkEntity entity){
        String content = getInterfaceParam(entity).trim();
        if (content.length() == 0) return "";
        String param = "";
        if (content.contains(",")){
            String[] args = content.split(",");
            for (String str : args) {
                if (param.trim().length() == 0) {
                    param = getOneRetrofitParam(entity.getMethod(),str);
                } else {
                    param = param + "," + getOneRetrofitParam(entity.getMethod(),str);
                }
            }
        }else {
            param = getOneRetrofitParam(entity.getMethod(),content);
        }
        return param;
    }

    private static String getOneRetrofitParam(String method, String str){

        if (str.trim().length() == 0) return "";
        if (str.trim().startsWith("RequestBody")){
            return "@Body " + str.trim();
        }
        if (method.equalsIgnoreCase("post")){
            if (str.trim().startsWith("Map")){
                return "@FieldMap " + str.trim();
            }else{
                String rightContent = getRightParam(str);
                return "@Field(\""+ rightContent +"\") "+str.trim();
            }
        }else {
            if (str.trim().startsWith("Map")){
                return "@QueryMap " + str.trim();
            }else{
                String rightContent = getRightParam(str);
                return "@Query(\""+ rightContent +"\") "+str.trim();
            }
        }
    }

    private static String getRightParam(String content) {
        String str = content.trim();
        int index = str.indexOf(" ");
        return str.substring(index + 1).trim();
    }

    public static String parseUrl(String selectText) {
        try {
            int index = selectText.indexOf("String");
            int last = selectText.indexOf("=");
            return selectText.substring(index + 6, last).trim();
        }catch (Exception e){
            return "";
        }
    }

    public static String parseInterface(String selectText) {
        try {
            int index = selectText.indexOf("\"");
            int last = selectText.lastIndexOf("\"");
            String content = selectText.substring(index + 1, last).trim();
            String[] args = content.split("/");
            return args[args.length - 1].trim() + "()";
        } catch (Exception e) {
            return "";
        }
    }

    public static String trimEnd(String input){
        if (input == null || input.trim().length() == 0) return "";
        return input.replace(";","");
    }

    public static String getBeanStr(NetWorkEntity entity){
        if (isObject(entity) || entity.getClearResponseBean().equals("")) return "";
        return "public class "+ entity.getClearResponseBean() + "{\n}";
    }

    //考虑包含List<String>
    public static String clearResponseBean(NetWorkEntity entity){
        String responseBean = entity.getResponseBean();
        //去字符串中间空格
        responseBean = responseBean.replace(" ","");
        if (responseBean.contains("List<")){
            int index = responseBean.indexOf("List<");
            int last = responseBean.indexOf(">");
            responseBean = responseBean.substring(index+5,last);
        }

        if (responseBean.startsWith("String") ||
                responseBean.startsWith("Double") ||
                responseBean.startsWith("Long") ||
                responseBean.startsWith("Integer") ||
                responseBean.startsWith("Float")
                || responseBean.equalsIgnoreCase("object")){
            return "";
        }
        return responseBean.trim();
    }

    public static boolean checkInterface(String input){
        if (input == null | input.trim().length() == 0) return false;
        if (input.trim().contains("(") && input.trim().contains(")")){
            return true;
        }
        return false;
    }

}