package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wbj on 2016/10/17.
 */

public class GoodsSec implements Serializable {
    public int arrCityId;//商品城市ID
    public String arrCityName;//商品城市名称
    public int daysCount;//总天数
    public int depCityId;//线路城市ID
    public String depCityName;//线路城市名称
    public int  goodsClass;//货物类型
    public String goodsLable;//货物单价
    public String goodsName;//货物名称
    public String goodsNo;//货物订单号
    public String goodsPicture;//货物小照片
    public String goodsType;//货物类型
    public String goodsVersion;//
    public int guideAmount;//
    public String headLable;//超省心,还是超自由
    public String hotelCostAmount;
    public String hotelCostPrice;
    public String hotelStatus;
    public int perPrice;
    public String places;//从哪儿到哪儿
    public String salePoints;
    public String shareURL;//分享地址
    public String skuDetailUrl;//sku细节URL
    public String transactionVolumes;//

    public ArrayList<CharacteristicLables> characteristicLables;
    public ArrayList<String>goodsPics;//照片集合
    public ArrayList<PassbyCity>passbyCity;

    public static class CharacteristicLables implements Serializable {
        public String lableName;
        public int lableType;
    }

    public static class PassbyCity implements Serializable{
        public int passbyId;
        public String passByName;
    }
}
