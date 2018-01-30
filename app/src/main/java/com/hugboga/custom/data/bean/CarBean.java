package com.hugboga.custom.data.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 车型
 * Created by admin on 2015/7/22.
 */
public class CarBean implements IBaseBean ,Cloneable{

    public int id;
    public int carType;//类型
    public int carSeat;//座位
    public String desc;//描述
    public String models;//代表车型
    public double originalPrice;//原价
    public double checkInPrice;//促销价
    public String pricemark;//价格戳
    public String urgentCutdownTip;//提示
    public int urgentFlag;//是否急单，1是，0非

    public int imgRes;//图片资源

    public int capOfLuggage;//行李数
    public int capOfPerson;//人数
    public double price; // 钱
    public double seckillingPrice;//秒杀价

    public String carLicenceNo;//车牌号
    public String carLicenceNoCovered;//车牌号遮盖

    public int priceChannel;
    public int orderChannel;


    public int match;//是否有匹配的车

    public int avgSpend;
    public String carDesc;
    public double localPrice;
    public int numOfPerson;
    public int overChargePerHour;
    public String payDeadline;
    public int seatCategory;
    public int servicePrice;
    public int totalDays;
    public int vehiclePrice;
    public ServiceQuoteSumBean serviceQuoteSum;
    public ServiceQuoteSumBean vehicleQuoteSum;
    public String serviceCityNote;
    public String expectedCompTime;

    public int seatType;

    public String carName2;

    public int carId;
    public int special;//0.普通车型 1.特殊车型
    public String carIntroduction;
    public List<String> carPictures;
    public List<String> carPicturesL;//非接口返回

    public String carBrandName;
    public String carName;
    public String batchNo;

    public int reconfirmFlag; //二次确认标示 0：否 1：是
    public String reconfirmTip; //二次确认提示

    public List<String> serviceTags;

    public ArrayList<GroupQuotesBean> quotes;

    public boolean isCallOnClick = false;

    public boolean isSpecialCar() {
        return special == 1;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        }catch (Exception e) {
            return null;
        }

    }
}
