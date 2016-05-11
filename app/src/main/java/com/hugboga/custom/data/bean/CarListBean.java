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
    public int interval;//预估时间（单位：分钟）
    public ArrayList<CarBean> carList;

}
