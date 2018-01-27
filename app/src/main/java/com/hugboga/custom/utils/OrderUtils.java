package com.hugboga.custom.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarAdditionalServicePrice;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.ContactUsersBean;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.MostFitBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderContact;
import com.hugboga.custom.data.bean.OrderPriceInfo;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.net.UrlLibs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderUtils {

    private String getUserExJson(ContactUsersBean contactUsersBean) {
        StringBuffer userExJson = new StringBuffer();
        userExJson.append("[");
        if (!TextUtils.isEmpty(contactUsersBean.userPhone)) {
            userExJson.append("{name:\"" + contactUsersBean.userName + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(contactUsersBean.phoneCode) + "\",mobile:\"" + contactUsersBean.userPhone + "\"}");
        }
        if (!TextUtils.isEmpty(contactUsersBean.user1Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user1Name + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(contactUsersBean.phone1Code) + "\",mobile:\"" + contactUsersBean.user1Phone + "\"}");
        }
        if (!TextUtils.isEmpty(contactUsersBean.user2Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user2Name + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(contactUsersBean.phone2Code) + "\",mobile:\"" + contactUsersBean.user2Phone + "\"}");
        }
        userExJson.append("]");
        return userExJson.toString();
    }

    private String getRealUserExJson(ContactUsersBean contactUsersBean) {
        StringBuffer realUserExJson = new StringBuffer();
        realUserExJson.append("[");

        if (!TextUtils.isEmpty(contactUsersBean.otherName)) {
            realUserExJson.append("{name:\"" + contactUsersBean.otherName + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(contactUsersBean.otherphoneCode) + "\",mobile:\"" + contactUsersBean.otherPhone + "\"}");
        }
        realUserExJson.append("]");
        return realUserExJson.toString();
    }

    public static int getSeat1PriceTotal(CarListBean carListBean, ManLuggageBean manLuggageBean) {
        int seat1Price = 0;
        if (null != carListBean && null != carListBean.additionalServicePrice && null != carListBean.additionalServicePrice.childSeatPrice1) {
            seat1Price = Integer.valueOf(carListBean.additionalServicePrice.childSeatPrice1);
        }
        int seat1PriceTotal = seat1Price * getSeat1Count(manLuggageBean);
        return seat1PriceTotal;
    }

    public static int getSeat2PriceTotal(CarAdditionalServicePrice additionalServicePrice, ManLuggageBean manLuggageBean) {
        int seat2Price = 0;

        if (null != additionalServicePrice && null != additionalServicePrice.childSeatPrice2) {
            seat2Price = Integer.valueOf(additionalServicePrice.childSeatPrice2);
        }
        int seat2PriceTotal = seat2Price * getSeat2Count(manLuggageBean);
        return seat2PriceTotal;
    }

    public static int getSeat1PriceTotal(CarAdditionalServicePrice additionalServicePrice, ManLuggageBean manLuggageBean) {
        int seat1Price = 0;
        if (null != additionalServicePrice && null != additionalServicePrice.childSeatPrice1) {
            seat1Price = Integer.valueOf(additionalServicePrice.childSeatPrice1);
        }
        int seat1PriceTotal = seat1Price * getSeat1Count(manLuggageBean);
        return seat1PriceTotal;
    }

    public static int getSeat2PriceTotal(CarListBean carListBean, ManLuggageBean manLuggageBean) {
        int seat2Price = 0;

        if (null != carListBean && null != carListBean.additionalServicePrice && null != carListBean.additionalServicePrice.childSeatPrice2) {
            seat2Price = Integer.valueOf(carListBean.additionalServicePrice.childSeatPrice2);
        }
        int seat2PriceTotal = seat2Price * getSeat2Count(manLuggageBean);
        return seat2PriceTotal;
    }

    private String getChileSeatJson(CarListBean carListBean, ManLuggageBean manLuggageBean) {
        int seat1Price = 0;
        int seat2Price = 0;
        if (null != carListBean && null != carListBean.additionalServicePrice && null != carListBean.additionalServicePrice.childSeatPrice1) {
            seat1Price = Integer.valueOf(carListBean.additionalServicePrice.childSeatPrice1);
        }
        if (null != carListBean && null != carListBean.additionalServicePrice && null != carListBean.additionalServicePrice.childSeatPrice2) {
            seat2Price = Integer.valueOf(carListBean.additionalServicePrice.childSeatPrice2);
        }
        StringBuffer childSeat = new StringBuffer();
        childSeat.append("{");
        childSeat.append("\"").append("childSeatPrice1\":" + seat1Price + ",");
        childSeat.append("\"").append("childSeatPrice2\":" + seat2Price + ",");
        childSeat.append("\"").append("childSeatPrice1Count\":" + getSeat1Count(manLuggageBean) + ",");
        childSeat.append("\"").append("childSeatPrice2Count\":" + getSeat2Count(manLuggageBean) + "");
        childSeat.append("}");
        return childSeat.toString();
    }


    public static int getSeat1Count(ManLuggageBean manLuggageBean) {
        if (null == manLuggageBean) {
            return 0;
        }
        return (manLuggageBean.childSeats >= 1 ? 1 : 0);
    }

    public static int getSeat2Count(ManLuggageBean manLuggageBean) {
        if (null == manLuggageBean) {
            return 0;
        }
        return (manLuggageBean.childSeats >= 1 ? (manLuggageBean.childSeats - 1) : 0);
    }

    public static int getSeat1Count(int childSeats) {
        return (childSeats >= 1 ? 1 : 0);
    }

    public static int getSeat2Count(int childSeats) {
        return (childSeats >= 1 ? (childSeats - 1) : 0);
    }

    private String getServiceEndTime(String date, int day) {
        try {
            String[] ymd = date.split("-");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(ymd[0]), Integer.valueOf(ymd[1]) - 1, Integer.valueOf(ymd[2]));
            calendar.add(Calendar.DAY_OF_YEAR, day);
            return DateUtils.dateDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            MLog.e("解析时间格式错误", e);
        }
        return null;
    }


    //包车参数
    public OrderBean getDayOrderByInput(String adultNum, CarBean carBean,
                                        String childrenNum, String endCityId,
                                        CityBean endBean, List<OrderContact> contact,
                                        String serverTime, String startDate,
                                        String endDate, int outNum, int inNum,
                                        String serviceAddressTel, String serviceAreaCode,
                                        CityBean startBean, boolean isHalfTravel,
                                        String startAddress, String destAddressDetail,
                                        String userName, String passCities, String userRemark,
                                        String childseatNum, String luggageNum,
                                        ContactUsersBean contactUsersBean,
                                        boolean dreamLeftIscheck, String travelFund,
                                        CouponBean couponBean, MostFitBean mostFitBean,
                                        String guideCollectId, PoiBean poiBean) {
        OrderBean orderBean = new OrderBean();//订单
        orderBean.adult = Integer.valueOf(adultNum);
        orderBean.carDesc = carBean.carDesc;
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.carType = carBean.carType;
        orderBean.child = Integer.valueOf(childrenNum);
        orderBean.destAddress = "";
        orderBean.destAddressDetail = "";
        orderBean.priceMark = carBean.pricemark;
        orderBean.contact = contact;
        orderBean.serviceStartTime = serverTime + ":00";
        orderBean.serviceTime = startDate;
        orderBean.serviceEndTime = endDate;
        orderBean.outTownDays = outNum;
        orderBean.inTownDays = inNum;
        orderBean.oneCityTravel = outNum == 0 ? 1 : 2;//1：市内畅游  2：跨城市
        orderBean.serviceAddressTel = serviceAddressTel;
        orderBean.serviceAreaCode = CommonUtils.removePhoneCodeSign(serviceAreaCode);
        orderBean.orderType = 1;
        orderBean.serviceCityId = startBean.cityId;
        orderBean.serviceEndCityid = endBean.cityId;
        orderBean.serviceCityName = startBean.name;
        orderBean.serviceEndCityName = endBean.name;
        orderBean.totalDays = (inNum + outNum);
        orderBean.isHalfDaily = isHalfTravel ? 1 : 0;

        orderBean.carId = carBean.carId;
        orderBean.capOfPerson = carBean.capOfPerson;
        orderBean.special = carBean.special;
        orderBean.urgentFlag = carBean.urgentFlag;

        if (null != poiBean) {
            orderBean.startAddress = poiBean.placeName;//upRight.getText().toString();
            orderBean.startAddressDetail = poiBean.placeDetail;//upSiteText.getText().toString();
        } else {
            orderBean.startAddress = "";
            orderBean.startAddressDetail = "";
        }


        orderBean.userName = userName;//manName.getText().toString();
        orderBean.stayCityListStr = passCities;
        orderBean.userRemark = userRemark;//mark.getText().toString();
        orderBean.serviceDepartTime = serverTime;
        orderBean.priceChannel = carBean.price + "";
        orderBean.childSeatNum = childseatNum;
        orderBean.luggageNum = luggageNum;
        orderBean.realUserName = contactUsersBean.otherName;
        orderBean.realAreaCode = CommonUtils.removePhoneCodeSign(contactUsersBean.otherphoneCode);
        orderBean.realMobile = contactUsersBean.otherPhone;


        if (contactUsersBean.isForOther) {
            orderBean.isRealUser = "2";
        } else {
            orderBean.isRealUser = "1";
        }
        orderBean.realSendSms = contactUsersBean.isSendMessage ? "1" : "0";
        if (dreamLeftIscheck) {
            orderBean.travelFund = travelFund;
            orderBean.orderPrice = carBean.price;
        } else {
            if (null == couponBean && null != mostFitBean) {
                orderBean.coupId = mostFitBean.couponId;
                orderBean.coupPriceInfo = mostFitBean.couponPrice + "";
                orderBean.orderPrice = carBean.price;
                if (null != mostFitBean && null != mostFitBean.actualPrice && mostFitBean.actualPrice != 0) {
                    orderBean.priceActual = mostFitBean.actualPrice + "";
                } else {
                    orderBean.priceActual = "0";
                }
            } else if (null != couponBean && null == mostFitBean) {
                orderBean.coupId = couponBean.couponID;
                orderBean.coupPriceInfo = couponBean.price;
                orderBean.orderPrice = carBean.price;
                if (couponBean.actualPrice != 0) {
                    orderBean.priceActual = couponBean.actualPrice + "";
                } else {
                    orderBean.priceActual = "0";
                }
            }
        }
        if (!TextUtils.isEmpty(guideCollectId)) {
            orderBean.guideCollectId = guideCollectId;
        }

        orderBean.userEx = getUserExJson(contactUsersBean);
        orderBean.realUserEx = getRealUserExJson(contactUsersBean);

        ManLuggageBean manLuggageBean = new ManLuggageBean();
        manLuggageBean.childSeats = Integer.valueOf(childseatNum);
        manLuggageBean.childs = Integer.valueOf(childrenNum);


        if (manLuggageBean.childSeats != 0) {
            orderBean.orderPrice = carBean.price;
            orderBean.priceChannel = carBean.price + "";
            orderBean.childSeatStr = getChileSeatJson(null, manLuggageBean);
        } else {
            orderBean.orderPrice = carBean.price;
            orderBean.childSeatStr = "";
            orderBean.priceChannel = carBean.price + "";
        }

        return orderBean;
    }

    public OrderBean getPickOrderByInput(FlightBean flightBean, PoiBean poiBean,
                                         CarBean carBean, String brandSign,
                                         CarListBean carListBean, String pickName,
                                         String adultNum, String childrenNum, String distance,
                                         String serviceAddressTel, String serviceAreaCode,
                                         String userName, String passCities, String userRemark,
                                         String serverTime, String childseatNum, String luggageNum,
                                         ContactUsersBean contactUsersBean, boolean dreamLeftIscheck,
                                         String travelFund, CouponBean couponBean, MostFitBean mostFitBean,
                                         String guideCollectId, ManLuggageBean manLuggageBean, boolean isCheckIn, String userWechat) {
        OrderBean orderBean = new OrderBean();//订单
        orderBean.orderType = 1;
        orderBean.flight = flightBean.flightNo;
        orderBean.flightBean = flightBean;
        orderBean.startAddress = flightBean.arrAirportName;
        //出发地，到达地经纬度
        orderBean.startAddressPoi = flightBean.arrLocation;
        orderBean.terminalLocation = poiBean.location;
        orderBean.carDesc = carBean.carDesc;
        orderBean.destAddress = poiBean.placeName;
        orderBean.destAddressDetail = poiBean.placeDetail;
        orderBean.serviceCityId = flightBean.arrCityId;
        orderBean.serviceTime = flightBean.arrDate + " " + flightBean.arrivalTime + ":00";
        orderBean.brandSign = brandSign;//pickName.getText().toString();
        orderBean.flightNo = flightBean.flightNo;
        orderBean.flightFlyTimeL = flightBean.arrDate;
        orderBean.flightArriveTimeL = flightBean.arrivalTime;
        orderBean.flightAirportBuiding = flightBean.arrTerminal;
        orderBean.flightAirportCode = flightBean.arrAirportCode;
        orderBean.flightAirportName = flightBean.arrAirportName;
        orderBean.flightDestCode = flightBean.depAirportCode;
        orderBean.flightDestName = flightBean.depAirportName;
        int pickupPrice = 0;
        if (null != carListBean.additionalServicePrice && null != carListBean.additionalServicePrice.pickupSignPrice) {
            pickupPrice = Integer.valueOf(carListBean.additionalServicePrice.pickupSignPrice);
            orderBean.isFlightSign = isCheckIn ? "1" : "0";
        } else {
            orderBean.isFlightSign = "0";
        }
        orderBean.priceFlightBrandSign = pickupPrice + "";

        orderBean.flightBrandSign = pickName;//pickName.getText().toString();
        orderBean.adult = Integer.valueOf(adultNum);
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.carType = carBean.carType;
        orderBean.child = Integer.valueOf(childrenNum);
        orderBean.distance = distance;
        orderBean.priceMark = carBean.pricemark;
        orderBean.serviceAddressTel = serviceAddressTel;//hotelPhoneText.getText().toString();
        orderBean.serviceAreaCode = CommonUtils.removePhoneCodeSign(serviceAreaCode);//hotelPhoneTextCodeClick.getText().toString();
        orderBean.userName = userName;//manName.getText().toString();
        orderBean.stayCityListStr = passCities;
        orderBean.userRemark = userRemark;//mark.getText().toString();
        orderBean.serviceDepartTime = serverTime;
        orderBean.priceChannel = carBean.price + "";
        orderBean.childSeatNum = childseatNum;
        orderBean.luggageNum = luggageNum;
        orderBean.realUserName = contactUsersBean.otherName;
        orderBean.realAreaCode = CommonUtils.removePhoneCodeSign(contactUsersBean.otherphoneCode);
        orderBean.realMobile = contactUsersBean.otherPhone;
        orderBean.isCheckin = isCheckIn ? "1" : "0";
        orderBean.userWechat = userWechat;

        orderBean.carId = carBean.carId;
        orderBean.guideCarId = carBean.id;
        orderBean.capOfPerson = carBean.capOfPerson;
        orderBean.special = carBean.special;
        orderBean.urgentFlag = carBean.urgentFlag;

        if (contactUsersBean.isForOther) {
            orderBean.isRealUser = "2";
        } else {
            orderBean.isRealUser = "1";
        }
        orderBean.realSendSms = contactUsersBean.isSendMessage ? "1" : "0";
        if (dreamLeftIscheck) {
            orderBean.travelFund = travelFund;
            orderBean.orderPrice = carBean.price;
        } else {
            if (null == couponBean && null != mostFitBean) {
                orderBean.coupId = mostFitBean.couponId;
                orderBean.coupPriceInfo = mostFitBean.couponPrice + "";
                orderBean.orderPrice = carBean.price;
                if (null != mostFitBean && null != mostFitBean.actualPrice && mostFitBean.actualPrice != 0) {
                    orderBean.priceActual = mostFitBean.actualPrice + "";
                } else {
                    orderBean.priceActual = "0";
                }
            } else if (null != couponBean && null == mostFitBean) {
                orderBean.coupId = couponBean.couponID;
                orderBean.coupPriceInfo = couponBean.price;
                orderBean.orderPrice = carBean.price;
                if (couponBean.actualPrice != 0) {
                    orderBean.priceActual = couponBean.actualPrice + "";
                } else {
                    orderBean.priceActual = "0";
                }
            }
        }
        orderBean.expectedCompTime = carBean.expectedCompTime;
        if (!TextUtils.isEmpty(guideCollectId)) {
            orderBean.guideCollectId = guideCollectId;
        }
        orderBean.userEx = getUserExJson(contactUsersBean);
        orderBean.realUserEx = getRealUserExJson(contactUsersBean);
        if (null == carListBean.additionalServicePrice.childSeatPrice1
                && null == carListBean.additionalServicePrice.childSeatPrice2) {
            orderBean.orderPrice = carBean.price;
            orderBean.childSeatStr = "";
            orderBean.priceChannel = carBean.price + "";
        } else {
            if (manLuggageBean.childSeats != 0) {
                orderBean.orderPrice = carBean.price + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean);
                orderBean.priceChannel = (carBean.price + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean)) + "";
                orderBean.childSeatStr = getChileSeatJson(carListBean, manLuggageBean);
            } else {
                orderBean.orderPrice = carBean.price;
                orderBean.childSeatStr = "";
                orderBean.priceChannel = carBean.price + "";
            }
        }

        orderBean.orderPrice = isCheckIn ?
                (carBean.price + pickupPrice) + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean)
                : carBean.price + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean);
        orderBean.priceChannel = isCheckIn ?
                "" + (carBean.price + pickupPrice + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean))
                : "" + (carBean.price + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean));

        if (carListBean.isSeckills) {
            orderBean.isSeckills = true;
            orderBean.priceTicket = orderBean.orderPrice;
            orderBean.priceActual = carBean.seckillingPrice + "";
            orderBean.priceChannel = carBean.seckillingPrice + "";
            orderBean.orderPrice = carBean.seckillingPrice;
            orderBean.timeLimitedSaleNo = carListBean.timeLimitedSaleNo;
            orderBean.timeLimitedSaleScheduleNo = carListBean.timeLimitedSaleScheduleNo;
        }
        return orderBean;
    }


    public OrderBean getSingleOrderByInput(String adultNum, CarBean carBean,
                                           String childrenNum, String endCityId,
                                           String startCityId, List<OrderContact> contact,
                                           String serverTime, String startCityName,
                                           String serverDate,
                                           PoiBean startPoi, PoiBean endPoi, String distance,
                                           String serviceAddressTel, String serviceAreaCode,
                                           CarListBean carListBean,
                                           ManLuggageBean manLuggageBean,
                                           String userName, String passCities, String userRemark,
                                           String childseatNum, String luggageNum,
                                           ContactUsersBean contactUsersBean,
                                           boolean dreamLeftIscheck, String travelFund,
                                           CouponBean couponBean, MostFitBean mostFitBean,
                                           String guideCollectId, String userWechat) {
        OrderBean orderBean = new OrderBean();//订单
        orderBean.orderType = 4;
        orderBean.adult = Integer.valueOf(adultNum);
        orderBean.carDesc = carBean.carDesc;
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.carType = carBean.carType;
        orderBean.child = Integer.valueOf(childrenNum);
        orderBean.serviceCityId = Integer.valueOf(startCityId);
        orderBean.serviceEndCityid = Integer.valueOf(endCityId);
        orderBean.serviceCityName = startCityName;
        orderBean.serviceEndCityName = endCityId;
        orderBean.contact = contact;
        orderBean.serviceStartTime = serverTime + ":00";
        orderBean.serviceTime = serverDate + " " + serverTime + ":00";

        orderBean.carId = carBean.carId;
        orderBean.guideCarId = carBean.id;
        orderBean.capOfPerson = carBean.capOfPerson;
        orderBean.special = carBean.special;
        orderBean.urgentFlag = carBean.urgentFlag;

        orderBean.startAddress = startPoi.placeName;
        orderBean.startAddressDetail = startPoi.placeDetail;
        orderBean.startAddressPoi = startPoi.location;

        orderBean.destAddress = endPoi.placeName;
        orderBean.destAddressDetail = endPoi.placeDetail;
        orderBean.terminalLocation = endPoi.location;
        orderBean.distance = distance;

        orderBean.priceMark = carBean.pricemark;

        orderBean.serviceAddressTel = serviceAddressTel;//hotelPhoneText.getText().toString();
        orderBean.serviceAreaCode = CommonUtils.removePhoneCodeSign(serviceAreaCode);//hotelPhoneTextCodeClick.getText().toString();


        orderBean.userName = userName;//manName.getText().toString();
        orderBean.stayCityListStr = passCities;
        orderBean.userRemark = userRemark;//mark.getText().toString();
        orderBean.userWechat = userWechat;

        orderBean.serviceDepartTime = serverTime;

        orderBean.childSeatNum = childseatNum;
        orderBean.luggageNum = luggageNum;

        orderBean.realUserName = contactUsersBean.otherName;
        orderBean.realAreaCode = CommonUtils.removePhoneCodeSign(contactUsersBean.otherphoneCode);
        orderBean.realMobile = contactUsersBean.otherPhone;
        if (contactUsersBean.isForOther) {
            orderBean.isRealUser = "2";
        } else {
            orderBean.isRealUser = "1";
        }
        orderBean.realSendSms = contactUsersBean.isSendMessage ? "1" : "0";

        if (dreamLeftIscheck) {
            orderBean.travelFund = travelFund;
        } else {
            if (null == couponBean && null != mostFitBean) {
                orderBean.coupId = mostFitBean.couponId;
                orderBean.coupPriceInfo = mostFitBean.couponPrice + "";
                if (null != mostFitBean && null != mostFitBean.actualPrice && mostFitBean.actualPrice != 0) {
                    orderBean.priceActual = mostFitBean.actualPrice + "";
                } else {
                    orderBean.priceActual = "0";
                }
            } else if (null != couponBean && null == mostFitBean) {
                orderBean.coupId = couponBean.couponID;
                orderBean.coupPriceInfo = couponBean.price;
                if (couponBean.actualPrice != 0) {
                    orderBean.priceActual = couponBean.actualPrice + "";
                } else {
                    orderBean.priceActual = "0";
                }
            }
        }
        orderBean.expectedCompTime = carBean.expectedCompTime;
        if (!TextUtils.isEmpty(guideCollectId)) {
            orderBean.guideCollectId = guideCollectId;
        }
        orderBean.userEx = getUserExJson(contactUsersBean);
        orderBean.realUserEx = getRealUserExJson(contactUsersBean);
        orderBean.orderPrice = carBean.price;
        if (null == carListBean.additionalServicePrice.childSeatPrice1
                && null == carListBean.additionalServicePrice.childSeatPrice2) {
            orderBean.orderPrice = carBean.price;
            orderBean.childSeatStr = "";
            orderBean.priceChannel = carBean.price + "";
        } else {
            if (manLuggageBean.childSeats != 0) {
                orderBean.orderPrice = carBean.price + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean);
                orderBean.priceChannel = (carBean.price + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean)) + "";
                orderBean.childSeatStr = getChileSeatJson(carListBean, manLuggageBean);
            } else {
                orderBean.orderPrice = carBean.price;
                orderBean.childSeatStr = "";
                orderBean.priceChannel = carBean.price + "";
            }
        }
//        orderBean.orderPrice = carBean.price + getSeat1PriceTotal(carListBean,manLuggageBean) + getSeat2PriceTotal(carListBean,manLuggageBean);
        orderBean.priceFlightBrandSign = "";
        return orderBean;
    }

    public OrderBean getSendOrderByInput(PoiBean poiBean,
                                         CarBean carBean, String contactName,
                                         boolean isCheckIn, String flightNo, AirPort airPort,
                                         CarListBean carListBean, String serverDate,
                                         boolean dreamLeftischeck,
                                         String adultNum, String childrenNum,
                                         String serviceAddressTel, String serviceAreaCode, String userRemark,
                                         String serverTime, String childseatNum, String luggageNum,
                                         ContactUsersBean contactUsersBean,
                                         String travelFund, CouponBean couponBean, MostFitBean mostFitBean,
                                         String guideCollectId, ManLuggageBean manLuggageBean, String userWechat) {
        OrderBean orderBean = new OrderBean();//订单
        orderBean.orderType = 2;
        orderBean.serviceAreaCode = CommonUtils.removePhoneCodeSign(serviceAreaCode);//hotelPhoneTextCodeClick.getText().toString();
        orderBean.serviceAddressTel = serviceAddressTel;//hotelPhoneText.getText().toString();
        orderBean.urgentFlag = carBean.urgentFlag;
        if (!TextUtils.isEmpty(guideCollectId)) {
            orderBean.guideCollectId = guideCollectId;
        }
        orderBean.carType = carBean.carType;
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.carDesc = carBean.carDesc;
        orderBean.userName = contactName;//manName.getText().toString();
        orderBean.userRemark = userRemark;//mark.getText().toString().trim();
        orderBean.childSeatNum = childseatNum;
        orderBean.luggageNum = luggageNum;
        orderBean.flightNo = flightNo;//airportName.getText().toString();
        orderBean.expectedCompTime = carBean.expectedCompTime;
        orderBean.destAddressPoi = airPort.location;
        orderBean.destAddressDetail = null;//poiBean.placeDetail;
        orderBean.startAddress = poiBean.placeName;
        orderBean.startAddressDetail = poiBean.placeDetail;
        //出发地，到达地经纬度
        orderBean.startAddressPoi = poiBean.location;
        orderBean.terminalLocation = poiBean.location;
        orderBean.priceMark = carBean.pricemark;
        orderBean.destAddress = airPort.airportName;
        orderBean.flightAirportCode = airPort.airportCode;
        orderBean.serviceCityId = airPort.cityId;
        orderBean.serviceTime = serverDate + " " + serverTime + ":00";
        orderBean.adult = Integer.valueOf(adultNum);
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.carType = carBean.carType;
        orderBean.child = Integer.valueOf(childrenNum);
        orderBean.distance = carListBean.distance + "";
        orderBean.realUserName = contactUsersBean.otherName;
        orderBean.realAreaCode = CommonUtils.removePhoneCodeSign(contactUsersBean.otherphoneCode);
        orderBean.realMobile = contactUsersBean.otherPhone;
        orderBean.userWechat = userWechat;

        orderBean.carId = carBean.carId;
        orderBean.guideCarId = carBean.id;
        orderBean.capOfPerson = carBean.capOfPerson;
        orderBean.special = carBean.special;

        int checkInPrice = 0;
        if (null != carListBean.additionalServicePrice && null != carListBean.additionalServicePrice.checkInPrice) {
            checkInPrice = Integer.valueOf(carListBean.additionalServicePrice.checkInPrice);
            orderBean.isCheckin = isCheckIn ? "1" : "0";
        } else {
            orderBean.isCheckin = "0";
        }
        orderBean.checkInPrice = checkInPrice;


        if (contactUsersBean.isForOther) {
            orderBean.isRealUser = "2";
        } else {
            orderBean.isRealUser = "1";
        }
        orderBean.realSendSms = contactUsersBean.isSendMessage ? "1" : "0";

        if (dreamLeftischeck) {
            orderBean.travelFund = travelFund;
            orderBean.orderPrice = carBean.price;
        } else {
            if (null == couponBean && null != mostFitBean) {
                orderBean.coupId = mostFitBean.couponId;
                orderBean.coupPriceInfo = mostFitBean.couponPrice + "";
                orderBean.orderPrice = carBean.price;
                if (null != mostFitBean && null != mostFitBean.actualPrice && mostFitBean.actualPrice != 0) {
                    orderBean.priceActual = mostFitBean.actualPrice + "";
                } else {
                    orderBean.priceActual = "0";
                }
            } else if (null != couponBean && null == mostFitBean) {
                orderBean.coupId = couponBean.couponID;
                orderBean.coupPriceInfo = couponBean.price;
                orderBean.orderPrice = carBean.price;
                if (couponBean.actualPrice != 0) {
                    orderBean.priceActual = couponBean.actualPrice + "";
                } else {
                    orderBean.priceActual = "0";
                }
            }
        }
        orderBean.userEx = getUserExJson(contactUsersBean);
        orderBean.realUserEx = getRealUserExJson(contactUsersBean);

        if (null == carListBean.additionalServicePrice.childSeatPrice1
                && null == carListBean.additionalServicePrice.childSeatPrice2) {
            orderBean.orderPrice = carBean.price;
            orderBean.childSeatStr = "";
            orderBean.priceChannel = carBean.price + "";
        } else {
            if (manLuggageBean.childSeats != 0) {
                orderBean.orderPrice = carBean.price + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean);
                orderBean.priceChannel = (carBean.price + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean)) + "";
                orderBean.childSeatStr = getChileSeatJson(carListBean, manLuggageBean);
            } else {
                orderBean.orderPrice = carBean.price;
                orderBean.childSeatStr = "";
                orderBean.priceChannel = carBean.price + "";
            }
        }
        orderBean.orderPrice = isCheckIn ?
                carBean.price + checkInPrice + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean)
                : carBean.price + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean);
//        orderBean.checkInPrice = isCheckIn ? Integer.valueOf(carListBean.additionalServicePrice.checkInPrice) : null;
        orderBean.priceChannel = isCheckIn ?
                "" + (carBean.price + checkInPrice + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean))
                : "" + (carBean.price + getSeat1PriceTotal(carListBean, manLuggageBean) + getSeat2PriceTotal(carListBean, manLuggageBean));
//        orderBean.flightAirportCode = airPort.airportCode;
        orderBean.flightAirportName = airPort.airportName;
        return orderBean;
    }

    public OrderBean getSKUOrderByInput(String guideCollectId, SkuItemBean skuBean,
                                        String startDate, String serverTime, String distance,
                                        CarBean carBean, String adultNum, String childrenNum,
                                        CityBean startBean, String getPassCityStr,
                                        ContactUsersBean contactUsersBean,
                                        String userRemark, String userName, PoiBean poiBean,
                                        boolean dreamLeftischeck,
                                        String travelFund, CouponBean couponBean, MostFitBean mostFitBean,
                                        CarListBean carListBean, ManLuggageBean manLuggageBean, int hotelRoom, double priceHotel,
                                        int orderType, String luggageNum, String userWechat, double goodsOtherPrice, double allGoodsOtherPrice) {
        OrderBean orderBean = new OrderBean();//订单

        if (!TextUtils.isEmpty(guideCollectId)) {
            orderBean.guideCollectId = guideCollectId;
        }

        orderBean.orderType = orderType;
        orderBean.goodsNo = skuBean.goodsNo;
        orderBean.lineSubject = skuBean.getGoodsName();
        orderBean.lineDescription = skuBean.salePoints;
        orderBean.orderGoodsType = skuBean.goodsType;
        orderBean.serviceTime = startDate;//日期
        orderBean.serviceStartTime = serverTime + ":00";//时间
        orderBean.serviceEndTime = getServiceEndTime(startDate, skuBean.daysCount - 1);
        orderBean.distance = distance;//距离
//        orderBean.expectedCompTime = 0;//耗时
        orderBean.carDesc = carBean.carDesc;//车型描述
        orderBean.carType = carBean.carType;//车型
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.priceMark = carBean.pricemark;
        orderBean.urgentFlag = carBean.urgentFlag;
        orderBean.adult = Integer.valueOf(adultNum);//成人数
        orderBean.child = Integer.valueOf(childrenNum);//儿童数
        orderBean.luggageNum = luggageNum;
        orderBean.contact = new ArrayList<OrderContact>();
        OrderContact orderContact = new OrderContact();
        orderContact.areaCode = "86";
        orderContact.tel = "";
        orderBean.contact.add(orderContact);
        if (poiBean != null) {
            orderBean.startAddress = poiBean.placeName;//startBean.placeName;
            orderBean.startAddressDetail = poiBean.placeDetail;
            orderBean.startAddressPoi = poiBean.location;
        }
        orderBean.realSendSms = contactUsersBean.isSendMessage ? "1" : "0";

        orderBean.carId = carBean.carId;
        orderBean.capOfPerson = carBean.capOfPerson;
        orderBean.special = carBean.special;
        orderBean.userWechat = userWechat;
        orderBean.guideCarId = carBean.id;

        if (contactUsersBean.isForOther) {
            orderBean.isRealUser = "2";
        } else {
            orderBean.isRealUser = "1";
        }

        if (dreamLeftischeck) {
            orderBean.travelFund = travelFund;
            orderBean.orderPrice = carBean.price;
        } else {
            if (null == couponBean && null != mostFitBean) {
                orderBean.coupId = mostFitBean.couponId;
                orderBean.coupPriceInfo = mostFitBean.couponPrice + "";
                orderBean.orderPrice = carBean.price;
                if (null != mostFitBean && null != mostFitBean.actualPrice && mostFitBean.actualPrice != 0) {
                    orderBean.priceActual = mostFitBean.actualPrice + "";
                } else {
                    orderBean.priceActual = "0";
                }
            } else if (null != couponBean && null == mostFitBean) {
                orderBean.coupId = couponBean.couponID;
                orderBean.coupPriceInfo = couponBean.price;
                orderBean.orderPrice = carBean.price;
                if (couponBean.actualPrice != 0) {
                    orderBean.priceActual = couponBean.actualPrice + "";
                } else {
                    orderBean.priceActual = "0";
                }
            }
        }

        orderBean.serviceCityId = skuBean.depCityId;
        orderBean.serviceCityName = skuBean.depCityName;
        //出发地，到达地经纬度
        orderBean.terminalLocation = null;
//        orderBean.destAddress = poiBean.placeName;
//        orderBean.destAddressDetail = poiBean.placeDetail;
        orderBean.serviceEndCityid = skuBean.arrCityId;
        orderBean.serviceEndCityName = skuBean.arrCityName;
        orderBean.totalDays = skuBean.daysCount;
        orderBean.oneCityTravel = skuBean.goodsType == 3 ? 1 : 2;//1：市内畅游  2：跨城市
        orderBean.isHalfDaily = 0;
        orderBean.inTownDays = skuBean.goodsType == 3 ? skuBean.daysCount : 0;
        orderBean.outTownDays = skuBean.goodsType == 3 ? 0 : skuBean.daysCount;
        orderBean.skuPoi = "";
        orderBean.stayCityListStr = getPassCityStr;
        orderBean.priceChannel = (carBean.price + priceHotel + allGoodsOtherPrice) + "";
        orderBean.userName = userName;//manName.getText().toString();
        orderBean.userRemark = userRemark;//mark.getText().toString();

        if (null == carListBean.additionalServicePrice || null == carListBean.additionalServicePrice.childSeatPrice1
                && null == carListBean.additionalServicePrice.childSeatPrice2) {
            orderBean.orderPrice = carBean.price;
            orderBean.childSeatStr = "";
            orderBean.priceChannel = (carBean.price + priceHotel + allGoodsOtherPrice) + "";
        } else {
            if (manLuggageBean.childSeats != 0) {
                orderBean.orderPrice = carBean.price + getSeat1PriceTotal(carListBean, manLuggageBean)
                        + getSeat2PriceTotal(carListBean, manLuggageBean);
                orderBean.priceChannel = (carBean.price + getSeat1PriceTotal(carListBean, manLuggageBean) + priceHotel + allGoodsOtherPrice
                        + getSeat2PriceTotal(carListBean, manLuggageBean)) + "";
                orderBean.childSeatStr = getChileSeatJson(carListBean, manLuggageBean);
            } else {
                orderBean.orderPrice = carBean.price;
                orderBean.childSeatStr = "";
                orderBean.priceChannel = (carBean.price + priceHotel + allGoodsOtherPrice) + "";
            }
        }

        orderBean.userEx = getUserExJson(contactUsersBean);
        orderBean.realUserEx = getRealUserExJson(contactUsersBean);

        orderBean.hotelRoom = hotelRoom;
        if (orderBean.orderPriceInfo == null) {
            orderBean.orderPriceInfo = new OrderPriceInfo();
        }
        orderBean.orderPriceInfo.priceHotel = priceHotel;
        orderBean.goodsOtherPrice = goodsOtherPrice;
        return orderBean;
    }

    public static void checkGuideCoflict(Context context, int orderType, int cityId, String guideIds,
                                         String startTime, String endTime, String passCityId,
                                         int totalDay, int carType, int carClass, int isSpecialCar, int carId, HttpRequestListener listener) {

//        RequestGuideConflict requestGuideConflict = new RequestGuideConflict(context,orderType,cityId,
//                guideIds,startTime,
//                endTime,passCityId,totalDay,
//                carType,carClass,isSpecialCar,carId);
//        HttpRequestUtils.request(context, requestGuideConflict,listener,true);
    }

    //确认订单协议
    public static void genAgreeMent(final Activity activity, TextView textView, String source) {
        genCLickSpan(activity, textView, activity.getString(R.string.commit_agree, source), activity.getString(R.string.commit_agree_click), UrlLibs.H5_TAI_AGREEMENT, 0xff7f7f7f, null);
    }

    //用户协议
    public static void genUserAgreeMent(final Activity activity, TextView textView, MyCLickSpan.OnSpanClickListener listener) {
        genCLickSpan(activity, textView, activity.getString(R.string.user_info_tip), activity.getString(R.string.user_info_tip1), UrlLibs.H5_PROTOCOL, 0xffa8a8a8, listener);
    }

    //联系意向单定制师
    public static void genUserTravelPurposeForm(final Activity activity, TextView textView, MyCLickSpan.OnSpanClickListener listener) {
        genCLickSpan(activity, textView, activity.getString(R.string.travel_purpose_connect), activity.getString(R.string.travel_purpose_connect1), "", 0xff000000, listener);
    }

    //常用卡支付协议
    public static void genCreditAgreeMent(final Activity activity, TextView textView) {
        genCLickSpan(activity, textView, activity.getString(R.string.common_card_payment_protocol), activity.getString(R.string.common_card_payment_protocol), UrlLibs.H5_CREDIT_CARD_ARGEEMENT, 0xff393838, null);
    }

    public static void genCLickSpan(final Activity activity, TextView textView, String agree_text, String agree_text_click, int color, MyCLickSpan.OnSpanClickListener listener) {
        genCLickSpan(activity, textView, agree_text, agree_text_click, null, color, listener);
    }

    public static void genCLickSpan(final Activity activity, TextView textView, String agree_text, String agree_text_click, String url, int color, MyCLickSpan.OnSpanClickListener listener) {
        genCLickSpan(activity, textView, agree_text, agree_text_click, url, color, true, listener);
    }

    public static void genCLickSpan(final Activity activity, TextView textView, String agree_text, String agree_text_click, String url, int color, boolean isShowLine, MyCLickSpan.OnSpanClickListener listener) {
        int start = agree_text.lastIndexOf(agree_text_click);
        int end = start + agree_text_click.length();
        SpannableString clickSpan = new SpannableString(agree_text);
        clickSpan.setSpan(new MyCLickSpan(activity, url, color, isShowLine, listener), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(clickSpan);
    }

    public static class MyCLickSpan extends ClickableSpan {
        Activity activity;
        String url;
        int color = 0xff008cef;
        OnSpanClickListener listener;
        boolean isShowLine = true;

        public MyCLickSpan(Activity activity, String url, int color, OnSpanClickListener listener) {
            this(activity, url, color, true, listener);
        }

        public MyCLickSpan(Activity activity, String url, int color, boolean isShowLine, OnSpanClickListener listener) {
            super();
            this.activity = activity;
            this.url = url;
            this.color = color;
            this.listener = listener;
            this.isShowLine = isShowLine;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(color);
            ds.setUnderlineText(isShowLine);
        }

        @Override
        public void onClick(View widget) {
            if (listener != null) {
                listener.onSpanClick(widget);
            } else {
                Intent intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra("web_url", url);
                activity.startActivity(intent);
            }
        }

        public interface OnSpanClickListener {
            public void onSpanClick(View view);
        }

        public void setOnSpanClickListener(OnSpanClickListener listener) {
            this.listener = listener;
        }
    }

    public static String getPassCityStr(SkuItemBean skuBean) {
        String passCity = "";
        if (skuBean != null) {
            passCity += skuBean.depCityId + "-0";
            if (null != skuBean.passCityList) {
                for (CityBean city : skuBean.passCityList) {
                    passCity += "," + city.cityId + "-0";
                }
            }
            passCity += "," + skuBean.arrCityId + "-0";
        }
        return passCity;
    }

    public static void showSaveDialog(final Activity activity) {
        showSaveDialog(activity, null);
    }

    public static void showSaveDialog(final Activity activity, DialogInterface.OnClickListener _exitClick) {
        DialogInterface.OnClickListener exitClick = null;
        if (_exitClick == null) {
            exitClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    activity.finish();
                }
            };
        } else {
            exitClick = _exitClick;
        }
        AlertDialogUtils.showAlertDialog(activity, MyApplication.getAppContext().getString(R.string.back_alert_msg), "确认离开", "继续填写", exitClick, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public static String getOrderTypeStr(int _orderType) {
        int resID = -1;
        switch (_orderType) {
            case Constants.BUSINESS_TYPE_PICK://接机
                resID = R.string.custom_pick_up;
                break;
            case Constants.BUSINESS_TYPE_SEND://送机
                resID = R.string.custom_send;
                break;
            case Constants.BUSINESS_TYPE_DAILY://包车
            case Constants.BUSINESS_TYPE_COMBINATION://组合单
                resID = R.string.custom_chartered;
                break;
            case Constants.BUSINESS_TYPE_RENT://单次接送
                resID = R.string.custom_single;
                break;
            case Constants.BUSINESS_TYPE_COMMEND://固定线路
                resID = R.string.custom_fixed_line;
                break;
            case Constants.BUSINESS_TYPE_RECOMMEND://推荐线路
                resID = R.string.custom_recommend_line;
                break;
        }
        if (resID != -1) {
            return MyApplication.getAppContext().getString(resID);
        } else {
            return "";
        }
    }

    public static void setNextStepText(TextView textView, int text1Id, int text2Id) {
        String text1 = CommonUtils.getString(text1Id);
        String text2 = CommonUtils.getString(text2Id);
        String text = text1 + "\n" + text2;
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new RelativeSizeSpan(0.9f), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.6f), text1.length() + 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

}
