package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.OrderPriceInfoBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qingcha on 17/8/28.
 */
@HttpRequest(path = UrlLibs.API_BATCH_PRICE_CONTAINSFEE, builder = NewParamsBuilder.class)
public class RequestBatchPriceInfo extends BaseRequest<ArrayList<OrderPriceInfoBean>> {

    public RequestBatchPriceInfo(Context context, String batchNo, long carId, int pickUpFlag, int checkInFlag, int childSeatFlag) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("batchNo", batchNo);
        map.put("carId", carId);
        map.put("pickUpFlag", pickUpFlag);
        map.put("checkInFlag", checkInFlag);
        map.put("childSeatFlag", childSeatFlag);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "40180";
    }

    private class DataParser extends ImplParser {

        @Override
        public Object parseArray(JSONArray array) throws Throwable {
            Gson gson = new Gson();
            ArrayList<OrderPriceInfoBean> orderPriceInfoList = gson.fromJson(array.toString(), new TypeToken<ArrayList<OrderPriceInfoBean>>(){}.getType());
            return orderPriceInfoList;
        }

        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            return null;
        }
    }
}