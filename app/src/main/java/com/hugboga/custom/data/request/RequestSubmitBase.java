package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.utils.Config;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;

import java.util.TreeMap;

/**
 * 下单请求公用基类
 * Created by admin on 2016/3/22.
 */
public class RequestSubmitBase extends BaseRequest<String> {

    public RequestSubmitBase(Context context,OrderBean orderBean) {
        super(context);
        map = new TreeMap();
        map.put("orderNo", orderBean.orderNo);
        map.put("orderChannel", Config.channelId);
        map.put("orderType", orderBean.orderType);
        map.put("urgentFlag", orderBean.urgentFlag);
        if(orderBean.checkInPrice!=null)
            map.put("checkInPrice", orderBean.checkInPrice);
        map.put("priceChannel", orderBean.orderPrice);
        map.put("priceMark", orderBean.priceMark);
        map.put("adultNum", orderBean.adult);
        map.put("childNum", orderBean.child);
        if(orderBean.childSeat!=null)
            map.put("childSeat", TextUtils.join(",", orderBean.childSeat));
        map.put("userName", orderBean.contactName);
        map.put("serviceCityId", orderBean.serviceCityId);
        map.put("serviceEndCityid", orderBean.serviceEndCityid);

        map.put("serviceTimeL", orderBean.serviceTime);
        map.put("serviceEndTimeL", orderBean.serviceEndTime);
        if(orderBean.serviceStartTime!=null)
            map.put("serviceRecTime", orderBean.serviceStartTime+":00");
        map.put("totalDays", orderBean.totalDays);
        map.put("expectedCompTime", orderBean.expectedCompTime);

        map.put("startAddress", orderBean.startAddress);
        map.put("startAddressDetail", orderBean.startAddressDetail);
        map.put("startAddressPoi", orderBean.startLocation);
        map.put("destAddress", orderBean.destAddress);
        map.put("destAddressDetail", orderBean.destAddressDetail);
        map.put("destAddressPoi", orderBean.terminalLocation);
        map.put("distance", orderBean.distance);
        map.put("carTypeId", orderBean.carType);
        map.put("carSeatNum", orderBean.seatCategory);
        map.put("carDesc", orderBean.carDesc);


        if(orderBean.contact!=null&&orderBean.contact.size()>0){
            map.put("userAreaCode1", orderBean.contact.get(0).areaCode);
            map.put("userMobile1", orderBean.contact.get(0).tel);
        }
        if(orderBean.orderType!=null&&orderBean.orderType.equals(5)){//如果是SKU  传
            map.put("lineSubject", orderBean.lineSubject);
            map.put("lineDescription", orderBean.lineDescription);
            map.put("orderGoodsType", orderBean.orderGoodsType);
            map.put("goodNo", orderBean.goodsNo);
        }

    }


    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    public ImplParser getParser() {
        return new ImplParser() {
            @Override
            public String parseObject(JSONObject obj) throws Throwable {
                return obj.optString("orderno");
            }
        };
    }
}
