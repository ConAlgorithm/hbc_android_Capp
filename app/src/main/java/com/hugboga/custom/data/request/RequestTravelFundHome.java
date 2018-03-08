package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

@HttpRequest(path = UrlLibs.TRAVELFUND_HOME, builder = NewParamsBuilder.class)
public class RequestTravelFundHome extends BaseRequest<TravelFundData> {

    public RequestTravelFundHome(Context context) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("userId", UserEntity.getUser().getUserId(context));
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.TRAVELFUND_HOME, TravelFundData.class);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40207";
    }

}