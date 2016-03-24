package com.hugboga.custom.data.request;

import android.app.Activity;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserOrder;

import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 *
 * 订单解析类
 * Created by admin on 2015/7/23.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_ORDER_DETAIL,builder = HbcParamsBuilder.class)
public class RequestOrderDetail extends BaseRequest<OrderBean> {

    public RequestOrderDetail(Activity activity,String orderId){
        super(activity);
        map = new HashMap<String,Object>();
        map.put("orderNo", orderId);
    }

    @Override
    public ImplParser getParser() {
        return new ParserOrder();
    }


}
