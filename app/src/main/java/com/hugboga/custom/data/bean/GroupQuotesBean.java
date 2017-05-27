package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by qingcha on 17/3/6.
 */
public class GroupQuotesBean implements Serializable{
    public String currency;
    public Double currencyRate;
    public int index;
    public Double originPrice;
    public Double price;
    public String pricemark;
    public int serviceType;
    public double transferDistance;
    public double transferEstTime;
    public double pickupDistance;
    public double pickupEstTime;
    public CarAdditionalServicePrice additionalServicePrice;
}
