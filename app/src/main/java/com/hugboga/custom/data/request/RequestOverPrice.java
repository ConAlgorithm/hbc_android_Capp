package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.OrderCostApplyInfo;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserApplyInfo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2016/3/24.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_ORDER_OVER_PRICE,builder = HbcParamsBuilder.class)
public class RequestOverPrice extends BaseRequest<ArrayList<OrderCostApplyInfo>> {

    public String applyfee;

    public RequestOverPrice(Context context,String orderNo) {
        super(context);
        map = new HashMap<String,Object>();
        map.put("orderNo", orderNo);
    }

    @Override
    public ImplParser getParser() {
        return new ImplParser() {
            @Override
            public Object parseObject(JSONObject obj) throws Throwable {
                applyfee = obj.optString("applyfee");
                JSONArray array = obj.optJSONArray("resultBean");
                ArrayList<OrderCostApplyInfo> list=null;
                if(array!=null) {
                    ParserApplyInfo parserApplyInfo = new ParserApplyInfo();
                    list= new ArrayList<OrderCostApplyInfo>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        list.add(parserApplyInfo.parseObject(object));
                    }
                }
                return list;
            }
        };
    }
}
