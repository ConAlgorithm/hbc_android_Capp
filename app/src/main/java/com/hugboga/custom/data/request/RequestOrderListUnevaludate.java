package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserTravel;

import org.xutils.http.annotation.HttpRequest;

@HttpRequest(path = UrlLibs.ORDER_LIST_UNEVALUDATE, builder = NewParamsBuilder.class)
public class RequestOrderListUnevaludate extends RequestTravel{

    public RequestOrderListUnevaludate(Context context,int orderShowType, int limit,int offset) {
        super(context, orderShowType,limit,offset);
    }

    @Override
    public String getUrlErrorCode() {
        return "40117";
    }
    @Override
    public ImplParser getParser() {
        return new ParserTravel();
    }
}