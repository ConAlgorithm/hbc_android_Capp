package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.OrderInfoBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/3/5.
 */
@HttpRequest(path = UrlLibs.API_ORDER_GROUP, builder = NewParamsBuilder.class)
public class RequestOrderGroup extends BaseRequest<OrderInfoBean> {

    private boolean isSeckills = false;

    public RequestOrderGroup(Context context, String _bodyEntity, boolean isSeckills) {
        super(context);
        map = new HashMap<String, Object>();
        this.isSeckills = isSeckills;
        this.bodyEntity = _bodyEntity;
        errorType = ERROR_TYPE_IGNORE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(getUrl(), OrderInfoBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        if (isSeckills) {
            return "40165";
        } else {
            return "40122";
        }
    }

    @Override
    public String getUrl() {
        if (isSeckills) {
            return UrlLibs.API_DAILY_SECKILL;
        } else {
            return UrlLibs.API_ORDER_GROUP;
        }
    }
}