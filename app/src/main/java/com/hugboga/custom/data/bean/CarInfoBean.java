package com.hugboga.custom.data.bean;

import java.util.List;

/**
 * Created  on 16/4/16.
 */
public class CarInfoBean implements IBaseBean{

    public List<SelectCarBean> cars;
    public int enableLocal;
    public int guideFloatSwitch;
//    public int guideFloats: [ ],
    public int intownKms;
    public int outtownKms;
    public int overChargePerKm;
    public int serviceHours;
    public int stayCharge;
    public boolean supportChildseat;
    public int timeNotReachFlag;
    public int halfDay;
    public int noneCarsState;
    public List<String> noneCarsReason;

//    noneCarsState  int  没有车型报价状态
//    -1.有车型报价 1. 当前时段无法提供服务
//    2.无法提供和您乘坐人数和行李数匹配的车型
//    noneCarsReason  String  无车原因描述


}
