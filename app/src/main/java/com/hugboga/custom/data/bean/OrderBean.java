package com.hugboga.custom.data.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZHZEPHI on 2015/7/20.
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=4915608
 */
public class OrderBean implements IBaseBean{
    /**
     * 非接口返回字段
     * */
    public String childSeatNum;//儿童座椅数
    public String priceChannel;//订单价格
    public int urgentFlag;//是否急单，1是，0非
    public FlightBean flightBean;
    public String expectedCompTime; //预计服务完成时间 接送次 必填
    public String stayCityListStr;//日租包车
    public String terminalLocation;//结束位置
    public String flightAirportName;//起飞机场名称
    public String flightArriveTimeL;//航班到达时间 格式：yyyy-MM-dd HH:mm:ss
    public String flightFlyTimeL;//航班起飞时间 格式：yyyy-MM-dd HH:mm:ss
    public String distance;//服务距离
    public String serviceDepartTime; //服务时间
    public Double orderPrice;//订单价格
    public Integer checkInPrice;//check in 价格 送机
    public String priceMark;//价格戳 询价系统返回ID
    public String skuPoi;//poi 列表，jsonArray格式
    public List<PoiBean> skuPoiArray;
    public String userEx;//乘车人信息
    public String realUserEx;
    public String coupId;
    public String coupPriceInfo;
    public String childSeatStr;// 儿童座椅价格及个数
    public String cancelReason;// 取消订单原因
    public int special;
    public int capOfPerson;
    public int orderIndex;//标识是第几段行程 1开始
    public int guideCarId;

    public boolean isSeckills = false;       // 是否是秒杀
    public String timeLimitedSaleNo;         // 秒杀活动编号
    public String timeLimitedSaleScheduleNo; // 秒杀活动场次编号
    public double priceTicket;               // 秒杀原价

    public String userName;                           // 联系人姓名
    public String userRemark;                         // 用户备注信息
    public String realUserName;                       // 乘车人姓名
    public String realAreaCode;                       // 乘车人区号
    public String realMobile;                         // 乘车人电话
    public String isRealUser;                         // 是否有乘车人：1-没有乘车人信息、2-有乘车人信息；
    public String realSendSms;                        // 给乘车人发短信：0-不发送、1-发送短信；
    public ArrayList<OrderContactBean> userList;      // 乘车人
    public ArrayList<OrderContactBean> realUserList;  // 联系人
    public String luggageNum;                         // luggageNumber 行李数
    public String userWechat;                         // 用户微信号

    public String goodsNo;          // goodNo 商品ID
    public String orderNo;          // 订单编号
    public Integer orderType;       // 订单类型：1-接机、2-送机、3-包车游、4-次租(单次接送)、5-固定线路、6-推荐线路；
    public int orderGoodsType;      // 订单子类型：1-接机、2-送机、3-市内包车、4-次租（单次接送）、5-精品线路、6-小长途、7-打长途；
    public OrderStatus orderStatus; // 订单状态：1-未付款、2：已付款、3-已接单、4-已到达、5-服务中、6-未评价、7-已完成、8-已取消、9-已退款、10-客诉处理中；
    public boolean isTwiceConfirm;  // 二次确认订单，0否，1是

    public String payDeadTime;              // 支付结束时间，订单未支付时有值
    public boolean cancelable;              // 是否能取消
    public String cancelText;               // 不可取消的文案提示
    public String cancelTip;                // 取消订单提示语
    public boolean canComment;              // appraisable 能否评价
    public int additionIsRead;              // 是否确认后付费用
    public String travelFund;               // 当前订单使用的旅游基金
    public int orderSource;                 // 订单来源：1-C端、2-GDS、3-OTA、12-分销；
    public String skuDetailUrl;             // H5商品详情URL
    public boolean isChangeManual;          // 是否人工退改：0-非人工、1-人工；
    public boolean isIm;                    // 是否可以聊天：0-不可以、1-可以；
    public boolean isPhone;                 // 是否可以打电话：0-隐藏、1-显示；
    public ArrayList<String> cancelRules;   // 订单退改规则
    public String priceActual;              // 使用券时，实际支付价格
    public double goodsOtherPrice;          // 其他费用
    public String goodsOtherPriceComment;   // 其他费用说明

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

    public int carId;
    public Integer carType;              // carTypeId 车辆类型：1-经济型、2-舒适型、3-豪华型、4-奢华型；
    public String carDesc;               // 车辆描述（现代圣达菲,起亚K5,雪佛兰迈锐宝）
    public Integer seatCategory;         // carSeatNum 车座数
    public Integer adult;                // adultNum 成人数
    public Integer child;                // childNum 小孩数
    public String passengerInfos;        // 座位信息 乘坐%1$s人、行李箱%2$s件、儿童座椅%3$s个
    public ChildSeats childSeat;         // 组合单子单，儿童座椅价格及个数
    public ChildSeats childSeats;        // 儿童座椅价格及个数

    public Integer totalDays;       // 包车天数
    public Integer inTownDays;      // serviceLocalDays 市内天数
    public Integer outTownDays;     // serviceNonlocalDays 市外城市天数
    public Integer isHalfDaily;     // halfDaily 是否半日包：0-不是半日包车、1-是半日包车；
    public Integer oneCityTravel;   // 1-市内包车、2-跨城市包车；
    public boolean carPool = false; // 是否拼车（SKU可能是拼车 1-是；0-不是）

    public String startAddress;       // 开始地址
    public String startAddressDetail; // 开始地址详情
    public String startAddressPoi;    // 开始地址poi
    public String destAddress;        // 结束地址
    public String destAddressDetail;  // 结束地址详情
    public String destAddressPoi;     // 结束地址poi

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

    public boolean insuranceEnable;                 // 是否可以添加保险 true可以 false不可以
    public String insuranceTips;                    // 添加保险提示;
    public List<List<InsureListBean>> insuranceMap; // 投保人列表
    public String insuranceStatus;                  // 保单状态提示
    public int insuranceStatusCode;                 // 1001-全部购买、1002-出现问题、1003-注销保险、1004-保单处理中；

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
    public String guideAgencyDriverId;      // 地接社老板ID
    public int guideAgencyType;             // 地接社类型,0个人地接社，1老版地接社员工，2老版地接社老板，3个人地接社（关注）

    public OrderPriceInfo orderPriceInfo;   // priceInfo
    public OrderGuideInfo orderGuideInfo;   // guideInfo
    public ChatBean imInfo;

    public int isShowBargain;       //  砍价复层是否显示
    public long bargainSeconds;     //  距离砍价结束时间 秒
    public int bargainStatus;       //  砍价状态 （0- "初始态", 1- "激活 "，2-"活动结束"）
    public double bargainAmount;    //  砍价金额

    public List<OrderContact> contact;

    public SubOrderDetail subOrderDetail;       // 子单信息
    public Integer orderJourneyCount;           // 子单个数
    public List<String> subOrderGuideAvartar;   // 子单司导头像
    public List<JourneyItem> journeyList;       // 行程信息
    public int deliverCategory;                 // 0普通发单，1组合单主单发单，2组合单拆单发单，3人工拆单，

    public FxJourneyInfoBean fxJourneyInfo;

    public boolean isSeparateOrder() {
        return deliverCategory == 2 || deliverCategory == 3;
    }

    public String getPayDeadTime() {
        if (TextUtils.isEmpty(payDeadTime)) {
            payDeadTime = "0";
        }
        return payDeadTime;
    }

    public int getTravelerCount() {
        if (child != null) {
            return adult + child;
        } else {
            return adult;
        }
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

    /**
     * 车辆类型
     * */
    public String getCarType() {
        String resultStr = "";
        switch (carType) {
            case 1:
                resultStr = "经济型";
                break;
            case 2:
                resultStr = "舒适型";
                break;
            case 3:
                resultStr = "豪华型";
                break;
            case 4:
                resultStr = "奢华型";
                break;
        }
        return resultStr;
    }

    public static class ChildSeats implements Serializable {
        public int childSeatPrice1;
        public int childSeatPrice1Count;
        public int childSeatPrice2;
        public int childSeatPrice2Count;

        //儿童座椅数
        public int getChildSeatCount() {
            return childSeatPrice1Count + childSeatPrice2Count;
        }
    }

    public String getGuideName() {
        if (orderGuideInfo == null) {
            return "";
        }
        if (guideAgencyType == 3) {
            return orderGuideInfo.contact;
        } else {
            return orderGuideInfo.guideName;
        }
    }

    public String getTravelUserName() {
        if ("2".equals(isRealUser) && realUserList != null && realUserList.size() > 0 && !TextUtils.isEmpty(realUserList.get(0).name)) {
            return realUserList.get(0).name;
        } else {
            return userName;
        }
    }

    /**
     *  判断组合单是否有退款
     * */
    public boolean isGroupRefund() {
        if (subOrderDetail == null || subOrderDetail.subOrderList == null) {
            return false;
        }
        final int size = subOrderDetail.subOrderList.size();
        for (int i = 0; i < size; i++) {
            OrderBean orderBean = subOrderDetail.subOrderList.get(i);
            if (orderBean == null) {
                continue;
            }
            OrderPriceInfo orderPriceInfo = orderBean.orderPriceInfo;
            if (orderPriceInfo == null) {
                continue;
            }
            if (orderPriceInfo.isRefund == 1) {//已经退款
                return true;
            }
        }
        return false;
    }

    public int getSubOrderPosition(String subOrderNo) {
        int subOrderPosition = -1;
        if (TextUtils.isEmpty(subOrderNo)) {
         return subOrderPosition;
        }
        if (subOrderDetail != null && subOrderDetail.totalCount > 0 && subOrderDetail.subOrderList != null) {
            int size = subOrderDetail.subOrderList.size();
            for (int i = 0; i < size; i++) {
                OrderBean orderBean = subOrderDetail.subOrderList.get(i);
                if (orderBean != null && subOrderNo.equalsIgnoreCase(orderBean.orderNo)) {
                    subOrderPosition = i;
                    break;
                }
            }
        }
        return subOrderPosition;
    }

    public static class SubOrderDetail implements Serializable{
        public Integer totalCount;              // 子单总数
        public List<OrderBean> subOrderList;    // 子单详情
    }

    public class JourneyItem implements Serializable{
        public String dateStr;                 // 日期
        public CTravelDayPickup pickup;        // 接
        public CTravelDayTransfer transfer;    // 送
        public CJourneyInfo journey;           // 行
        public int day;                        // 本地添加，当前天数
    }

    public class CTravelDayPickup implements Serializable{
        public String serviceTimeStr;       // 07:00 服务时间
        public String startAddress;         // 出发地址
        public String startAddressDetail;   // 出发地址详情
        public String startAddressPoi;      // 出发的poi
        public String destAddress;          // 目的地
        public String destAddressDetail;    // 目的地详情
        public String destAddressPoi;       // 目的地poi
        public String flightBrandSign;      // 举牌接机姓名
        public String flightNo;             // 航班编号
        public String serviceCityName;      // 服务城市
        public String flightArriveTime;     // 航班到达时间
    }

    public class CTravelDayTransfer implements Serializable{
        public String serviceTimeStr;       // 07:00 服务时间
        public String startAddress;         // 出发地址
        public String startAddressDetail;   // 出发地址详情
        public String startAddressPoi;      // 出发的poi
        public String serviceCityName;

        public String destAddress;          // 目的地
        public String destAddressDetail;    // 目的地详情
        public String destAddressPoi;       // 目的地poi

        public String flightNo;             // 航班编号
        public Integer isCheckin;           // 是否协助登机 1 协助登机 其它不协助登机
    }

    public class CJourneyInfo implements Serializable{
        public boolean hasDetailUrl;       // 是否包含行程url
        public Integer startCityId;        // 开始城市ID
        public String startCityName;       // 开始城市ID
        public Integer cityId;             // cityId:201,城市ID
        public String cityName;            // cityName:"首尔",城市名
        public Integer type;               // 0:半日、1:市内、2:周边、3:其它城市
        public String typeName;            // typeName:"市内"
        public String description;         // description:"",
        public String stayCityInfo;        // 住宿信息
        public String remark;              // 人工填写的备注
        public String scenicDesc;          // 景点
        public String scopeDesc;           // 游玩范围

        public String serviceTimeStr;      // 服务时间 07：00
        public String startAddress;        // 出发地址
        public String startAddressDetail;  // 出发地址详情
        public String startAddressPoi;     // 出发的poi

        public String destAddress;         // 目的地
        public String destAddressDetail;   // 目的地详情
        public String destAddressPoi;      // 目的地poi

        public String dateStr;             // 2016-11-11
        public Integer complexType;        // 1 接机 2 送机
        public Integer isHalfDaily;        // 是否半日包
        public Double totalHours;          // 当天总时长

        public ArrayList<Labels> labels;

        public String getLabelKilometre() {
            if (labels == null || labels.size() <= 0) {
                return "";
            }
            int size = labels.size();
            for (int i = 0; i < size; i++) {
                Labels label = labels.get(i);
                if (label == null) {
                    continue;
                }
                if (label.type == 3 || label.type == 4 || label.type == 5 || label.type == 7) {
                    return label.kilometre;
                }
            }
            return "";
        }

        public String getLabelTime() {
            if (labels == null || labels.size() <= 0) {
                return "";
            }
            int size = labels.size();
            for (int i = 0; i < size; i++) {
                Labels label = labels.get(i);
                if (label == null) {
                    continue;
                }
                if (label.type == 3 || label.type == 4 || label.type == 5 || label.type == 7) {
                    return label.time;
                }
            }
            return "";
        }
    }

    public class Labels implements Serializable{
        public String name;
        public String time;
        public String kilometre;
        public int type;
    }

    public PoiBean getStartPoiBean() {
        return new PoiBean(startAddress, startAddressDetail, startAddressPoi);
    }

    public PoiBean getDestPoiBean() {
        return new PoiBean(destAddress, destAddressDetail, destAddressPoi);
    }

    public static class FxJourneyInfoBean implements Serializable{
        public String guideId;
        public String guideName;
        public String guideNo;
        public String journeyCityIds;
        public String journeyCityNames;
        public String journeyCover;
        public int journeyDays;
        public String journeyId;
        public String journeyNo;
        public String journeyTitle;
        public String journeyUrl;
        public int journeyVersion;
        public String orderNo;
    }
}
