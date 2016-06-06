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
        map.put("adultNum", params.orderType);
        map.put("childNum", params.childNum);
        map.put("serviceAddressTel", params.serviceAddressTel);
        map.put("serviceAreaCode", params.serviceAreaCode);
        map.put("userAreaCode1", params.userAreaCode1);
        map.put("userMobile1", params.userMobile1);
        map.put("userAreaCode2", params.userAreaCode2);
        map.put("userMobile2", params.userMobile2);
        map.put("userAreaCode3", params.userAreaCode3);
        map.put("userMobile3", params.userMobile3);
        map.put("userRemark", params.userRemark);
        map.put("userName", params.userName);
        map.put("isArrivalVisa", params.isArrivalVisa);
        map.put("serviceDate", params.serviceDate);
        map.put("serviceRecTime", params.serviceRecTime);
        map.put("servicePassCitys", params.servicePassCitys);
        map.put("startAddress", params.startAddress);
        map.put("flightBrandSign", params.flightBrandSign);
        map.put("flightNo", params.flightNo);
        map.put("flightAirportCode", params.flightAirportCode);
        map.put("flightAirportName", params.flightAirportName);
        map.put("flightFlyTimeL", params.flightFlyTimeL);
        map.put("flightArriveTimeL", params.flightArriveTimeL);
        map.put("flightAirportBuiding", params.flightAirportBuiding);
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
        public String userAreaCode1;//区域号1
        public String userMobile1;//用户手机号1
        public String userAreaCode2;//区域号2
        public String userMobile2;//用户手机号2
        public String userAreaCode3;//区域号3
        public String userMobile3;//用户手机号3
        public String userRemark;//备注
        public String userName;//联系人姓名
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
    }
}