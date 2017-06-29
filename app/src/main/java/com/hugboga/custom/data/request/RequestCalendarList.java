package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.squareup.timessquare.CalendarListBean;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;
@HttpRequest(path = UrlLibs.API_CALENDAR_LIST, builder = NewParamsBuilder.class)
public class RequestCalendarList extends BaseRequest<HashMap<String, CalendarListBean>> {

    private int orderType;

    public RequestCalendarList(Context context, String guideMonth, String guideId, int orderType, String tag) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("guideMonth", guideMonth);
        map.put("guideId", guideId);
        this.tag = tag;
        this.orderType = orderType;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return new DataParser(orderType);
    }

    @Override
    public String getUrlErrorCode() {
        return "40158";
    }

    private static class DataParser extends ImplParser {

        private int orderType;

        public DataParser(int orderType) {
            this.orderType = orderType;
        }

        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            return CalendarListBean.getMonthMap(obj.toString(), orderType);
        }
    }
}

