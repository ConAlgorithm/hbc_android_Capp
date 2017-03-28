package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/3.
 */
@HttpRequest(path = UrlLibs.API_QUERY_HAS_CREATE_FORM, builder = NewParamsBuilder.class)
public class RequestHasCreatedTravelForm extends BaseRequest<RequestHasCreatedTravelForm.HasWork> {
    public RequestHasCreatedTravelForm(Context context, String opUserId) {
        super(context);
        map = new HashMap();
        map.put("opUserId", opUserId);
    }

    @Override
    public ImplParser getParser() {
        return new DataParse();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40124";
    }

    public static class DataParse extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            HasWork workOrder = gson.fromJson(obj.toString(),HasWork.class);
            return workOrder;
        }
    }

    public class HasWork implements Serializable {
        public Boolean hasWorkorder;        //

        public Boolean getHasWorkorder() {
            return hasWorkorder;
        }

        public void setHasWorkorder(Boolean hasWorkorder) {
            this.hasWorkorder = hasWorkorder;
        }
    }
}