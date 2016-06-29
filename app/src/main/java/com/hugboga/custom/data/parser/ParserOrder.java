package com.hugboga.custom.data.parser;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AppraisementBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.InsureListBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderContact;
import com.hugboga.custom.data.bean.OrderContactBean;
import com.hugboga.custom.data.bean.OrderPriceInfo;
import com.hugboga.custom.data.bean.OrderStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单bean
 * Created by admin on 2016/3/23.
 */
public class ParserOrder extends ImplParser {
    @Override
    public OrderBean parseObject(JSONObject jsonObj) throws Throwable {
        OrderBean orderbean = new OrderBean();
        orderbean.orderType = jsonObj.optInt("orderType");
        orderbean.luggageNum = jsonObj.optString("luggageNumber");
        orderbean.orderGoodsType = jsonObj.optInt("orderGoodsType", Constants.BUSINESS_TYPE_DAILY_LONG);
        orderbean.orderNo = jsonObj.optString("orderNo");
        orderbean.imcount = jsonObj.optInt("imCount");
        int status = jsonObj.optInt("orderStatus");
        orderbean.orderStatus = OrderStatus.getStateByCode(status);
        orderbean.serviceCityId = jsonObj.optInt("serviceCityId");
        orderbean.serviceCityName = jsonObj.optString("serviceCityName");
        orderbean.serviceEndCityid = jsonObj.optInt("serviceEndCityid");
        orderbean.serviceEndCityName = jsonObj.optString("serviceEndCityname");
        orderbean.carType = jsonObj.optInt("carTypeId");
        orderbean.seatCategory = jsonObj.optInt("carSeatNum");
        orderbean.carDesc = jsonObj.optString("carDesc");
        orderbean.serviceTime = jsonObj.optString("serviceTime");
        orderbean.serviceEndTime = jsonObj.optString("serviceEndTime");
        orderbean.serviceStartTime = jsonObj.optString("serviceRecTime");
        orderbean.startAddress = jsonObj.optString("startAddress");
        orderbean.startAddressDetail = jsonObj.optString("startAddressDetail");
        orderbean.startLocation = jsonObj.optString("startAddressPoi");
        orderbean.destAddress = jsonObj.optString("destAddress");
        orderbean.destAddressDetail = jsonObj.optString("destAddressDetail");
        orderbean.serviceAreaCode = jsonObj.optString("serviceAreaCode");
        orderbean.serviceAddressTel = jsonObj.optString("serviceAddressTel");
        orderbean.distance = jsonObj.optString("distance");
        orderbean.contactName = jsonObj.optString("userName");
        orderbean.brandSign = jsonObj.optString("flightBrandSign");
        orderbean.flightBrandSign = jsonObj.optString("flightBrandSign");
        orderbean.flightAirportCode = jsonObj.optString("flightAirportCode");
        orderbean.adult = jsonObj.optInt("adultNum");
        orderbean.child = jsonObj.optInt("childNum");
        orderbean.visa = jsonObj.optInt("isArrivalVisa");
        orderbean.memo = jsonObj.optString("userRemark");
        orderbean.flight = jsonObj.optString("flightNo");
        orderbean.cancelable = jsonObj.optBoolean("cancelable");
        orderbean.cancelText = jsonObj.optString("cancelText");
        orderbean.cancelTip = jsonObj.optString("cancelTip");
        orderbean.canComment = jsonObj.optBoolean("appraisable");
        orderbean.canChat = jsonObj.optBoolean("canChat");
        orderbean.imToken = jsonObj.optString("imToken");
        orderbean.payDeadTime = jsonObj.optString("payDeadTime");
        orderbean.totalDays = jsonObj.optInt("totalDays");
        orderbean.additionIsRead = jsonObj.optInt("additionIsRead");
        orderbean.lineSubject = jsonObj.optString("lineSubject");
        orderbean.lineDescription = jsonObj.optString("lineDescription");
        orderbean.insuranceEnable = jsonObj.optBoolean("insuranceEnable");
        orderbean.insuranceTips = jsonObj.optString("insuranceTips");
        orderbean.insuranceStatus = jsonObj.optString("insuranceStatus");
        orderbean.insuranceStatusCode = jsonObj.optInt("insuranceStatusCode");
        Gson gson = new Gson();
        orderbean.insuranceList = gson.fromJson(jsonObj.optString("insuranceList"), new TypeToken<List<InsureListBean>>(){}.getType());

        orderbean.realAreaCode = jsonObj.optString("realAreaCode");
        orderbean.realMobile = jsonObj.optString("realMobile");
        orderbean.realUserName = jsonObj.optString("realUserName");

        orderbean.isFlightSign = jsonObj.optString("isFlightSign");


        //passByCity
        JSONArray passByCityArray = jsonObj.optJSONArray("passCities");
        if (passByCityArray != null && passByCityArray.length() > 0) {
            orderbean.passByCity = new ArrayList<CityBean>(passByCityArray.length());
            ParserCity parserCity = new ParserCity();
            for (int i = 0; i < passByCityArray.length(); i++) {
                orderbean.passByCity.add(parserCity.parseObject(passByCityArray.optJSONObject(i)));
            }
        }

        //childSeat
        String childSeatStr = jsonObj.optString("childSeat");
        if (!TextUtils.isEmpty(childSeatStr)) {
            orderbean.childSeat = new ArrayList<String>();
            String[] childSeats = childSeatStr.split(",");
            for (String seat : childSeats) {
                orderbean.childSeat.add(seat);
            }
        }

        JSONArray cancelRules = jsonObj.optJSONArray("cancelRules");
        if (cancelRules != null && cancelRules.length() > 0) {
            orderbean.cancelRules = new ArrayList<String>(cancelRules.length());
            for (int i = 0; i < cancelRules.length(); i++) {
                orderbean.cancelRules.add(cancelRules.optString(i));
            }
        }

        //contact
        orderbean.contact = new ArrayList<OrderContact>();
        orderbean.cancelText = jsonObj.optString("cancelText");
        OrderContact oc = new OrderContact();
        oc.areaCode = jsonObj.optString("userAreaCode1");
        oc.tel = jsonObj.optString("userMobile1");
        if (!TextUtils.isEmpty(oc.tel))
            orderbean.contact.add(oc);
        if (jsonObj.has("userMobile2")) {
            oc = new OrderContact();
            oc.areaCode = jsonObj.optString("userAreaCode2");
            oc.tel = jsonObj.optString("userMobile2");
            if (!TextUtils.isEmpty(oc.tel))
                orderbean.contact.add(oc);
        }
        if (jsonObj.has("userMobile3")) {
            oc = new OrderContact();
            oc.areaCode = jsonObj.optString("userAreaCode3");
            oc.tel = jsonObj.optString("userMobile3");
            if (!TextUtils.isEmpty(oc.tel))
                orderbean.contact.add(oc);
        }
        //priceInfo
        orderbean.orderPriceInfo = new OrderPriceInfo();
        orderbean.orderPriceInfo.parser(jsonObj.optJSONObject("priceInfo"));
        // orderCoupon
        orderbean.orderCoupon = new ParserCouponBean().parseObject(jsonObj.optJSONObject("coupon"));
        //orderGuideInfo
        if (jsonObj.has("guideInfo")) {
            orderbean.orderGuideInfo = new ParserGuideInfo().parseObject(jsonObj.optJSONObject("guideInfo"));
        }
        if (jsonObj.has("appraisement")) {
            orderbean.assessmentBean = new ParserAssessment().parseObject(jsonObj.optJSONObject("appraisement"));
        }
        //2.2.0
        orderbean.isHalfDaily = jsonObj.optInt("halfDaily");
        orderbean.inTownDays = jsonObj.optInt("serviceLocalDays");
        orderbean.outTownDays = jsonObj.optInt("serviceNonlocalDays");
        orderbean.journeyComment = jsonObj.optString("journeyComment");
        orderbean.dailyTips = jsonObj.optString("dailyOrderTips");
        orderbean.dailyTips = jsonObj.optString("dailyOrderTips");
        orderbean.flightNo = jsonObj.optString("flightNo");
        orderbean.flightDeptCityName = jsonObj.optString("flightDeptCityName");
        orderbean.flightDestCityName = jsonObj.optString("flightDestCityName");
        orderbean.serviceTimeStr = jsonObj.optString("serviceTimeStr");
        orderbean.serviceEndTimeStr = jsonObj.optString("serviceEndTimeStr");
        orderbean.passengerInfos = jsonObj.optString("passengerInfos");
        orderbean.userCommentStatus = jsonObj.optInt("userCommentStatus");
        orderbean.realSendSms = jsonObj.optString("realSendSms");
        orderbean.isCheckin = jsonObj.optString("isCheckin");
        orderbean.isRealUser = jsonObj.optString("isRealUser");
        orderbean.priceCommentReward = jsonObj.optInt("priceCommentReward");
        orderbean.userList = gson.fromJson(jsonObj.optString("userList"), new TypeToken<List<OrderContactBean>>(){}.getType());
        orderbean.realUserList = gson.fromJson(jsonObj.optString("realUserList"), new TypeToken<List<OrderContactBean>>(){}.getType());
        orderbean.appraisement = gson.fromJson(jsonObj.optString("appraisement"), AppraisementBean.class);
        orderbean.carPool = jsonObj.optBoolean("carPool");
        orderbean.isIm = jsonObj.optInt("isIm") == 1;
        orderbean.isPhone = jsonObj.optInt("isPhone") == 1;
        orderbean.picUrl = jsonObj.optString("picUrl");
        orderbean.isChangeManual = jsonObj.optBoolean("isChangeManual");
        orderbean.hotelStatus = jsonObj.optInt("hotelStatus");
        orderbean.hotelRoom = jsonObj.optInt("hotelRoom");
        orderbean.hotelDays = jsonObj.optInt("hotelDays");
        orderbean.orderSource = jsonObj.optInt("orderSource");
        orderbean.skuDetailUrl = jsonObj.optString("skuDetailUrl");
        return orderbean;
    }
}