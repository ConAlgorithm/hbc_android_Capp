package com.hugboga.custom.data.bean;

import android.text.TextUtils;

import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ZHZEPHI on 2015/7/20.
 */
public class OrderBean implements IBaseBean {

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
//    public Integer startCityID;
//    public String startCityName;
    public Integer serviceEndCityid;
    public String serviceEndCityName;
    public Integer carType;//必选	int	1 经济 2舒适 3豪华 4奢华
    public Integer seatCategory;//必选	int	车座数
    public String carDesc;//选填	string	现代圣达菲,起亚K5,雪佛兰迈锐宝
    public String flight;//航班号
//    public FlightBean flightBean;
    public String serviceTime;//服务时间[2015-10-03 20:02:34]
    public String serviceEndTime; //包车结束时间
    public String serviceStartTime; //包车起始时间，选填
    public int expectedCompTime; //预计服务完成时间 接送次 必填
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
//    public List<OrderContact> contact;
//    public OrderPriceInfo orderPriceInfo;
//    public OrderGuideInfo orderGuideInfo;
    public CouponBean orderCoupon;
    public int additionIsRead;//增项费用是否已读
    public String lineSubject;//精品线路，标题 xx一日游
    public String lineDescription;//精品线路，简介
//    public AssessmentBean assessmentBean;//评价

    //2.2.0
    public Integer oneCityTravel;//1：市内畅游  2：跨城市
    public Integer isHalfDaily ;//0:不是半日包车 1:是半日包车
    public Integer inTownDays;//市内天数
    public Integer outTownDays;//市外城市天数
    public String journeyComment;//行程说明
    public String dailyTips;//注意事项

}
