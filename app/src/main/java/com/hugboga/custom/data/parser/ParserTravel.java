package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.TravelListAllBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 2016/3/23.
 */
public class ParserTravel extends ImplParser {

    public static final int AllLISTT = 0;
    public static final int UNPAYISTT = 1;
    public static final int INGISTT = 2;
    public static final int UNEVALUATEIONLISTT = 3;

    @Override
    public TravelListAllBean parseObject(JSONObject obj) throws Throwable {
        TravelListAllBean travelListAllBean = new TravelListAllBean();
        travelListAllBean.totalSize = obj.optInt("totalSize");
        travelListAllBean.inviteContent = obj.optString("inviteContent");
        JSONArray jsonArray = obj.optJSONArray("resultBean");
        ArrayList<OrderBean> listData = new ArrayList<OrderBean>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject segObj = jsonArray.optJSONObject(i);
                ParserOrder parserOrder = new ParserOrder();
                listData.add(parserOrder.parseObject(segObj));
            }
        }
        travelListAllBean.resultBean = listData;
        travelListAllBean.unpayTotalSize = obj.optInt("unpayTotalSize");
        travelListAllBean.ingTotalSize = obj.optInt("ingTotalSize");
        travelListAllBean.evaluationTotalSize = obj.optInt("evaluationTotalSize");
        return travelListAllBean;
    }
}
