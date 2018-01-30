package com.hugboga.custom.data.parser;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AppraisementBean;
import com.hugboga.custom.data.bean.ChatBean;
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
import java.util.Iterator;
import java.util.List;

/**
 * 订单bean
 * Created by admin on 2016/3/23.
 */
public class ParserOrder extends ImplParser {
    @Override
    public OrderBean parseObject(JSONObject jsonObj) throws Throwable {
        Gson gson = new Gson();
        OrderBean orderbean = new OrderBean();

        orderbean.distance = jsonObj.optString("distance");

        orderbean.userRemark = jsonObj.optString("userRemark");
        orderbean.userName = jsonObj.optString("userName");

        orderbean.realAreaCode = jsonObj.optString("realAreaCode");
        orderbean.realMobile = jsonObj.optString("realMobile");
        orderbean.realUserName = jsonObj.optString("realUserName");
        orderbean.isRealUser = jsonObj.optString("isRealUser");
        orderbean.realSendSms = jsonObj.optString("realSendSms");
        orderbean.userList = gson.fromJson(jsonObj.optString("userList"), new TypeToken<List<OrderContactBean>>(){}.getType());
        orderbean.realUserList = gson.fromJson(jsonObj.optString("realUserList"), new TypeToken<List<OrderContactBean>>(){}.getType());
        orderbean.luggageNum = jsonObj.optString("luggageNumber");
        orderbean.userWechat = jsonObj.optString("userWechat");

        orderbean.goodsNo = jsonObj.optString("goodNo");
        orderbean.orderNo = jsonObj.optString("orderNo");
        orderbean.orderType = jsonObj.optInt("orderType");
        orderbean.orderGoodsType = jsonObj.optInt("orderGoodsType", Constants.BUSINESS_TYPE_DAILY_LONG);
        int status = jsonObj.optInt("orderStatus");
        orderbean.orderStatus = OrderStatus.getStateByCode(status);

        Integer twiceConfirm = jsonObj.optInt("twiceConfirm");
        orderbean.isTwiceConfirm = twiceConfirm != null ? twiceConfirm == 1 : false;
        Integer twiceCancelShowSpan = jsonObj.optInt("twiceCancelShowSpan");
        orderbean.isTwiceCancelShowSpan = twiceCancelShowSpan != null ? twiceCancelShowSpan == 1 : false;

        orderbean.payDeadTime = jsonObj.optString("payDeadTime");
        orderbean.cancelable = jsonObj.optBoolean("cancelable");
        orderbean.cancelText = jsonObj.optString("cancelText");
        orderbean.cancelTip = jsonObj.optString("cancelTip");
        orderbean.canComment = jsonObj.optBoolean("appraisable");
        orderbean.additionIsRead = jsonObj.optInt("additionIsRead");
        orderbean.travelFund = "" + jsonObj.optDouble("travelFund");
        orderbean.orderSource = jsonObj.optInt("orderSource");
        orderbean.skuDetailUrl = jsonObj.optString("skuDetailUrl");
        orderbean.isChangeManual = jsonObj.optInt("isChangeManual") == 1;
        orderbean.isIm = jsonObj.optInt("isIm") == 1;
        orderbean.isPhone = jsonObj.optInt("isPhone") == 1;
        JSONArray cancelRules = jsonObj.optJSONArray("cancelRules");
        if (cancelRules != null && cancelRules.length() > 0) {
            orderbean.cancelRules = new ArrayList<String>(cancelRules.length());
            for (int i = 0; i < cancelRules.length(); i++) {
                orderbean.cancelRules.add(cancelRules.optString(i));
            }
        }
        orderbean.priceActual = jsonObj.optString("priceActual");

        //orderbean.imcount = jsonObj.optInt("imCount");
        orderbean.imToken = jsonObj.optString("imToken");
        orderbean.canChat = jsonObj.optBoolean("canChat");

        orderbean.serviceCityId = jsonObj.optInt("serviceCityId");
        orderbean.serviceCityName = jsonObj.optString("serviceCityName");
        orderbean.serviceEndCityid = jsonObj.optInt("serviceEndCityid");
        orderbean.serviceEndCityName = jsonObj.optString("serviceEndCityname");
        orderbean.serviceAreaCode = jsonObj.optString("serviceAreaCode");
        orderbean.serviceAddressTel = jsonObj.optString("serviceAddressTel");
        orderbean.serviceTime = jsonObj.optString("serviceTime");
        orderbean.serviceEndTime = jsonObj.optString("serviceEndTime");
        orderbean.serviceStartTime = jsonObj.optString("serviceRecTime");
        orderbean.serviceTimeStr = jsonObj.optString("serviceTimeStr");
        orderbean.serviceEndTimeStr = jsonObj.optString("serviceEndTimeStr");

        orderbean.carId = jsonObj.optInt("carId");
        orderbean.carType = jsonObj.optInt("carTypeId");
        orderbean.carDesc = jsonObj.optString("carDesc");
        orderbean.seatCategory = jsonObj.optInt("carSeatNum");
        orderbean.adult = jsonObj.optInt("adultNum");
        orderbean.child = jsonObj.optInt("childNum");
        orderbean.passengerInfos = jsonObj.optString("passengerInfos");

        orderbean.childSeat = gson.fromJson(jsonObj.optString("childSeat"), OrderBean.ChildSeats.class);
        orderbean.childSeats = gson.fromJson(jsonObj.optString("childSeats"), OrderBean.ChildSeats.class);

        orderbean.totalDays = jsonObj.optInt("totalDays");
        orderbean.inTownDays = jsonObj.optInt("serviceLocalDays");
        orderbean.outTownDays = jsonObj.optInt("serviceNonlocalDays");
        orderbean.isHalfDaily = jsonObj.optInt("halfDaily");
        orderbean.oneCityTravel = jsonObj.optInt("oneCityTravel");
        orderbean.carPool = jsonObj.optBoolean("carPool");

        orderbean.startAddress = jsonObj.optString("startAddress");
        orderbean.startAddressDetail = jsonObj.optString("startAddressDetail");
        orderbean.startAddressPoi = jsonObj.optString("startAddressPoi");
        orderbean.destAddress = jsonObj.optString("destAddress");
        orderbean.destAddressDetail = jsonObj.optString("destAddressDetail");
        orderbean.destAddressPoi = jsonObj.optString("destAddressPoi");

        orderbean.flightAirportBuiding = jsonObj.optString("flightAirportBuiding");
        orderbean.flightAirportCode = jsonObj.optString("flightAirportCode");
        orderbean.brandSign = jsonObj.optString("flightBrandSign");
        orderbean.flightBrandSign = jsonObj.optString("flightBrandSign");
        orderbean.flightDestCode = jsonObj.optString("flightDestCode");
        orderbean.flightDestName = jsonObj.optString("flightDestName");
        orderbean.flight = jsonObj.optString("flightNo");
        orderbean.flightNo = jsonObj.optString("flightNo");
        orderbean.flightDeptCityName = jsonObj.optString("flightDeptCityName");
        orderbean.flightDestCityName = jsonObj.optString("flightDestCityName");
        orderbean.visa = jsonObj.optInt("isArrivalVisa");
        orderbean.priceFlightBrandSign = "" + jsonObj.optDouble("priceFlightBrandSign");
        orderbean.isFlightSign = jsonObj.optString("isFlightSign");
        orderbean.isCheckin = jsonObj.optString("isCheckin");
        orderbean.goodsOtherPrice = jsonObj.optDouble("goodsOtherPrice", 0);
        orderbean.goodsOtherPriceComment = jsonObj.optString("goodsOtherPriceComment");

        orderbean.insuranceEnable = jsonObj.optBoolean("insuranceEnable");
        orderbean.insuranceTips = jsonObj.optString("insuranceTips");
        orderbean.insuranceStatus = jsonObj.optString("insuranceStatus");
        orderbean.insuranceStatusCode = jsonObj.optInt("insuranceStatusCode");

        JSONObject insuranceMapBean = jsonObj.optJSONObject("insuranceMap");
        if (insuranceMapBean != null) {
            List<List<InsureListBean>> insuranceList = new ArrayList<>();
            Iterator<String> keys = insuranceMapBean.keys();
            for (; keys.hasNext(); ) {
                String key = keys.next();
                List<InsureListBean> itemList = gson.fromJson(insuranceMapBean.optString(key), new TypeToken<List<InsureListBean>>(){}.getType());
                insuranceList.add(itemList);
            }
            orderbean.insuranceMap = insuranceList;
        }

        JSONArray passByCityArray = jsonObj.optJSONArray("passCities");
        if (passByCityArray != null && passByCityArray.length() > 0) {
            orderbean.passByCity = new ArrayList<CityBean>(passByCityArray.length());
            ParserCity parserCity = new ParserCity();
            for (int i = 0; i < passByCityArray.length(); i++) {
                orderbean.passByCity.add(parserCity.parseObject(passByCityArray.optJSONObject(i)));
            }
        }
        orderbean.lineSubject = jsonObj.optString("lineSubject");
        orderbean.lineDescription = jsonObj.optString("lineDescription");
        orderbean.picUrl = jsonObj.optString("picUrl");
        orderbean.journeyComment = jsonObj.optString("journeyComment");
        orderbean.dailyTips = jsonObj.optString("dailyOrderTips");

        orderbean.hotelStatus = jsonObj.optInt("hotelStatus");
        orderbean.hotelRoom = jsonObj.optInt("hotelRoom");
        orderbean.hotelDays = jsonObj.optInt("hotelDays");

        orderbean.isShowBargain = jsonObj.optInt("isShowBargain");
        orderbean.bargainSeconds = jsonObj.optLong("bargainSeconds");
        orderbean.bargainStatus = jsonObj.optInt("bargainStatus");
        orderbean.bargainAmount = jsonObj.optDouble("bargainAmount");

        orderbean.guideCollectId = jsonObj.optString("guideCollectId");
        orderbean.userCommentStatus = jsonObj.optInt("userCommentStatus");
        orderbean.priceCommentReward = jsonObj.optInt("priceCommentReward");
        if (jsonObj.has("appraisement")) {
            orderbean.assessmentBean = new ParserAssessment().parseObject(jsonObj.optJSONObject("appraisement"));
        }
        orderbean.appraisement = gson.fromJson(jsonObj.optString("appraisement"), AppraisementBean.class);
        orderbean.guideAgencyDriverId = jsonObj.optString("guideAgencyDriverId");
        orderbean.guideAgencyType = jsonObj.optInt("guideAgencyType");

        //priceInfo
        orderbean.orderPriceInfo = new OrderPriceInfo();
        orderbean.orderPriceInfo.parser(jsonObj.optJSONObject("priceInfo"));
        //orderGuideInfo
        if (jsonObj.has("guideInfo")) {
            orderbean.orderGuideInfo = new ParserGuideInfo().parseObject(jsonObj.optJSONObject("guideInfo"));
        }
        if(jsonObj.has("imInfo")){
            JSONObject jsonObject = jsonObj.optJSONObject("imInfo");
            if(jsonObject!=null){
                orderbean.imInfo = new ChatBean();
                orderbean.imInfo.parseObject(jsonObject);
//                if(orderbean.orderGuideInfo!=null){
//                    orderbean.orderGuideInfo.guideImId = jsonObject.optString("neTargetId");
//                }
            }
        }

        //contact
        orderbean.contact = new ArrayList<OrderContact>();
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
        if (jsonObj.has("subOrderDetail")) {
            orderbean.subOrderDetail = new ParserSubOrderDetail().parseObject(jsonObj.optJSONObject("subOrderDetail"));
        }
        orderbean.orderJourneyCount = jsonObj.optInt("orderJourneyCount");

        if (jsonObj.has("subOrderGuideAvartar")) {
            JSONArray guideAvartarList = jsonObj.optJSONArray("subOrderGuideAvartar");
            if (guideAvartarList != null && guideAvartarList.length() > 0) {
                orderbean.subOrderGuideAvartar = new ArrayList<String>(guideAvartarList.length());
                for (int i = 0; i < guideAvartarList.length(); i++) {
                    orderbean.subOrderGuideAvartar.add(guideAvartarList.optString(i));
                }
            }
        }
        orderbean.journeyList = gson.fromJson(jsonObj.optString("journeyList"), new TypeToken<List<OrderBean.JourneyItem>>(){}.getType());

        orderbean.deliverCategory = jsonObj.optInt("deliverCategory");

        orderbean.fxJourneyInfo = gson.fromJson(jsonObj.optString("fxJourneyInfo"), OrderBean.FxJourneyInfoBean.class);
        return orderbean;
    }
}