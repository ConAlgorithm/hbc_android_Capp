package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by qingcha on 16/6/4.
 */
@HttpRequest(path = UrlLibs.API_ORDER_EDIT, builder = NewParamsBuilder.class)
public class RequestOrderEdit extends BaseRequest<GuidesDetailData> {

    public RequestOrderEdit(Context context, Params params) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderNo", params.orderNo);
        map.put("orderType", params.orderType);
        map.put("serviceAddressTel", params.serviceAddressTel);
        map.put("serviceAreaCode", params.serviceAreaCode);
        map.put("userRemark", params.userRemark);
        if (!TextUtils.isEmpty(params.startAddress)) {
            map.put("startAddress", params.startAddress);
        }
        if (!TextUtils.isEmpty(params.flightBrandSign)) {
            map.put("flightBrandSign", params.flightBrandSign);
        }
        if (!TextUtils.isEmpty(params.flightNo)) {
            map.put("flightNo", params.flightNo);
        }
        map.put("userEx", params.userEx);
        if (!TextUtils.isEmpty(params.realUserEx)) {
            map.put("realUserEx", params.realUserEx);
            map.put("isRealUser", 2);
        } else {
            map.put("isRealUser", 1);
        }
        map.put("adultNum", params.adultNum);
        map.put("childNum", params.childNum);
        map.put("isArrivalVisa", params.isArrivalVisa);
        map.put("serviceDate", params.serviceDate);
        map.put("serviceRecTime", params.serviceRecTime);
        map.put("servicePassCitys", params.servicePassCitys);
        map.put("flightAirportCode", params.flightAirportCode);
        map.put("flightAirportName", params.flightAirportName);
        map.put("flightFlyTimeL", params.flightFlyTimeL);
        map.put("flightArriveTimeL", params.flightArriveTimeL);
        map.put("flightAirportBuiding", params.flightAirportBuiding);
        map.put("realSendSms", params.realSendSms);
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            return obj.toString();
        }
    }

    public static class Params implements Serializable {
        public String orderNo;
        public int orderType;//可选1-接机；2-送机；3-日租；4-次租
        public int adultNum;//成人座位数
        public int childNum;//小孩座位数
        public String serviceAddressTel;//目的地酒店或者区域电话号码
        public String serviceAreaCode;//目的地区域
        public String userRemark;//备注
        public int isArrivalVisa;//是否落地签
        public String serviceDate;//服务时间
        public String serviceRecTime;//日租服务时间的时分秒
        public String servicePassCitys;//日租途径城市
        public String startAddress;//出发地
        public String flightBrandSign;//接送机接机牌名称
        public String flightNo;//送机航班号
        public String flightAirportCode;//送机航班机场三字码
        public String flightAirportName;//送机机场名称
        public String flightFlyTimeL;//送机起飞时间
        public String flightArriveTimeL;//送机到达时间
        public String flightAirportBuiding;//送机航站楼
        public String userEx;
        public String realUserEx;
        public String isRealUser;//有乘车人 必须传入为2 没有1
        public int realSendSms;//是否给乘车人发送短信 0 不发送 1 发送
    }
}