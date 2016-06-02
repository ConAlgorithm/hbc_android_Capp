package com.hugboga.custom.data.bean;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.hugboga.custom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZHZEPHI on 2015/7/20.
 */
public class OrderBean implements IBaseBean ,Parcelable{

    public String childSeatNum;
    public String luggageNum;
    public String realUserName;
    public String realAreaCode;
    public String realMobile;
    public String isRealUser;
    public String startAddressPoi;
    public String destAddressPoi;
    public String userName;

    public String priceChannel;
    public String userRemark;


    public Integer orderType;//1: 接机 2: 送机 3: 市内包车(由日租拆分出来) 4: 次租
    public int orderGoodsType;//扩展字段   1: 接机 2: 送机 3: 市内包车(由日租拆分出来) 4: 次租 5: 精品线路(由日租拆分出来) 6: 小长途 (由日租拆分出来)7: 大长途 (由日租拆分出来)
    public String orderNo; //订单号
    //    public String orderID;
    public Integer imcount; //IM未读消息数
    /**
     * 订单状态。0-等待支付；1-预订成功；2-导游已接单；3-导游已到达；4-您已上车；5-行程结束;100-已完成；-1-已取消；-5-退款中；-6-退款完成；
     */
    public OrderStatus orderStatus;
    public int urgentFlag;//是否急单，1是，0非
    public Integer serviceCityId;//	服务地城市ID
    public String serviceCityName;
    public Integer serviceEndCityid;
    public String serviceEndCityName;
    public Integer carType;//必选	int	1 经济 2舒适 3豪华 4奢华
    public Integer seatCategory;//必选	int	车座数
    public String carDesc;//选填	string	现代圣达菲,起亚K5,雪佛兰迈锐宝
    public String flight;//航班号
    public FlightBean flightBean;
    public String serviceTime;//服务时间[2015-10-03 20:02:34]
    public String serviceEndTime; //包车结束时间
    public String serviceStartTime; //包车起始时间，选填
    public String expectedCompTime; //预计服务完成时间 接送次 必填
    public ArrayList<Integer> passByCityID;
    public ArrayList<CityBean> passByCity;
    public String stayCityListStr;//日租包车
    public Integer totalDays;//包车总天数
    public String startAddress;
    public String startAddressDetail;
    public String startLocation;//起始位置
    public String destAddress;//目的地address
    public String destAddressDetail;//目的地address详情
    public String terminalLocation;//结束位置
    public String flightAirportCode;//机场三字码code
    public String serviceAreaCode;//目的地区号
    public String serviceAddressTel;//目的地酒店或者区域电话号码
    public String distance;//服务距离
    public String contactName;
    public String brandSign;
    public Integer adult;//成人座位数
    public Integer child;//小孩座位数
    public Integer visa;
    public String memo;
    public String payDeadTime;//支付结束时间
    public boolean cancelable;//是否能取消
    public String cancelText; //禁止取消订单提示语
    public String cancelTip; //取消订单提示语
    public boolean canComment;//能否评价
    public boolean canChat;
    public String imToken;
    public Integer orderPrice;//订单价格
    public Integer checkInPrice;//check in 价格 送机
    public String priceMark;//价格戳 询价系统返回ID
    public ArrayList<String> childSeat;
    public List<OrderContact> contact;
    public OrderPriceInfo orderPriceInfo;
    public OrderGuideInfo orderGuideInfo;
    public CouponBean orderCoupon;
    public AssessmentBean assessmentBean;//评价

    public int additionIsRead;//增项费用是否已读
    public String lineSubject;//精品线路，标题 xx一日游
    public String lineDescription;//精品线路，简介

    //2.2.0
    public Integer oneCityTravel;//1：市内畅游  2：跨城市
    public Integer isHalfDaily;//0:不是半日包车 1:是半日包车
    public Integer inTownDays;//市内天数
    public Integer outTownDays;//市外城市天数
    public String journeyComment;//行程说明
    public String dailyTips;//注意事项

    public boolean insuranceEnable; //是否投保
    public String insuranceTips;//添加保险提示;
    public List<InsureListBean> insuranceList;

    //2.5.0
    public String goodsNo;//商品ID
    public String skuPoi;//poi 列表，jsonArray格式
    public List<PoiBean> skuPoiArray;

    public String insuranceStatus;
    public int insuranceStatusCode;////1001 全部购买 ,1002 出现问题,1003 注销保险,1004 保单处理中


    public String serviceDepartTime; //服务时间

    public String realSendSms;//1 发送短信 0 不发送 null 不发送

    public String travelFund;//当前订单使用的旅游基金
    public String guideCollectId;//指定司导ID

    public String userEx;
    public String realUserEx;

    public String coupId;
    public String coupPriceInfo;

    public String childSeatStr; //2.7.0 新加 对应 接口字段 childSeat


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
        dest.writeList(this.passByCityID);
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
        dest.writeString(this.childSeatStr);
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
        this.passByCityID = new ArrayList<Integer>();
        in.readList(this.passByCityID, Integer.class.getClassLoader());
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
        this.childSeatStr = in.readString();
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
