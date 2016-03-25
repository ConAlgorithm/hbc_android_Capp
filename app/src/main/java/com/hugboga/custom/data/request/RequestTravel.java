package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserTravel;

import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 订单行程列表 解析器
 * Created by admin on 2016/3/23.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_ORDER_LIST,builder = HbcParamsBuilder.class)
public class RequestTravel extends BaseRequest<ArrayList<OrderBean>> {
    public RequestTravel(Context context,  int orderShowType, int offset,int limit)  {
        super(context);
        map = new HashMap<String,Object>();
        map.put("searchType", orderShowType+1);
        map.put("offset",offset);
        map.put("limit",limit);
        }

    @Override
    public ImplParser getParser() {
        return new ParserTravel();
    }
}
