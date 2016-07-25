package com.hugboga.custom.data.bean;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hugboga.custom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZHZEPHI on 2015/7/20.
 */
public class OrderBean implements IBaseBean ,Parcelable{
    /**
     * 非接口返回字段
     * */
    public String childSeatNum;
    public String destAddressPoi;
    public String priceChannel;
    public int urgentFlag;//是否急单，1是，0非
    public FlightBean flightBean;
    public String expectedCompTime; //预计服务完成时间 接送次 必填
    public String stayCityListStr;//日租包车
    public String terminalLocation;//结束位置
    public String flightAirportName;
    public String flightArriveTimeL;
    public String flightFlyTimeL;
    public String distance;//服务距离
    public String serviceDepartTime; //服务时间
    public Integer orderPrice;//订单价格
    public Integer checkInPrice;//check in 价格 送机
    public String priceMark;//价格戳 询价系统返回ID
    public String skuPoi;//poi 列表，jsonArray格式
    public List<PoiBean> skuPoiArray;
    public String userEx;
    public String realUserEx;
    public String coupId;
    public String coupPriceInfo;
    public String childSeatStr;// 儿童座椅价格及个数

    /**
     * 问题字段
     * */
    public String userName;    // null
    public String contactName; // userName 联系人姓名

    public String userRemark; // null
    public String memo;       // userRemark 用户备注信息

    public String startAddressPoi; // null
    public String startLocation;   // startAddressPoi 起始位置




    public String realUserName;                       // 乘车人姓名
    public String realAreaCode;                       // 乘车人区号
    public String realMobile;                         // 乘车人电话
    public String isRealUser;                         // 是否有乘车人：1-没有乘车人信息、2-有乘车人信息；
    public String realSendSms;                        // 给乘车人发短信：0-不发送、1-发送短信；
    public ArrayList<OrderContactBean> userList;      // 乘车人
    public ArrayList<OrderContactBean> realUserList;  // 联系人
    public String luggageNum;                         // luggageNumber 行李数

    public String goodsNo;          // goodNo 商品ID
    public String orderNo;          // 订单编号
    public Integer orderType;       // 订单类型：1-接机、2-送机、3-包车游、4-次租(单次接送)、5-固定线路、6-推荐线路；
    public int orderGoodsType;      // 订单子类型：1-接机、2-送机、3-市内包车、4-次租（单次接送）、5-精品线路、6-小长途、7-打长途；
    public OrderStatus orderStatus; // 订单状态：1-未付款、2：已付款、3-已接单、4-已到达、5-服务中、6-未评价、7-已完成、8-已取消、9-已退款、10-客诉处理中；

    public String payDeadTime;              // 支付结束时间，订单未支付时有值
    public boolean cancelable;              // 是否能取消
    public String cancelText;               // 不可取消的文案提示
    public String cancelTip;                // 取消订单提示语
    public boolean canComment;              // appraisable 能否评价
    public int additionIsRead;              // 是否确认后付费用
    public String travelFund;               // 当前订单使用的旅游基金
    public int orderSource;                 // 订单来源：1-C端、2-GDS、3-OTA；
    public String skuDetailUrl;             // H5商品详情URL
    public boolean isChangeManual;          // 是否人工退改：0-非人工、1-人工；
    public boolean isIm;                    // 是否可以聊天：0-不可以、1-可以；
    public boolean isPhone;                 // 是否可以打电话：0-隐藏、1-显示；
    public ArrayList<String> cancelRules;   // 订单退改规则
    public String priceActual;              // 使用券时，实际支付价格

    public Integer imcount; // IM未读消息数
    public boolean canChat; // 是否可聊天
    public String imToken;  // IM token

    public String serviceAreaCode;    // 服务酒店区号，例如：86，中国
    public String serviceAddressTel;  // 服务酒店或者区域电话号码
    public Integer serviceCityId;     // 服务城市ID
    public String serviceCityName;    // 服务城市
    public Integer serviceEndCityid;  // 服务结束城市ID
    public String serviceEndCityName; // 服务结束城市
    public String serviceTime;        // 服务开始时间
    public String serviceEndTime;     // 服务结束时间
    public String serviceStartTime;   // serviceRecTime 服务时间时分秒
    public String serviceTimeStr;     // 格式化后的当地时间串 开始时间例如： 04月21日（周五）10:05
    public String serviceEndTimeStr;  // 格式化后的服务结束时间，当地时间串 结束时间

    public Integer carType;              // carTypeId 车辆类型：1-经济型、2-舒适型、3-豪华型、4-奢华型；
    public String carDesc;               // 车辆描述（现代圣达菲,起亚K5,雪佛兰迈锐宝）
    public Integer seatCategory;         // carSeatNum 车座数
    public Integer adult;                // adultNum 成人座位数
    public Integer child;                // childNum 小孩座位数
    public String passengerInfos;        // 座位信息 乘坐%1$s人、行李箱%2$s件、儿童座椅%3$s个
    public ArrayList<String> childSeat;  // 儿童座椅价格及个数

    public Integer totalDays;       // 包车天数
    public Integer inTownDays;      // serviceLocalDays 市内天数
    public Integer outTownDays;     // serviceNonlocalDays 市外城市天数
    public Integer isHalfDaily;     // halfDaily 是否半日包：0-不是半日包车、1-是半日包车；
    public Integer oneCityTravel;   // 1-市内包车、2-跨城市包车；
    public boolean carPool = false; // 是否拼车（SKU可能是拼车 1-是；0-不是）

    public String startAddress;       // 开始地址
    public String startAddressDetail; // 开始地址详情
    public String destAddress;        // 结束地址
    public String destAddressDetail;  // 结束地址详情

    public String flightAirportBuiding; // 起飞机场航站楼
    public String flightAirportCode;    // 起飞机场三字码code
    public String flightBrandSign;      // 接机牌
    public String brandSign;            // TODO flightBrandSign 接机牌
    public String flightDestCode;       // 降落机场三字码
    public String flightDestName;       // 降落机场名称
    public String flightNo;             // 航班号
    public String flight;               // TODO flightNo 航班号
    public String flightDeptCityName;   // 起飞机场所在城市
    public String flightDestCityName;   // 降落机场所在城市
    public Integer visa;                // isArrivalVisa 是否落地签证
    public String isArrivalVisa;        // TODO 没赋值
    public String priceFlightBrandSign; // 接机牌费用
    public String isFlightSign;         // 是否选择举牌接机服务
    public String isCheckin;            // 是否协助登机：0-未选、1-选择；

    public boolean insuranceEnable;             // 是否可以添加保险 true可以 false不可以
    public String insuranceTips;                // 添加保险提示;
    public List<InsureListBean> insuranceList;  // 投保人列表
    public String insuranceStatus;              // 保单状态提示
    public int insuranceStatusCode;             // 1001-全部购买、1002-出现问题、1003-注销保险、1004-保单处理中；

    public ArrayList<CityBean> passByCity; // passCities 包车途径城市
    public String lineSubject;             // 线路名称，精品线路，标题 xx一日游
    public String lineDescription;         // 线路描述，精品线路，简介
    public String journeyComment;          // 行程说明（游玩计划）
    public String dailyTips;               // dailyOrderTips 注意事项（城市提示）
    public String picUrl;                  // 路线活动图片

    public int hotelRoom;   // 房间数
    public int hotelDays;   // 几晚
    public int hotelStatus; // 是否有酒店（0，没有；1，有）

    public String guideCollectId;           // 指定司导ID
    public int userCommentStatus;           // 用户是否给导游评价过：0-未评价、1-评价过；
    public int priceCommentReward;          // 好评奖励金额
    public AppraisementBean appraisement;   // 评价信息
    public AssessmentBean assessmentBean;   // TODO appraisement 评价信息

    public OrderPriceInfo orderPriceInfo;   // priceInfo
    public OrderGuideInfo orderGuideInfo;   // guideInfo
    public CouponBean orderCoupon;          // coupon

    public List<OrderContact> contact;

    public String getOrderTypeStr(Context context) {
        switch (orderGoodsType) {
            case 1:
                return context.getString(R.string.title_pick);
            case 2:
                return context.getString(R.string.title_send);
            case 3:
                return context.getString(R.string.title_daily_in_town);
            case 4:
                return context.getString(R.string.title_rent);
            case 5:
                return context.getString(R.string.title_commend);
            case 6:
                return context.getString(R.string.title_daily_small);
            case 7:
                return context.getString(R.string.title_daily_large);
            default:
                return "";
        }
    }

    public String getPayDeadTime() {
        if (TextUtils.isEmpty(payDeadTime)) {
            payDeadTime = "0";
        }
        return payDeadTime;
    }

    /**
     * 是否评价过
     * */
    public boolean isEvaluated() {
        return userCommentStatus == 1;
    }

    /**
     * 保单状态
     * */
    public String getInsuranceStatus() {
        String resultStr = "";
        switch (insuranceStatusCode) {
            case 1001:
                resultStr = "全部购买";
                break;
            case 1002:
                resultStr = "出现问题";
                break;
            case 1003:
                resultStr = "注销保险";
                break;
            case 1004:
                resultStr = "保单处理中";
                break;
        }
        return resultStr;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.childSeatNum);
        dest.writeString(this.luggageNum);
        dest.writeString(this.realUserName);
        dest.writeString(this.realAreaCode);
        dest.writeString(this.realMobile);
        dest.writeString(this.isRealUser);
        dest.writeString(this.startAddressPoi);
        dest.writeString(this.destAddressPoi);
        dest.writeString(this.userName);
        dest.writeString(this.priceChannel);
        dest.writeString(this.userRemark);
        dest.writeValue(this.orderType);
        dest.writeInt(this.orderGoodsType);
        dest.writeString(this.orderNo);
        dest.writeValue(this.imcount);
        dest.writeInt(this.orderStatus == null ? -1 : this.orderStatus.ordinal());
        dest.writeInt(this.urgentFlag);
        dest.writeValue(this.serviceCityId);
        dest.writeString(this.serviceCityName);
        dest.writeValue(this.serviceEndCityid);
        dest.writeString(this.serviceEndCityName);
        dest.writeValue(this.carType);
        dest.writeValue(this.seatCategory);
        dest.writeString(this.carDesc);
        dest.writeString(this.flight);
        dest.writeParcelable(this.flightBean, flags);
        dest.writeString(this.serviceTime);
        dest.writeString(this.serviceEndTime);
        dest.writeString(this.serviceStartTime);
        dest.writeString(this.expectedCompTime);
        dest.writeTypedList(this.passByCity);
        dest.writeString(this.stayCityListStr);
        dest.writeValue(this.totalDays);
        dest.writeString(this.startAddress);
        dest.writeString(this.startAddressDetail);
        dest.writeString(this.startLocation);
        dest.writeString(this.destAddress);
        dest.writeString(this.destAddressDetail);
        dest.writeString(this.terminalLocation);
        dest.writeString(this.flightAirportCode);
        dest.writeString(this.serviceAreaCode);
        dest.writeString(this.serviceAddressTel);
        dest.writeString(this.distance);
        dest.writeString(this.contactName);
        dest.writeString(this.brandSign);
        dest.writeValue(this.adult);
        dest.writeValue(this.child);
        dest.writeValue(this.visa);
        dest.writeString(this.memo);
        dest.writeString(this.payDeadTime);
        dest.writeByte(this.cancelable ? (byte) 1 : (byte) 0);
        dest.writeString(this.cancelText);
        dest.writeString(this.cancelTip);
        dest.writeByte(this.canComment ? (byte) 1 : (byte) 0);
        dest.writeByte(this.canChat ? (byte) 1 : (byte) 0);
        dest.writeString(this.imToken);
        dest.writeValue(this.orderPrice);
        dest.writeValue(this.checkInPrice);
        dest.writeString(this.priceMark);
        dest.writeStringList(this.childSeat);
        dest.writeList(this.contact);
        dest.writeParcelable(this.orderPriceInfo, flags);
        dest.writeParcelable(this.orderGuideInfo, flags);
        dest.writeParcelable(this.orderCoupon, flags);
        dest.writeParcelable(this.assessmentBean, flags);
        dest.writeInt(this.additionIsRead);
        dest.writeString(this.lineSubject);
        dest.writeString(this.lineDescription);
        dest.writeValue(this.oneCityTravel);
        dest.writeValue(this.isHalfDaily);
        dest.writeValue(this.inTownDays);
        dest.writeValue(this.outTownDays);
        dest.writeString(this.journeyComment);
        dest.writeString(this.dailyTips);
        dest.writeByte(this.insuranceEnable ? (byte) 1 : (byte) 0);
        dest.writeString(this.insuranceTips);
        dest.writeTypedList(this.insuranceList);
        dest.writeString(this.goodsNo);
        dest.writeString(this.skuPoi);
        dest.writeTypedList(this.skuPoiArray);
        dest.writeString(this.insuranceStatus);
        dest.writeInt(this.insuranceStatusCode);
        dest.writeString(this.serviceDepartTime);
        dest.writeString(this.realSendSms);
        dest.writeString(this.travelFund);
        dest.writeString(this.guideCollectId);
        dest.writeString(this.userEx);
        dest.writeString(this.realUserEx);
        dest.writeString(this.coupId);
        dest.writeString(this.coupPriceInfo);
        dest.writeString(this.flightDeptCityName);
        dest.writeString(this.flightDestCityName);
        dest.writeString(this.serviceTimeStr);
        dest.writeString(this.serviceEndTimeStr);
        dest.writeString(this.passengerInfos);
        dest.writeInt(this.userCommentStatus);
        dest.writeString(this.childSeatStr);
        dest.writeString(this.isArrivalVisa);
        dest.writeString(this.priceFlightBrandSign);
        dest.writeString(this.isFlightSign);
        dest.writeString(this.priceActual);
        dest.writeString(this.isCheckin);
        dest.writeString(this.flightAirportBuiding);
        dest.writeString(this.flightAirportName);
        dest.writeString(this.flightArriveTimeL);
        dest.writeString(this.flightBrandSign);
        dest.writeString(this.flightDestCode);
        dest.writeString(this.flightDestName);
        dest.writeString(this.flightFlyTimeL);
        dest.writeString(this.flightNo);
        dest.writeTypedList(this.userList);
        dest.writeTypedList(this.realUserList);
        dest.writeInt(this.priceCommentReward);
        dest.writeByte(this.carPool ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.appraisement);
        dest.writeStringList(this.cancelRules);
        dest.writeByte(this.isIm ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPhone ? (byte) 1 : (byte) 0);
        dest.writeString(this.picUrl);
        dest.writeByte(this.isChangeManual ? (byte) 1 : (byte) 0);
        dest.writeInt(this.hotelStatus);
        dest.writeInt(this.hotelRoom);
        dest.writeInt(this.hotelDays);
        dest.writeInt(this.orderSource);
        dest.writeString(this.skuDetailUrl);
    }

    public OrderBean() {
    }

    protected OrderBean(Parcel in) {
        this.childSeatNum = in.readString();
        this.luggageNum = in.readString();
        this.realUserName = in.readString();
        this.realAreaCode = in.readString();
        this.realMobile = in.readString();
        this.isRealUser = in.readString();
        this.startAddressPoi = in.readString();
        this.destAddressPoi = in.readString();
        this.userName = in.readString();
        this.priceChannel = in.readString();
        this.userRemark = in.readString();
        this.orderType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.orderGoodsType = in.readInt();
        this.orderNo = in.readString();
        this.imcount = (Integer) in.readValue(Integer.class.getClassLoader());
        int tmpOrderStatus = in.readInt();
        this.orderStatus = tmpOrderStatus == -1 ? null : OrderStatus.values()[tmpOrderStatus];
        this.urgentFlag = in.readInt();
        this.serviceCityId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.serviceCityName = in.readString();
        this.serviceEndCityid = (Integer) in.readValue(Integer.class.getClassLoader());
        this.serviceEndCityName = in.readString();
        this.carType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.seatCategory = (Integer) in.readValue(Integer.class.getClassLoader());
        this.carDesc = in.readString();
        this.flight = in.readString();
        this.flightBean = in.readParcelable(FlightBean.class.getClassLoader());
        this.serviceTime = in.readString();
        this.serviceEndTime = in.readString();
        this.serviceStartTime = in.readString();
        this.expectedCompTime = in.readString();
        this.passByCity = in.createTypedArrayList(CityBean.CREATOR);
        this.stayCityListStr = in.readString();
        this.totalDays = (Integer) in.readValue(Integer.class.getClassLoader());
        this.startAddress = in.readString();
        this.startAddressDetail = in.readString();
        this.startLocation = in.readString();
        this.destAddress = in.readString();
        this.destAddressDetail = in.readString();
        this.terminalLocation = in.readString();
        this.flightAirportCode = in.readString();
        this.serviceAreaCode = in.readString();
        this.serviceAddressTel = in.readString();
        this.distance = in.readString();
        this.contactName = in.readString();
        this.brandSign = in.readString();
        this.adult = (Integer) in.readValue(Integer.class.getClassLoader());
        this.child = (Integer) in.readValue(Integer.class.getClassLoader());
        this.visa = (Integer) in.readValue(Integer.class.getClassLoader());
        this.memo = in.readString();
        this.payDeadTime = in.readString();
        this.cancelable = in.readByte() != 0;
        this.cancelText = in.readString();
        this.cancelTip = in.readString();
        this.canComment = in.readByte() != 0;
        this.canChat = in.readByte() != 0;
        this.imToken = in.readString();
        this.orderPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.checkInPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.priceMark = in.readString();
        this.childSeat = in.createStringArrayList();
        this.contact = new ArrayList<OrderContact>();
        in.readList(this.contact, OrderContact.class.getClassLoader());
        this.orderPriceInfo = in.readParcelable(OrderPriceInfo.class.getClassLoader());
        this.orderGuideInfo = in.readParcelable(OrderGuideInfo.class.getClassLoader());
        this.orderCoupon = in.readParcelable(CouponBean.class.getClassLoader());
        this.assessmentBean = in.readParcelable(AssessmentBean.class.getClassLoader());
        this.additionIsRead = in.readInt();
        this.lineSubject = in.readString();
        this.lineDescription = in.readString();
        this.oneCityTravel = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isHalfDaily = (Integer) in.readValue(Integer.class.getClassLoader());
        this.inTownDays = (Integer) in.readValue(Integer.class.getClassLoader());
        this.outTownDays = (Integer) in.readValue(Integer.class.getClassLoader());
        this.journeyComment = in.readString();
        this.dailyTips = in.readString();
        this.insuranceEnable = in.readByte() != 0;
        this.insuranceTips = in.readString();
        this.insuranceList = in.createTypedArrayList(InsureListBean.CREATOR);
        this.goodsNo = in.readString();
        this.skuPoi = in.readString();
        this.skuPoiArray = in.createTypedArrayList(PoiBean.CREATOR);
        this.insuranceStatus = in.readString();
        this.insuranceStatusCode = in.readInt();
        this.serviceDepartTime = in.readString();
        this.realSendSms = in.readString();
        this.travelFund = in.readString();
        this.guideCollectId = in.readString();
        this.userEx = in.readString();
        this.realUserEx = in.readString();
        this.coupId = in.readString();
        this.coupPriceInfo = in.readString();
        this.flightDeptCityName = in.readString();
        this.flightDestCityName = in.readString();
        this.serviceTimeStr = in.readString();
        this.serviceEndTimeStr = in.readString();
        this.passengerInfos = in.readString();
        this.userCommentStatus = in.readInt();
        this.childSeatStr = in.readString();
        this.isArrivalVisa = in.readString();
        this.priceFlightBrandSign = in.readString();
        this.isFlightSign = in.readString();
        this.priceActual = in.readString();
        this.isCheckin = in.readString();
        this.flightAirportBuiding = in.readString();
        this.flightAirportName = in.readString();
        this.flightArriveTimeL = in.readString();
        this.flightBrandSign = in.readString();
        this.flightDestCode = in.readString();
        this.flightDestName = in.readString();
        this.flightFlyTimeL = in.readString();
        this.flightNo = in.readString();
        this.userList = in.createTypedArrayList(OrderContactBean.CREATOR);
        this.realUserList = in.createTypedArrayList(OrderContactBean.CREATOR);
        this.priceCommentReward = in.readInt();
        this.carPool = in.readByte() != 0;
        this.appraisement = (AppraisementBean) in.readSerializable();
        this.cancelRules = in.createStringArrayList();
        this.isIm = in.readByte() != 0;
        this.isPhone = in.readByte() != 0;
        this.hotelRoom = in.readInt();
        this.picUrl = in.readString();
        this.isChangeManual = in.readByte() != 0;
        this.hotelStatus = in.readInt();
        this.hotelRoom = in.readInt();
        this.hotelDays = in.readInt();
        this.orderSource = in.readInt();
        this.skuDetailUrl = in.readString();
    }

    public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
        @Override
        public OrderBean createFromParcel(Parcel source) {
            return new OrderBean(source);
        }

        @Override
        public OrderBean[] newArray(int size) {
            return new OrderBean[size];
        }
    };
}
