package com.hugboga.custom.data.request;


import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

@HttpRequest(path = UrlLibs.QUERY_TRAVELFUND_INCOMELOG, builder = NewParamsBuilder.class)
public class RequestTravelFundIncomeLog extends BaseRequest<TravelFundData> {

    public RequestTravelFundIncomeLog(Context context, int offset) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("userId", UserEntity.getUser().getUserId(context));
        map.put("offset", offset);
        map.put("limit", 30);
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.QUERY_TRAVELFUND_INCOMELOG, TravelFundData.class);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40208";
    }

}