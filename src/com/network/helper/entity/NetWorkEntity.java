package com.network.helper.entity;

import com.network.helper.config.ArmConstant;

/**
 *
 * 用户输入参数
 * @author chentong
 * @date 2019-3-6
 *
 */
public class NetWorkEntity {
    private String packageName = "com.hexin.xffq"; //包路径
    private String psiInput; //粘贴值
    private String url; //url
    private String method; //method
    private String requestInterface; //请求接口
    private String responseBean;  //返回实体类
    private String bizModule;       //业务模块
    private String clearResponseBean; //清理后真实的实体类

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPsiInput() {
        return psiInput;
    }

    public void setPsiInput(String psiInput) {
        this.psiInput = psiInput;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestInterface() {
        return requestInterface;
    }

    public void setRequestInterface(String requestInterface) {
        this.requestInterface = requestInterface;
    }

    public String getResponseBean() {
        return responseBean;
    }

    public void setResponseBean(String responseBean) {
        this.responseBean = responseBean;
        setClearResponseBean(ArmConstant.clearResponseBean(this));
    }

    public String getBizModule() {
        return bizModule;
    }

    public void setBizModule(String bizModule) {
        this.bizModule = bizModule;
    }

    public String getClearResponseBean() {
        return clearResponseBean;
    }

    private void setClearResponseBean(String clearResponseBean) {
        this.clearResponseBean = clearResponseBean;
    }

    @Override
    public String toString() {
        return "NetWorkEntity{" +
                "packageName='" + packageName + '\'' +
                ", psiInput='" + psiInput + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", requestInterface='" + requestInterface + '\'' +
                ", responseBean='" + responseBean + '\'' +
                ", bizModule='" + bizModule + '\'' +
                ", clearResponseBean='" + clearResponseBean + '\'' +
                '}';
    }

    public static void main(String[] args) {
        NetWorkEntity entity = new NetWorkEntity();

        String selectText = "    public static final String DEPOSITORY_ACTIVEPAY_INTERFACENAME = \"/petty/borrow/activePay\";";

        System.out.println(ArmConstant.parseUrl(selectText));
        System.out.println(ArmConstant.parseInterface(selectText));

        entity.setUrl("DEPCONFIRM_INTERFACENAME");
        entity.setMethod("post");
        entity.setRequestInterface("getDepStep(int  borrowId)");
        entity.setResponseBean("object");
        entity.setBizModule("OrderDetails");

        System.out.println(entity.getClearResponseBean());

        System.out.println("==========mvp.model.api.Api.java========");
        System.out.println(ArmConstant.getApiString(entity));

        System.out.println("==========mvp.contract.BizContract.java========");
        System.out.println(ArmConstant.getContractViewStr(entity));

        System.out.println("==========mvp.contract.BizContract.java========");
        System.out.println(ArmConstant.getContractModelStr(entity));

        System.out.println("==========mvp.model.BizModel.java========");
        System.out.println(ArmConstant.getModelStr(entity));

        System.out.println("==========mvp.presenter.BizPresenter.java========");
        System.out.println(ArmConstant.getPresenterStr(entity));

        System.out.println("==========mvp.ui.activity/fragment.BizActivity/Fragment.java========");
        System.out.println(ArmConstant.getPageContractView(entity));

        System.out.println("==========mvp.bean.Bean.java========");
        System.out.println(ArmConstant.getBeanStr(entity));
    }
}