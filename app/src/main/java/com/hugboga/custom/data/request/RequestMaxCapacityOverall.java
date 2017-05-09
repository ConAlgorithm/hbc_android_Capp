package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CapacityBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

@HttpRequest(path = UrlLibs.API_MAX_CAPACITY_OVERALL, builder = NewParamsBuilder.class)
public class RequestMaxCapacityOverall extends BaseRequest<CapacityBean> {

    public RequestMaxCapacityOverall(Context context ) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", Constants.REQUEST_SOURCE);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "40140";
    }

    private class DataParser extends ImplParser {

        @Override
        public Object parseArray(JSONArray array) throws Throwable {
            return null;
        }

        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            CapacityBean capacityBean = new CapacityBean();
            capacityBean.numOfPerson = obj.optInt("numOfPerson");
            capacityBean.numOfLuggage = obj.optInt("numOfLuggage");
            return capacityBean;
        }
    }
}