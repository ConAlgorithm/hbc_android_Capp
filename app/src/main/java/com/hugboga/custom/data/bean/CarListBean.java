package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 车型列表
 * Created by admin on 2016/3/20.
 */
public class CarListBean implements IBaseBean {
    public int orderType;//订单类型
    public int goodsType;//商品类型
    public double distance;//预估路程（单位：公里）
    public double hotelPrice;//酒店价格
    public int interval;//预估时间（单位：分钟）
    @SerializedName("quoteInfos")
    public ArrayList<CarBean> carList;
    public int serviceWay;//101：中文司导、201：当地司机&中文客服、202：当地司机&中文导游、301：英文司机&中文客服

    public int timeNotReachFlag;
    public boolean supportChildseat;
    public int guideFloatSwitch;
    public boolean supportBanner;
    public String noneCarsReason;
    public int noneCarsState;
//    public String noneCarsParam;

    public String estTime;
    public String enableLocal;

    public boolean showHotel;//是否显示酒店

    public int hotelNum;//几天
    public int hourseNum;//几间房

    public Double goodsOtherPrice;//商品其他费用价格(如果有)
    public String goodsOtherPriceComment;//商品其他费用说明(如果有)

    public boolean isSeckills = false;       // 本地字段，是否是秒杀
    public String timeLimitedSaleNo;         // 本地字段，秒杀活动编号
    public String timeLimitedSaleScheduleNo; // 本地字段，秒杀活动场次编号

    public CarAdditionalServicePrice additionalServicePrice;


    public String getServiceTypeStr() {
        String result = "";
        switch (serviceWay) {
            case 101:
                result = "中文司导";
                break;
            case 201:
                result = "当地司机&中文客服";
                break;
            case 202:
                result = "当地司机&中文导游";
                break;
            case 301:
                result = "英文司机&中文客服";
                break;
        }
        return result;
    }

}
