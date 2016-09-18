package com.hugboga.custom.data.bean;

import java.util.List;

/**
 * 车型
 * Created by admin on 2015/7/22.
 */
public class CarBean implements IBaseBean {

    public int id;
    public int carType;//类型
    public int carSeat;//座位
    public String desc;//描述
    public String models;//代表车型
    public int originalPrice;//原价
    public int checkInPrice;//促销价
    public String pricemark;//价格戳
    public String urgentCutdownTip;//提示
    public int urgentFlag;//是否急单，1是，0非

    public int imgRes;//图片资源

    public int capOfLuggage;//行李数
    public int capOfPerson;//人数
    public int price; // 钱

    public int localPrice;

    public int priceChannel;
    public int orderChannel;

    public String expectedCompTime;

    public int carId;
    public int special;
    public String carIntroduction;
    public List<String> carPictures;


}
