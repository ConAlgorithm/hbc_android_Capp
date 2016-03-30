package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserNewOrder;

import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * 请求聊天界面订单数据
 * Created by ZHZEPHI on 2016/3/26.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_IM_ORDER_LIST, builder = HbcParamsBuilder.class)
public class RequestIMOrder extends BaseRequest<Object[]> {

    public RequestIMOrder(Context context, String guideId) {
        super(context);
        map = new HashMap<String, Object>();
        try {
            map.put("guideId", guideId);
            map.put("offset", "0");
            map.put("limit", "10");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ImplParser getParser() {
        return new ParserNewOrder();
    }
}
