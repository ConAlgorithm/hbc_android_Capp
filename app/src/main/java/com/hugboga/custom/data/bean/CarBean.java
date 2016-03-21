package com.hugboga.custom.data.bean;

/**
 *
 * 车型
 * Created by admin on 2016/3/20.
 */
public class CarBean implements IBaseBean{

    public int id;
    public int carType;//类型
    public int carSeat;//座位
    public String desc;//描述
    public String models;//代表车型
    public int originalPrice;//原价
    public int checkInPrice;//促销价
    public String pricemark;//价格戳
    public int urgentFlag;//是否急单，1是，0非

    public int imgRes;//图片资源
}
