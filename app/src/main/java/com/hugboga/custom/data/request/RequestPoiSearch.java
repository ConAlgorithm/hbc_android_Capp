package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.data.bean.NewPoiBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserPoiSearch;

import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/21.
 */
@HttpRequest(path = UrlLibs.POI_SEARCH_PLACES, builder = NewParamsBuilder.class)
public class RequestPoiSearch extends BaseRequest<NewPoiBean> {
    public RequestPoiSearch(Context context, int cityId, String location, String keyword, int offset, int limit) {
        super(context);
        map = new HashMap();
        try {
            map.put("location", location);
            if (cityId != -1)
                map.put("cityId", cityId);
            map.put("keyword", keyword);
            map.put("offset", offset);
            map.put("limit", limit);
        } catch (Exception e) {
            MLog.e(e.toString());
        }
    }

    @Override
    public ImplParser getParser() {
        return new ParserPoiSearch();
    }

    @Override
    public String getUrlErrorCode() {
        return "40069";
    }
}
