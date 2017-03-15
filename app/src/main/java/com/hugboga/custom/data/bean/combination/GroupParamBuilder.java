package com.hugboga.custom.data.bean.combination;

import android.content.Context;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.activity.CombinationOrderActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarAdditionalServicePrice;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.ContactUserBean;
import com.hugboga.custom.data.bean.ContactUsersBean;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.GroupQuotesBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.MostFitBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestBatchPrice;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Config;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.OrderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/3/6.
 */
public class GroupParamBuilder {

    private Context context;
    private CharterDataUtils charterDataUtils;
    private CarListBean carListBean;
    private CarBean carBean;
    private ManLuggageBean manLuggageBean;
    private ContactUsersBean contactUsersBean;
    private String mark;
    private boolean isCheckedTravelFund;
    private double travelFund;
    private CouponBean couponBean;
    private MostFitBean mostFitBean;
    private PoiBean startPoiBean;
    private int allChildSeatPrice;

    public GroupParamBuilder() {
        context = MyApplication.getAppContext();
    }

    public GroupParamBuilder charterDataUtils(CharterDataUtils charterDataUtils) {
        this.charterDataUtils = charterDataUtils;
        return this;
    }

    public GroupParamBuilder carListBean(CarListBean carListBean) {
        this.carListBean = carListBean;
        return this;
    }

    public GroupParamBuilder carBean(CarBean carBean) {
        this.carBean = carBean;
        return this;
    }

    public GroupParamBuilder manLuggageBean(ManLuggageBean manLuggageBean) {
        this.manLuggageBean = manLuggageBean;
        return this;
    }

    public GroupParamBuilder contactUsersBean(ContactUsersBean contactUsersBean) {
        this.contactUsersBean = contactUsersBean;
        return this;
    }

    public GroupParamBuilder mark(String mark) {
        this.mark = mark;
        return this;
    }

    public GroupParamBuilder isCheckedTravelFund(boolean isCheckedTravelFund) {
        this.isCheckedTravelFund = isCheckedTravelFund;
        return this;
    }

    public GroupParamBuilder travelFund(double travelFund) {
        this.travelFund = travelFund;
        return this;
    }

    public GroupParamBuilder couponBean(CouponBean couponBean) {
        this.couponBean = couponBean;
        return this;
    }

    public GroupParamBuilder mostFitBean(MostFitBean mostFitBean) {
        this.mostFitBean = mostFitBean;
        return this;
    }

    public GroupParamBuilder startPoiBean(PoiBean startPoiBean) {
        this.startPoiBean = startPoiBean;
        return this;
    }

    public GroupParamBuilder allChildSeatPrice(int allChildSeatPrice) {
        this.allChildSeatPrice = allChildSeatPrice;
        return this;
    }

    public String build() {
        return JsonUtils.toJson(getRequestParamsBody());
    }

    public GroupParam getRequestParamsBody() {
        GroupParam groupParam = new GroupParam();
        GroupDailyParam groupDailyParam = null;
        List<GroupDailyParam> dailyList = new ArrayList<>();
        int groupDailyParamIndex = 0;
        int orderIndex = 0;
        String servicePassCitys = "";

        ArrayList<CityRouteBean.CityRouteScope> travelList = charterDataUtils.travelList;
        int size = travelList.size();

        for (int i = 0; i < size; i++) {
            CityRouteBean.CityRouteScope cityRouteScope = travelList.get(i);
            if (cityRouteScope.routeType == CityRouteBean.RouteType.PICKUP) {//只接机
                List<GroupPickupParam> pickupList = new ArrayList<>();
                pickupList.add(getGroupPickupParam(cityRouteScope));
                groupParam.pickupList = pickupList;
                orderIndex++;
            } else if (cityRouteScope.routeType == CityRouteBean.RouteType.SEND) {//只送机
                List<GroupTransParam> sendList = new ArrayList<>();
                sendList.add(getGroupTransParam(cityRouteScope));
                groupParam.transList = sendList;
            } else if (cityRouteScope.routeType == CityRouteBean.RouteType.AT_WILL) {//随便转转
                continue;
            } else {
                if (groupDailyParam == null) {
                    groupDailyParam = new GroupDailyParam();
                }
                if (groupDailyParam.servicePassDetailList.size() == 0) {
                    groupDailyParamIndex = i;
                }
                GroupDailyParam.ServicePassDetail servicePassDetail = getServicePassDetail(i, size, cityRouteScope);
                groupDailyParam.servicePassDetailList.add(servicePassDetail);
                servicePassCitys += servicePassDetail.cityId + "-1-" + RequestBatchPrice.getTourType(cityRouteScope.routeType);

                boolean isBatch = charterDataUtils.getStartCityBean(i + 1) != charterDataUtils.getStartCityBean(i + 2);//判断当天出发城市和明天开始城市是否相同，不相同，拆单
                if ((i + 1 >= size || travelList.get(i + 1).routeType == CityRouteBean.RouteType.AT_WILL)
                        || travelList.get(i + 1).routeType == CityRouteBean.RouteType.SEND
                        || cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN || isBatch) {
                    setGroupDailyParam(groupDailyParamIndex, i, orderIndex, size, cityRouteScope, groupDailyParam);
                    groupDailyParam.servicePassCitys = servicePassCitys;
                    dailyList.add(groupDailyParam);
                    groupDailyParam = new GroupDailyParam();
                    orderIndex++;
                    servicePassCitys = "";
                } else {
                    servicePassCitys += ",";
                }
            }
        }
        groupParam.parent = getGroupParentParam();
        groupParam.dailyList = dailyList;
        return groupParam;
    }

    public GroupParentParam getGroupParentParam() {
        GroupParentParam groupParentParam = new GroupParentParam();
        groupParentParam.carTypeId = carBean.carType;
        groupParentParam.carName = carBean.carName;
        groupParentParam.carSeatNum = carBean.seatCategory;
        groupParentParam.carDesc = carBean.carDesc;
        if (carBean.carId != 0) {
            groupParentParam.carModelId = "" + carBean.carId;
        }
        groupParentParam.isSpecialCar = carBean.special;
        groupParentParam.capOfLuggage = carBean.capOfLuggage;
        groupParentParam.capOfPerson = carBean.capOfPerson;
        groupParentParam.urgentFlag = carBean.urgentFlag;
        groupParentParam.orderChannel = "" + Config.channelId;
        groupParentParam.orderChannelName = "C端渠道";

        if (charterDataUtils.guidesDetailData != null) {//客人收藏司导ID 指定司导下单时 必填
            groupParentParam.guideCollectId = charterDataUtils.guidesDetailData.guideId;
            groupParentParam.carId = "" + carBean.id;
            String commentLabels = charterDataUtils.guidesDetailData.getLabels();
            if (!TextUtils.isEmpty(commentLabels)) {
                groupParentParam.guideLabel = commentLabels;
            }
        }
        groupParentParam.adultNum = manLuggageBean.mans;
        groupParentParam.childNum = manLuggageBean.childs;
        groupParentParam.luggageNumber = manLuggageBean.luggages;
        groupParentParam.childSeatInfo = getAllChileSeatBean(carBean, manLuggageBean);
        groupParentParam.userId = UserEntity.getUser().getUserId(context);
        groupParentParam.realSendSms = contactUsersBean.isSendMessage ? 1 : 0;
        groupParentParam.isRealUser = contactUsersBean.isForOther ? 2 : 1;
        ArrayList<ContactUserBean> userExInfo = new ArrayList<ContactUserBean>();
        userExInfo.add(getUserExBean(contactUsersBean));
        groupParentParam.userExInfo = userExInfo;
        ContactUserBean contactUserBean = getRealUserExBean(contactUsersBean);
        if (contactUserBean != null) {
            ArrayList<ContactUserBean> realUserExInfo = new ArrayList<ContactUserBean>();
            realUserExInfo.add(getRealUserExBean(contactUsersBean));
            groupParentParam.realUserExInfo = realUserExInfo;
        }
        groupParentParam.userRemark = mark;
        groupParentParam.priceChannel = Double.valueOf(carBean.price) + allChildSeatPrice;
        if (isCheckedTravelFund) {
            groupParentParam.travelFund = travelFund;
        } else {
            if (null == couponBean && null != mostFitBean) {
                groupParentParam.coupId = mostFitBean.couponId;
                groupParentParam.coupPriceInfo = mostFitBean.couponPrice + "";
                if (null != mostFitBean && null != mostFitBean.actualPrice && mostFitBean.actualPrice != 0) {
                    groupParentParam.priceActual = mostFitBean.actualPrice;
                } else {
                    groupParentParam.priceActual = 0d;
                }
            } else if (null != couponBean && null == mostFitBean) {
                groupParentParam.coupId = couponBean.couponID;
                groupParentParam.coupPriceInfo = couponBean.price;
                if (couponBean.actualPrice != 0) {
                    groupParentParam.priceActual = couponBean.actualPrice;
                } else {
                    groupParentParam.priceActual = 0d;
                }
            }
        }
        groupParentParam.totalDays = charterDataUtils.chooseDateBean.dayNums;
        CityBean startCityBean = charterDataUtils.getStartCityBean(1);
        groupParentParam.serviceCityName = startCityBean.name;
        groupParentParam.serviceCityId = startCityBean.cityId;

        if (charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {
            groupParentParam.serviceTime = charterDataUtils.chooseDateBean.start_date + " " + charterDataUtils.flightBean.arrivalTime + ":00";
        } else {
            groupParentParam.serviceTime = charterDataUtils.chooseDateBean.start_date + " " + CombinationOrderActivity.SERVER_TIME;
        }
        groupParentParam.serviceEndTime = charterDataUtils.chooseDateBean.end_date + " " + CombinationOrderActivity.SERVER_TIME_END;
        groupParentParam.priceMark = carBean.batchNo;
        return groupParentParam;
    }

    private OrderBean.ChildSeats getAllChileSeatBean(CarBean _carBean, ManLuggageBean manLuggageBean) {
        OrderBean.ChildSeats childSeats = new OrderBean.ChildSeats();
        int allChildSeatPrice1 = 0;
        int allChildSeatPrice2 = 0;
        ArrayList<GroupQuotesBean> quotes = _carBean.quotes;
        int size = quotes.size();
        for (int i = 0; i < size; i++) {
            GroupQuotesBean groupQuotesBean = quotes.get(i);
            if (groupQuotesBean.additionalServicePrice != null) {
                CarAdditionalServicePrice additionalServicePrice = groupQuotesBean.additionalServicePrice;
                allChildSeatPrice1 += CommonUtils.getCountInteger(additionalServicePrice.childSeatPrice1);
                allChildSeatPrice2 += CommonUtils.getCountInteger(additionalServicePrice.childSeatPrice2);
            }
        }
        childSeats.childSeatPrice1 = allChildSeatPrice1;
        childSeats.childSeatPrice2 = allChildSeatPrice2;
        childSeats.childSeatPrice1Count = OrderUtils.getSeat1Count(manLuggageBean);
        childSeats.childSeatPrice2Count = OrderUtils.getSeat2Count(manLuggageBean);
        return childSeats;
    }

    private OrderBean.ChildSeats getChileSeatBean(GroupQuotesBean groupQuotesBean, ManLuggageBean manLuggageBean) {
        if (groupQuotesBean == null || groupQuotesBean.additionalServicePrice == null) {
            return null;
        }
        OrderBean.ChildSeats childSeats = new OrderBean.ChildSeats();
        CarAdditionalServicePrice additionalServicePrice = groupQuotesBean.additionalServicePrice;
        childSeats.childSeatPrice1 = CommonUtils.getCountInteger(additionalServicePrice.childSeatPrice1);
        childSeats.childSeatPrice2 = CommonUtils.getCountInteger(additionalServicePrice.childSeatPrice2);
        childSeats.childSeatPrice1Count = OrderUtils.getSeat1Count(manLuggageBean);
        childSeats.childSeatPrice2Count = OrderUtils.getSeat2Count(manLuggageBean);
        return childSeats;
    }

    private ContactUserBean getUserExBean(ContactUsersBean contactUsersBean) {
        ContactUserBean userExBean = new ContactUserBean();
        userExBean.name = contactUsersBean.userName;
        userExBean.areaCode = contactUsersBean.phoneCode;
        userExBean.mobile = contactUsersBean.userPhone;
        return userExBean;
    }

    private ContactUserBean getRealUserExBean(ContactUsersBean contactUsersBean) {
        if (TextUtils.isEmpty(contactUsersBean.otherName)) {
            return null;
        }
        ContactUserBean userExBean = new ContactUserBean();
        userExBean.name = contactUsersBean.otherName;
        userExBean.areaCode = contactUsersBean.otherphoneCode;
        userExBean.mobile = contactUsersBean.otherPhone;
        return userExBean;
    }

    public int getSeatTotalPrice(CarAdditionalServicePrice additionalServicePrice, int childSeatCount) {
        if (additionalServicePrice == null) {
            return 0;
        }
        int result = 0;
        if (childSeatCount >= 1 && CommonUtils.getCountInteger(additionalServicePrice.childSeatPrice1) > 0) {
            result = CommonUtils.getCountInteger(additionalServicePrice.childSeatPrice1);
        }
        if (childSeatCount > 1 && CommonUtils.getCountInteger(additionalServicePrice.childSeatPrice2) > 0) {
            result += (CommonUtils.getCountInteger(additionalServicePrice.childSeatPrice2) * (childSeatCount - 1));
        }
        return result;
    }

    private GroupPickupParam getGroupPickupParam(CityRouteBean.CityRouteScope cityRouteScope) {
        GroupPickupParam groupPickupParam = new GroupPickupParam();
        GroupQuotesBean groupQuotesBean = carBean.quotes.get(0);
        CarAdditionalServicePrice additionalServicePrice = groupQuotesBean.additionalServicePrice;
        if (additionalServicePrice != null) {
            groupPickupParam.priceFlightBrandSign = CommonUtils.getCountDouble(additionalServicePrice.checkInPrice);
            if (manLuggageBean.childSeats != 0) {
                groupPickupParam.priceChannel = groupQuotesBean.price +
                        OrderUtils.getSeat1PriceTotal(additionalServicePrice, manLuggageBean) +
                        OrderUtils.getSeat2PriceTotal(additionalServicePrice, manLuggageBean);
            } else {
                groupPickupParam.priceChannel = groupQuotesBean.price;
            }
        } else {
            groupPickupParam.priceChannel = groupQuotesBean.price;
        }

        CityBean startCityBean = charterDataUtils.getStartCityBean(1);
        FlightBean flightBean = charterDataUtils.flightBean;
        groupPickupParam.serviceCityId = startCityBean.cityId;
        groupPickupParam.serviceCityName = startCityBean.name;
        groupPickupParam.serviceTime = flightBean.arrDate + " "+ flightBean.arrivalTime + ":00";
        groupPickupParam.destAddress = charterDataUtils.pickUpPoiBean.placeName;
        groupPickupParam.destAddressDetail = charterDataUtils.pickUpPoiBean.placeDetail;
        groupPickupParam.destAddressPoi = charterDataUtils.pickUpPoiBean.location;
        if (flightBean.arrivalAirport != null && !TextUtils.isEmpty(flightBean.arrivalAirport.airportName)){
            groupPickupParam.startAddress = flightBean.arrivalAirport.airportName;
            groupPickupParam.flightDestName = flightBean.arrivalAirport.airportName;
        } else {
            groupPickupParam.startAddress = flightBean.arrAirportName;
            groupPickupParam.flightDestName = flightBean.arrAirportName;
        }
        groupPickupParam.startAddressPoi = flightBean.arrLocation;
        groupPickupParam.flightNo = flightBean.flightNo;
        groupPickupParam.flightDestCode = flightBean.arrivalAirportCode;
        groupPickupParam.flightArriveTime = flightBean.arrDate + " " + flightBean.arrivalTime + ":00";
        groupPickupParam.flightAirportCode = flightBean.depAirportCode;
        groupPickupParam.flightFlyTime = flightBean.depDate + " " + flightBean.depTime + ":00";
        groupPickupParam.flightAirportName = flightBean.depAirportName;
        groupPickupParam.flightAirportBuiding = flightBean.depTerminal;
        groupPickupParam.flightDestBuilding = flightBean.arrTerminal;
        groupPickupParam.priceMark = groupQuotesBean.pricemark;
        groupPickupParam.distance = groupQuotesBean.transferDistance;
        groupPickupParam.expectedCompTime = (int)groupQuotesBean.transferEstTime;
        groupPickupParam.childSeatInfo = getChileSeatBean(groupQuotesBean, manLuggageBean);
        return groupPickupParam;
    }

    private GroupTransParam getGroupTransParam(CityRouteBean.CityRouteScope cityRouteScope) {
        GroupQuotesBean groupQuotesBean = carBean.quotes.get(carBean.quotes.size() - 1);
        GroupTransParam groupTransParam = new GroupTransParam();
        AirPort airPort = charterDataUtils.airPortBean;
        CityBean startCityBean = charterDataUtils.getStartCityBean(charterDataUtils.chooseDateBean.dayNums);
        groupTransParam.serviceCityId = startCityBean.cityId;
        groupTransParam.serviceCityName = startCityBean.name;
        groupTransParam.serviceTime = charterDataUtils.chooseDateBean.end_date + " " + charterDataUtils.sendServerTime + ":00";
        groupTransParam.flightAirportCode = airPort.airportCode;
        groupTransParam.flightAirportName =  airPort.airportName;
        groupTransParam.startAddress = charterDataUtils.sendPoiBean.placeName;
        groupTransParam.startAddressPoi = charterDataUtils.sendPoiBean.location;
        groupTransParam.startAddressDetail = charterDataUtils.sendPoiBean.placeDetail;
        groupTransParam.destAddress = airPort.airportName;
        groupTransParam.destAddressPoi = airPort.location;
        groupTransParam.priceMark = groupQuotesBean.pricemark;
        groupTransParam.isCheckin = 0;//当前版本不支持isCheckin
        groupTransParam.priceChannel = groupQuotesBean.price;
        groupTransParam.distance = groupQuotesBean.transferDistance;
        groupTransParam.childSeatInfo = getChileSeatBean(groupQuotesBean, manLuggageBean);
        return groupTransParam;
    }

    private void setGroupDailyParam(int groupDailyParamIndex, int index, int orderIndex, int travelListSize, CityRouteBean.CityRouteScope cityRouteScope, GroupDailyParam croupDailyParam) {
        if (carBean.quotes == null || orderIndex >= carBean.quotes.size()) {
            return;
        }
        CityBean startCityBean = charterDataUtils.getStartCityBean(groupDailyParamIndex + 1);
        croupDailyParam.serviceTime = DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, groupDailyParamIndex) + " " + CombinationOrderActivity.SERVER_TIME;
        GroupQuotesBean groupQuotesBean = carBean.quotes.get(orderIndex);
        //0 默认只 1 包含接机 2 包含送机 3 接机和送机 必填
        croupDailyParam.complexType = 0;
        if (groupDailyParamIndex == 0 && charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {
            croupDailyParam.complexType = 1;

            FlightBean flightBean = charterDataUtils.flightBean;
            croupDailyParam.startAddressPoi = flightBean.arrLocation;
            croupDailyParam.startAddress = flightBean.arrAirportName;

            GroupDailyParam.TravelFlightParam travelFlightParam = new GroupDailyParam.TravelFlightParam();
            travelFlightParam.serviceTime = DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, groupDailyParamIndex) + " " + flightBean.arrivalTime + ":00";
            travelFlightParam.flightDestCode = flightBean.arrAirportCode;
            travelFlightParam.flightDestName = flightBean.arrAirportName;
            travelFlightParam.flightDestPoi = flightBean.arrLocation;
            travelFlightParam.flightNo = flightBean.flightNo;
            travelFlightParam.flightArriveTime = flightBean.arrDate + " " + flightBean.arrivalTime + ":00";
            travelFlightParam.flightAirportCode = flightBean.depAirportCode;
            travelFlightParam.flightAriportPoi = flightBean.depLocation;
            travelFlightParam.flightFlyTime = flightBean.depDate + " " + flightBean.depTime + ":00";
            travelFlightParam.flightAirportName = flightBean.depAirportName;
            travelFlightParam.flightAirportBuiding = flightBean.depTerminal;
            travelFlightParam.flightDestBuilding = flightBean.arrTerminal;
            croupDailyParam.setTravelFlightParam(travelFlightParam, true);

            croupDailyParam.serviceTime = travelFlightParam.serviceTime;
        } else if (index == 0 && startPoiBean != null) {
            croupDailyParam.startAddress = startPoiBean.placeName;
            croupDailyParam.startAddressDetail = startPoiBean.placeDetail;
            croupDailyParam.startAddressPoi = startPoiBean.location;
        }
        if (index == travelListSize - 1 && charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null) {
            croupDailyParam.complexType = croupDailyParam.complexType == 1 ? 3 : 2;
            AirPort airPortBean = charterDataUtils.airPortBean;
            GroupDailyParam.TravelFlightParam travelFlightParam = new GroupDailyParam.TravelFlightParam();
            travelFlightParam.flightAirportCode = airPortBean.airportCode;
            travelFlightParam.flightAriportPoi = airPortBean.location;
            travelFlightParam.flightAirportName =  airPortBean.airportName;
            croupDailyParam.setTravelFlightParam(travelFlightParam, false);
        }
        croupDailyParam.priceChannel = groupQuotesBean.price + getSeatTotalPrice(groupQuotesBean.additionalServicePrice, manLuggageBean.childSeats);
        croupDailyParam.priceMark = groupQuotesBean.pricemark;
        croupDailyParam.totalDays = index - groupDailyParamIndex + 1;
        croupDailyParam.serviceCityId = startCityBean.cityId;
        croupDailyParam.serviceCityName = startCityBean.name;
        croupDailyParam.userRemark = mark;
        CityBean endCityBean = null;
        if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
            endCityBean = charterDataUtils.getEndCityBean(index + 1);
        } else {
            endCityBean = startCityBean;
        }
        croupDailyParam.serviceEndCityname = endCityBean.name;
        croupDailyParam.serviceEndCityid = endCityBean.cityId;
        croupDailyParam.serviceEndTime = DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, index) + " " + CombinationOrderActivity.SERVER_TIME_END;
        if (croupDailyParam.servicePassDetailList != null && croupDailyParam.servicePassDetailList.size() == 1 && cityRouteScope.routeType == CityRouteBean.RouteType.HALFDAY) {
            croupDailyParam.halfDaily = 1;
        }
        croupDailyParam.childSeatInfo = getChileSeatBean(groupQuotesBean, manLuggageBean);
    }

    private GroupDailyParam.ServicePassDetail getServicePassDetail(int index, int travelListSize, CityRouteBean.CityRouteScope cityRouteScope) {
        GroupDailyParam.ServicePassDetail servicePassDetail = new GroupDailyParam.ServicePassDetail();
        CityBean startCityBean = charterDataUtils.getStartCityBean(index + 1);
        servicePassDetail.startCityId = startCityBean.cityId;
        servicePassDetail.cityName = startCityBean.name;
        if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
            CityBean endCityBean = charterDataUtils.getEndCityBean(index + 1);
            servicePassDetail.cityId = endCityBean.cityId;
            servicePassDetail.endCityName = endCityBean.name;
        } else {
            servicePassDetail.cityId = startCityBean.cityId;
            servicePassDetail.endCityName = startCityBean.name;
        }
        servicePassDetail.days = 1; //后端要求写死
        servicePassDetail.cityType = RequestBatchPrice.getTourType(cityRouteScope.routeType);
        if (index == 0 && charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {
            servicePassDetail.airportCode = charterDataUtils.flightBean.arrAirportCode;
            servicePassDetail.complexType = 1;
        } else if (index == travelListSize - 1 && charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null) {
            servicePassDetail.airportCode = charterDataUtils.airPortBean.airportCode;
            servicePassDetail.complexType = 2;
            servicePassDetail.cityId = charterDataUtils.airPortBean.cityId;
            servicePassDetail.endCityName = charterDataUtils.airPortBean.cityName;
        }
        servicePassDetail.description = startCityBean.description;
        return servicePassDetail;
    }
}
