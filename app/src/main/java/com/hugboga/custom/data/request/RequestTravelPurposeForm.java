package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/1.
 * http://wiki.hbc.tech/pages/viewpage.action?title=C4.0.0&spaceKey=hbcAdmin
 * C端创建工单，提交
 */
@HttpRequest(path = UrlLibs.API_CREATE_TRAVEL_FORM, builder = NewParamsBuilder.class)
public class RequestTravelPurposeForm extends BaseRequest{

    public RequestTravelPurposeForm(Context context,String opUserId,String opUserName,String workChannel,String workChannelName,
                                    String toCityId,String toCity,String tripTimeStr,String userRemark,String userAreaCode,String userMobile) {
        super(context);
        map = new HashMap<>();
        map.put("opUserId", opUserId);
        map.put("opUserName", opUserName);
        map.put("workChannel", workChannel);
        map.put("workChannelName", workChannelName);
        map.put("toCityId", toCityId);
        map.put("toCity", toCity);
        map.put("tripTimeStr", tripTimeStr);
        map.put("tripDayNum", 0);
        map.put("adultNum", 0);
        map.put("childNum", 0);
        map.put("partnerRemark", "无");
        map.put("userRemark", userRemark);
        map.put("userAreaCode", userAreaCode);
        map.put("userMobile", userMobile);
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40121";
    }
}