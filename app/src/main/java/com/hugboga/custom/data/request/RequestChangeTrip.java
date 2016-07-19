package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2016/3/26.
 */

@HttpRequest(path = UrlLibs.SERVER_IP_ORDER_EDIT,builder = NewParamsBuilder.class)
public class RequestChangeTrip extends BaseRequest {
    public RequestChangeTrip(Context context,OrderBean orderBean) {
        super(context);
        Map<String,Object> dataMap = null;
        map = new TreeMap();
        if(orderBean!=null){
            switch (orderBean.orderType){
                case Constants.BUSINESS_TYPE_PICK:
                    dataMap = new RequestSubmitPick(context,orderBean).getDataMap();
                    break;
                case Constants.BUSINESS_TYPE_SEND:
                    dataMap = new RequestSubmitSend(context,orderBean).getDataMap();
                    break;
                case Constants.BUSINESS_TYPE_DAILY:
                case Constants.BUSINESS_TYPE_DAILY_SHORT:
                case Constants.BUSINESS_TYPE_DAILY_LONG:
                case Constants.BUSINESS_TYPE_COMMEND:
                    dataMap = new RequestSubmitDaily(context,orderBean).getDataMap();
                    break;
                case Constants.BUSINESS_TYPE_RENT:
                    dataMap = new RequestSubmitRent(context,orderBean).getDataMap();
                    break;
            }
            if(dataMap !=null)map.putAll(dataMap);
            dataMap = null;
        }

    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public String getUrlErrorCode() {
        return "40012";
    }
}
