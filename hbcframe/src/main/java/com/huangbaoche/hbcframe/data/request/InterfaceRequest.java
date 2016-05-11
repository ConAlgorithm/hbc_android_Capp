package com.huangbaoche.hbcframe.data.request;


import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

public interface InterfaceRequest {

abstract HttpMethod getHttpMethod();
}
