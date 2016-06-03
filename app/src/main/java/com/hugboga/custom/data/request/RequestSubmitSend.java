package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.annotation.HttpRequest;

/**
 * Created by admin on 2016/3/22.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_SUBMIT_TRANSFER, builder = NewParamsBuilder.class)
public class RequestSubmitSend extends RequestSubmitBase{
    public RequestSubmitSend(Context context, OrderBean orderBean) {
        super(context, orderBean);
        map.put("memo", orderBean.memo);
        map.put("serviceAreaCode", orderBean.serviceAreaCode);
        map.put("serviceAddressTel", orderBean.serviceAddressTel);

        map.put("flightAirportCode", orderBean.flightAirportCode);
        map.put("flightAirportName", orderBean.destAddress);
        map.put("realSendSms",orderBean.realSendSms);

        map.put("coupId",orderBean.coupId);
        map.put("userEx",orderBean.userEx);

        map.put("realUserEx",orderBean.realUserEx);
        map.put("travelFund",orderBean.travelFund);
        map.put("guideCollectId",orderBean.guideCollectId);
        map.put("childSeat",orderBean.childSeat);

        map.put("priceActual",orderBean.priceActual);
        map.put("luggageNumber",orderBean.luggageNum);
        map.put("coupPriceInfo",orderBean.coupPriceInfo);
        map.put("userRemark",orderBean.userRemark);

        map.put("orderChannel",orderBean.priceChannel);
        map.put("serviceTime",orderBean.serviceTime);

        map.put("isCheckin",orderBean.isCheckin);
        map.put("checkInPrice",orderBean.checkInPrice);
        map.put("childSeat",orderBean.childSeatStr);

        map.put("flightNo", orderBean.flightNo);

        map.put("priceChannel",orderBean.priceChannel);



//        if(orderBean.flightBean != null){
//            map.put("flightFlyTimeL", orderBean.flightBean.depDate + " " + orderBean.flightBean.depTime + ":00");
//            map.put("flightArriveTimeL", orderBean.flightBean.arrDate + " " + orderBean.flightBean.arrivalTime + ":00");
//            map.put("flightAirportBuiding", orderBean.flightBean == null?null:orderBean.flightBean.depTerminal);
//            if(!TextUtils.isEmpty(orderBean.flightBean.flightNo)) {
//                map.put("flightNo", orderBean.flightBean.flightNo);
//            }
//            if(!TextUtils.isEmpty(orderBean.flightBean.arrivalAirportCode)) {
//                map.put("flightDestCode", orderBean.flightBean.arrivalAirportCode);
//            }
//            if(!TextUtils.isEmpty(orderBean.flightBean.arrAirportName)) {
//                map.put("flightDestName", orderBean.flightBean.arrAirportName);
//            }
//
//        }

    }
}
