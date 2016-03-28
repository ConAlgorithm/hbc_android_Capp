package com.hugboga.custom.data.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by admin on 2015/7/21.
 */
@Table(name = "airport")
public class AirPort implements IBaseBean {
    @Column(name = "airport_id", isId = true)
    public int airportId;
    @Column(name = "airport_name")
    public String airportName;
    @Column(name = "area_code")
    public String areaCode;
    @Column(name = "city_initial")
    public String cityFirstLetter;
    @Column(name = "city_id")
    public int cityId;
    @Column(name = "city_name")
    public String cityName;
    @Column(name = "airport_location")
    public String location;//坐标
    @Column(name = "place_name")
    public String placeName;
    @Column(name = "airport_code")
    public String airportCode;
    @Column(name = "childseat_switch")
    public boolean childSeatSwitch;
    @Column(name = "banner_switch")
    public boolean bannerSwitch;
    @Column(name = "landing_visa_switch")
    public boolean visaSwitch;
    @Column(name = "is_hot")
    public boolean isHot;//是否热门
    @Column(name = "hot_weight")
    public int hotWeight;//热门程度

    public boolean isFirst = false;

//    @Override
//    public void parser(JSONObject jsonObj) throws JSONException {
//        airportId = jsonObj.optInt("airportId");
//        cityId = jsonObj.optInt("cityId");
//        airportName = jsonObj.optString("airportName");
//        areaCode = jsonObj.optString("areaCode");
//        cityFirstLetter = jsonObj.optString("cityInitial");
//        cityName = jsonObj.optString("cityName");
//        placeName = jsonObj.optString("placeName");
//        airportCode = jsonObj.optString("airportCode");
//        location = jsonObj.optString("location");
//    }
}
