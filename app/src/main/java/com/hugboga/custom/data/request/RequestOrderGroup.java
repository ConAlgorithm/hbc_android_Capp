package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderContactBean;
import com.hugboga.custom.data.bean.OrderInfoBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by qingcha on 17/3/5.
 */
@HttpRequest(path = UrlLibs.API_ORDER_GROUP, builder = NewParamsBuilder.class)
public class RequestOrderGroup extends BaseRequest<OrderInfoBean> {

    public RequestOrderGroup(Context context, String _bodyEntity) {
        super(context);
        map = new HashMap<String, Object>();
        this.bodyEntity = _bodyEntity;
        errorType = ERROR_TYPE_IGNORE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_ORDER_GROUP, OrderInfoBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40122";
    }
}