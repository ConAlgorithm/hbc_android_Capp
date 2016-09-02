package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserNewOrder;

import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * 请求历史订单列表
 * Created by ZHZEPHI on 2016/3/26.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_ORDER_HISTORY, builder = NewParamsBuilder.class)
public class RequestOrder extends BaseRequest<Object[]> {

    public RequestOrder(Context context, String guideId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("guideId", guideId);
        map.put("orderByType", "1"); //排序类型：SERVING_TIME(1,"服务时间"),DELIVER_ACP_TIME(2,"接单时间")
    }

    @Override
    public ImplParser getParser() {
        return new ParserNewOrder();
    }

    @Override
    public String getUrlErrorCode() {
        return "40064";
    }
}
