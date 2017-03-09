package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserTravel;

import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * 订单行程列表 解析器
 * Created by admin on 2016/3/23.
 */
public abstract class RequestTravel extends BaseRequest {
    public RequestTravel(Context context, int orderShowType) {
        super(context);
        map = new HashMap<String, Object>();
        if (orderShowType != 0) {
            map.put("searchType", orderShowType);
        }
    }

    @Override
    public ImplParser getParser() {
        return new ParserTravel();
    }

}
