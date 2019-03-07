package com.network.helper.test.mvp.bean;

import java.io.Serializable;

/**
 *
 * bean文件锚定类
 * 用于定位bean文件目录
 * @param <T>
 *
 */
public class BaseDataBean<T> implements Serializable {

    public static final int RESPONSE_SUCCESS_CODE = 200;
    public int code;
    public String msg;
    public T data;

    //判断业务是否成功
    public boolean isSuccessful(){
        return code == RESPONSE_SUCCESS_CODE;
    }

}