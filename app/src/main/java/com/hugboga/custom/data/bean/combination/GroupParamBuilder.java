package com.hugboga.custom.data.bean.combination;

import android.content.Context;
import android.text.TextUtils;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.activity.CombinationOrderActivity;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarAdditionalServicePrice;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.ContactUserBean;
import com.hugboga.custom.data.bean.ContactUsersBean;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.GroupQuotesBean;
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
import com.hugboga.custom.widget.SkuOrderTravelerInfoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/3/6.
 */
public class GroupParamBuilder {

    private Context context;
    private CharterDataUtils charterDataUtils;
    private CarBean carBean;
    private ManLuggageBean manLuggageBean;
    private ContactUsersBean contactUsersBean;
    private boolean isCheckedTravelFund;
    private double travelFund;
    private CouponBean couponBean;
    private MostFitBean mostFitBean;
    private PoiBean startPoiBean;
    private double allChildSeatPrice;
    private SkuOrderTravelerInfoView.TravelerInfoBean travelerInfoBean;

    public GroupParamBuilder() {
        context = MyApplication.getAppContext();
    }

    public GroupParamBuilder charterDataUtils(CharterDataUtils charterDataUtils) {
        this.charterDataUtils = charterDataUtils;
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

    public GroupParamBuilder allChildSeatPrice(double allChildSeatPrice) {
        this.allChildSeatPrice = allChildSeatPrice;
        return this;
    }

    public GroupParamBuilder travelerInfoBean(SkuOrderTravelerInfoView.TravelerInfoBean travelerInfoBean) {
        this.travelerInfoBean = travelerInfoBean;
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

                if (i + 1 >= size
                        || travelList.get(i + 1).routeType == CityRouteBean.RouteType.AT_WILL
                        || travelList.get(i + 1).routeType == CityRouteBean.RouteType.SEND
                        || (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN && charterDataUtils.getEndCityBean(i + 1).cityId != charterDataUtils.getStartCityBean(i + 2).cityId)
                        || (cityRouteScope.routeType != CityRouteBean.RouteType.OUTTOWN && charterDataUtils.getStartCityBean(i + 1).cityId != charterDataUtils.getStartCityBean(i + 2).cityId)) {
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

        if (!charterDataUtils.isGroupOrder) {
            groupParentParam.childSeatInfo = getAllChileSeatBean(carBean, manLuggageBean);
        }
        groupParentParam.userId = UserEntity.getUser().getUserId(context);
        groupParentParam.realSendSms = contactUsersBean.isSendMessage ? 1 : 0;
        groupParentParam.isRealUser = contactUsersBean.isForOther ? 2 : 1;
        ArrayList<ContactUserBean> userExInfo = new ArrayList<ContactUserBean>();
        userExInfo.add(getUserExBean(contactUsersBean));
        ContactUserBean contactUserBean2 = getUserExBean2(contactUsersBean);
        if (contactUserBean2 != null) {
            userExInfo.add(contactUserBean2);
        }
        groupParentParam.userExInfo = userExInfo;
        ContactUserBean contactUserBean = getRealUserExBean(contactUsersBean);
        if (contactUserBean != null) {
            ArrayList<ContactUserBean> realUserExInfo = new ArrayList<ContactUserBean>();
            realUserExInfo.add(getRealUserExBean(contactUsersBean));
            groupParentParam.realUserExInfo = realUserExInfo;
        }
        if (travelerInfoBean != null) {
            groupParentParam.userRemark = travelerInfoBean.mark;
            groupParentParam.userWechat = travelerInfoBean.wechatNo;
        }
        groupParentParam.priceChannel = carBean.price + allChildSeatPrice;
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

        if (charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null) {
            groupParentParam.serviceEndCityname = charterDataUtils.airPortBean.cityName;
            groupParentParam.serviceEndCityid = charterDataUtils.airPortBean.cityId;
        } else {
            CityRouteBean.CityRouteScope cityRouteScope = charterDataUtils.travelList.get(charterDataUtils.travelList.size() - 1);
            CityBean endCityBean = null;
            if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
                endCityBean = charterDataUtils.getEndCityBean(charterDataUtils.chooseDateBean.dayNums);
            } else {
                endCityBean = charterDataUtils.getStartCityBean(charterDataUtils.chooseDateBean.dayNums);
            }
            groupParentParam.serviceEndCityname = endCityBean.name;
            groupParentParam.serviceEndCityid = endCityBean.cityId;
        }

        String serverTime = (travelerInfoBean == null || TextUtils.isEmpty(travelerInfoBean.serverTime)) ? CombinationOrderActivity.SERVER_TIME : travelerInfoBean.serverTime + ":00";
        if (charterDataUtils.travelList != null && charterDataUtils.travelList.size() > 0 &&
                charterDataUtils.travelList.get(0).routeType == CityRouteBean.RouteType.PICKUP) {//只接机
            serverTime = charterDataUtils.flightBean.arrivalTime + ":00";
        }
        groupParentParam.serviceTime = charterDataUtils.chooseDateBean.start_date + " " + serverTime;
        groupParentParam.serviceEndTime = charterDataUtils.chooseDateBean.end_date + " " + CombinationOrderActivity.SERVER_TIME_END;
        groupParentParam.priceMark = carBean.batchNo;

        if (startPoiBean != null) {
            groupParentParam.startAddress = startPoiBean.placeName;
            groupParentParam.startAddressPoi = startPoiBean.location;
            groupParentParam.startAddressDetail = startPoiBean.placeDetail;
        } else if (charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {
            groupParentParam.startAddress = charterDataUtils.flightBean.arrAirportName;
        }
        if (charterDataUtils.isSeckills()) {
            groupParentParam.limitedSaleNo = charterDataUtils.seckillsBean.timeLimitedSaleNo;
            groupParentParam.limitedSaleScheduleNo = charterDataUtils.seckillsBean.timeLimitedSaleScheduleNo;
            groupParentParam.priceTicket = groupParentParam.priceChannel;
            groupParentParam.priceActual = carBean.seckillingPrice;
            groupParentParam.priceChannel = carBean.seckillingPrice;
        }
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
                if (additionalServicePrice == null) {
                    continue;
                }
                int childSeatPrice1 = CommonUtils.getCountInteger(additionalServicePrice.childSeatPrice1);
                int childSeatPrice2 = CommonUtils.getCountInteger(additionalServicePrice.childSeatPrice2);
                if (childSeatPrice1 > 0) {
                    allChildSeatPrice1 += childSeatPrice1;
                }
                if (childSeatPrice2 > 0) {
                    allChildSeatPrice2 += childSeatPrice2;
                }
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

    private ContactUserBean getUserExBean(ContactUsersBean contactUsersBean) {
        ContactUserBean userExBean = new ContactUserBean();
        userExBean.name = contactUsersBean.userName;
        userExBean.areaCode = TextUtils.isEmpty(contactUsersBean.phoneCode) ? "86" : contactUsersBean.phoneCode;
        userExBean.mobile = contactUsersBean.userPhone;
        return userExBean;
    }

    private ContactUserBean getUserExBean2(ContactUsersBean contactUsersBean) {
        if (TextUtils.isEmpty(contactUsersBean.user1Name) || TextUtils.isEmpty(contactUsersBean.user1Phone)) {
            return null;
        }
        ContactUserBean userExBean = new ContactUserBean();
        userExBean.name = contactUsersBean.user1Name;
        userExBean.areaCode = TextUtils.isEmpty(contactUsersBean.phone1Code) ? "86" : contactUsersBean.phone1Code;
        userExBean.mobile = contactUsersBean.user1Phone;
        return userExBean;
    }

    private ContactUserBean getRealUserExBean(ContactUsersBean contactUsersBean) {
        if (TextUtils.isEmpty(contactUsersBean.otherName)) {
            return null;
        }
        ContactUserBean userExBean = new ContactUserBean();
        userExBean.name = contactUsersBean.otherName;
        userExBean.areaCode = TextUtils.isEmpty(contactUsersBean.otherphoneCode) ? "86" : contactUsersBean.otherphoneCode;
        userExBean.mobile = contactUsersBean.otherPhone;
        return userExBean;
    }

    private GroupPickupParam getGroupPickupParam(CityRouteBean.CityRouteScope cityRouteScope) {
        GroupPickupParam groupPickupParam = new GroupPickupParam();
        GroupQuotesBean groupQuotesBean = carBean.quotes.get(0);
        CityBean startCityBean = charterDataUtils.getStartCityBean(1);
        FlightBean flightBean = charterDataUtils.flightBean;
        groupPickupParam.serviceCityId = startCityBean.cityId;
        groupPickupParam.serviceCityName = startCityBean.name;

        String serverTime = (travelerInfoBean == null || TextUtils.isEmpty(travelerInfoBean.serverTime)) ? flightBean.arrivalTime : travelerInfoBean.serverTime;
        if (charterDataUtils.travelList != null && charterDataUtils.travelList.size() > 0 &&
                charterDataUtils.travelList.get(0).routeType == CityRouteBean.RouteType.PICKUP) {//只接机
            serverTime = flightBean.arrivalTime + ":00";
        }
        groupPickupParam.serviceTime = flightBean.arrDate + " "+ serverTime + ":00";
        groupPickupParam.destAddress = charterDataUtils.pickUpPoiBean.placeName;
        groupPickupParam.destAddressDetail = charterDataUtils.pickUpPoiBean.placeDetail;
        groupPickupParam.destAddressPoi = charterDataUtils.pickUpPoiBean.location;
        groupPickupParam.startAddress = flightBean.arrAirportName;
        groupPickupParam.flightDestName = flightBean.arrAirportName;
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
        if (!charterDataUtils.isGroupOrder && groupQuotesBean.additionalServicePrice != null) {
            groupPickupParam.childSeatInfo = getChileSeatBean(groupQuotesBean, manLuggageBean);
            groupPickupParam.priceChannel = groupQuotesBean.price + getSeatTotalPrice(groupQuotesBean.additionalServicePrice, manLuggageBean.childSeats);
        } else {
            groupPickupParam.priceChannel = groupQuotesBean.price;
        }
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
        if (!charterDataUtils.isGroupOrder && groupQuotesBean.additionalServicePrice != null) {
            groupTransParam.childSeatInfo = getChileSeatBean(groupQuotesBean, manLuggageBean);
            groupTransParam.priceChannel = groupQuotesBean.price + getSeatTotalPrice(groupQuotesBean.additionalServicePrice, manLuggageBean.childSeats);
        } else {
            groupTransParam.priceChannel = groupQuotesBean.price;
        }
        groupTransParam.distance = groupQuotesBean.transferDistance;
        return groupTransParam;
    }

    private void setGroupDailyParam(int groupDailyParamIndex, int index, int orderIndex, int travelListSize, CityRouteBean.CityRouteScope cityRouteScope, GroupDailyParam croupDailyParam) {
        if (carBean.quotes == null || orderIndex >= carBean.quotes.size()) {
            return;
        }
        CityBean startCityBean = charterDataUtils.getStartCityBean(groupDailyParamIndex + 1);
        String serverTime = (travelerInfoBean != null && !TextUtils.isEmpty(travelerInfoBean.serverTime) && orderIndex == 0) ? travelerInfoBean.serverTime + ":00" : CombinationOrderActivity.SERVER_TIME;
        croupDailyParam.serviceTime = DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, groupDailyParamIndex) + " " + serverTime;

        GroupQuotesBean groupQuotesBean = carBean.quotes.get(orderIndex);
        //0 默认只 1 包含接机 2 包含送机 3 接机和送机 必填
        croupDailyParam.complexType = 0;
        if (groupDailyParamIndex == 0 && charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {
            croupDailyParam.complexType = 1;

            FlightBean flightBean = charterDataUtils.flightBean;
            croupDailyParam.startAddressPoi = flightBean.arrLocation;
            croupDailyParam.startAddress = flightBean.arrAirportName;

            GroupDailyParam.TravelFlightParam travelFlightParam = new GroupDailyParam.TravelFlightParam();
            String serverTime2 = (travelerInfoBean == null || TextUtils.isEmpty(travelerInfoBean.serverTime)) ? flightBean.arrivalTime : travelerInfoBean.serverTime;
            travelFlightParam.serviceTime = DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, groupDailyParamIndex) + " " + serverTime2 + ":00";
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
        } else if (orderIndex == 0 && startPoiBean != null) {
            croupDailyParam.startAddress = startPoiBean.placeName;
            croupDailyParam.startAddressDetail = startPoiBean.placeDetail;
            croupDailyParam.startAddressPoi = startPoiBean.location;
        }
        boolean isSend = false;
        if (index == travelListSize - 1 && charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null) {
            croupDailyParam.complexType = croupDailyParam.complexType == 1 ? 3 : 2;
            AirPort airPortBean = charterDataUtils.airPortBean;
            GroupDailyParam.TravelFlightParam travelFlightParam = new GroupDailyParam.TravelFlightParam();
            travelFlightParam.flightAirportCode = airPortBean.airportCode;
            travelFlightParam.flightAriportPoi = airPortBean.location;
            travelFlightParam.flightAirportName =  airPortBean.airportName;
            croupDailyParam.setTravelFlightParam(travelFlightParam, false);

            croupDailyParam.serviceEndCityname = airPortBean.cityName;
            croupDailyParam.serviceEndCityid = airPortBean.cityId;
            isSend = true;
        }

        if (!isSend) {
            CityBean endCityBean = null;
            if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
                endCityBean = charterDataUtils.getEndCityBean(index + 1);
            } else {
                endCityBean = charterDataUtils.getStartCityBean(index + 1);
            }
            croupDailyParam.serviceEndCityname = endCityBean.name;
            croupDailyParam.serviceEndCityid = endCityBean.cityId;
        }

        croupDailyParam.priceMark = groupQuotesBean.pricemark;
        croupDailyParam.totalDays = index - groupDailyParamIndex + 1;
        croupDailyParam.serviceCityId = startCityBean.cityId;
        croupDailyParam.serviceCityName = startCityBean.name;
        if (travelerInfoBean != null) {
            croupDailyParam.userRemark = travelerInfoBean.mark;
            croupDailyParam.userWechat = travelerInfoBean.wechatNo;
        }

        croupDailyParam.serviceEndTime = DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, index) + " " + CombinationOrderActivity.SERVER_TIME_END;
        if (croupDailyParam.servicePassDetailList != null && croupDailyParam.servicePassDetailList.size() == 1 && cityRouteScope.routeType == CityRouteBean.RouteType.HALFDAY) {
            croupDailyParam.halfDaily = 1;
        }
        if (!charterDataUtils.isGroupOrder && groupQuotesBean.additionalServicePrice != null) {
            croupDailyParam.childSeatInfo = getChileSeatBean(groupQuotesBean, manLuggageBean);
            croupDailyParam.priceChannel = groupQuotesBean.price + getSeatTotalPrice(groupQuotesBean.additionalServicePrice, manLuggageBean.childSeats);
        } else {
            croupDailyParam.priceChannel = groupQuotesBean.price;
        }

        if (charterDataUtils.isSeckills()) {
            croupDailyParam.limitedSaleNo = charterDataUtils.seckillsBean.timeLimitedSaleNo;
            croupDailyParam.limitedSaleScheduleNo = charterDataUtils.seckillsBean.timeLimitedSaleScheduleNo;
            croupDailyParam.priceTicket = croupDailyParam.priceChannel;
            croupDailyParam.priceActual = carBean.seckillingPrice;
            croupDailyParam.priceChannel = carBean.seckillingPrice;
        }
    }

    private GroupDailyParam.ServicePassDetail getServicePassDetail(int index, int travelListSize, CityRouteBean.CityRouteScope cityRouteScope) {
        GroupDailyParam.ServicePassDetail servicePassDetail = new GroupDailyParam.ServicePassDetail();
        CityBean startCityBean = charterDataUtils.getStartCityBean(index + 1);
        servicePassDetail.startCityId = startCityBean.cityId;
        if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
            CityBean endCityBean = charterDataUtils.getEndCityBean(index + 1);
            servicePassDetail.cityId = endCityBean.cityId;
            servicePassDetail.cityName = endCityBean.name;
        } else {
            servicePassDetail.cityId = startCityBean.cityId;
            servicePassDetail.cityName = startCityBean.name;
            if (cityRouteScope.isOpeanFence()) {
                if (!TextUtils.isEmpty(cityRouteScope.routeScope)) {
                    String scope = cityRouteScope.routeType == CityRouteBean.RouteType.SUBURBAN ? "周边范围：" : "市内范围：";
                    servicePassDetail.scopeDesc = scope + cityRouteScope.routeScope;
                }
                if (!TextUtils.isEmpty(cityRouteScope.routePlaces)) {
                    servicePassDetail.scenicDesc = "推荐景点：" + cityRouteScope.routePlaces;
                }
            } else {
                servicePassDetail.scenicDesc = cityRouteScope.routeScope;
                servicePassDetail.scopeDesc = cityRouteScope.routePlaces;
            }
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
            servicePassDetail.cityName = charterDataUtils.airPortBean.cityName;
        }
        servicePassDetail.description = startCityBean.description;
        return servicePassDetail;
    }
}
