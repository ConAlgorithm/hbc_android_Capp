package com.hugboga.custom.data.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * 包车bean
 * Created by admin on 2015/7/27.
 */
public class DailyBean implements IBaseBean {

    public int startCityID;//城市ID
    public String areaCode;//区号
    public String startCityName;//城市名
    public int terminalCityID;//目的地城市ID
    public String terminalCityName;//目的地名
    public String startLocation;//开始坐标
    public String terminalLocation;//结束坐标


    public String startDate;//开始日期
    public String endDate;//结束日期
    public int totalDay;//总共天数
    public boolean childSeatSwitch;//是否能选儿童座椅

    //2.1
    public Integer[] passByCityID;//途径城市ID
    public String stayCity;//途径城市字符串
    //2.2
    public int oneCityTravel;//1：市内畅游  2：跨城市
    public boolean isHalfDay;//半日包 1半日，0非半日包
    public int inTownDays;//市内天数
    public int outTownDays = -1;//市外城市天数

//    @Override
//    public void parser(JSONObject jsonObj) throws JSONException {
//
//    }
}
