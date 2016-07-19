package com.huangbaoche.hbcframe.data.request;


import org.xutils.http.HttpMethod;

public interface InterfaceRequest {

    abstract HttpMethod getHttpMethod();
    String getUrlErrorCode();
}
