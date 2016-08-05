package com.hugboga.custom.data.bean;

import java.util.ArrayList;

/**
 * 车型列表
 * Created by admin on 2016/3/20.
 */
public class CarListBean implements IBaseBean {
    public int orderType;//订单类型
    public int goodsType;//商品类型
    public double distance;//预估路程（单位：公里）
    public int hotelPrice;//酒店价格
    public int interval;//预估时间（单位：分钟）
    public ArrayList<CarBean> carList;

    public int timeNotReachFlag;
    public boolean supportChildseat;
    public int guideFloatSwitch;
    public boolean supportBanner;

    public String estTime;
    public String enableLocal;

    public boolean showHotel;//是否显示酒店

    public int hotelNum;//几天
    public int hourseNum;//几间房


    public CarAdditionalServicePrice additionalServicePrice;


}
