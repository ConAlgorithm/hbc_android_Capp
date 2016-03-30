package com.hugboga.custom.data.bean;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by admin on 2015/7/28.
 */

@Table(name = "city")
public class CityBean implements IBaseBean {


    @Column(name = "city_id", isId = true)
    public int cityId;
    @Column(name = "cn_name")
    public String name;
    @Column(name = "initial")
    public String firstLetter;
    @Column(name = "en_name")
    public String enName;
    @Column(name = "location")
    public String location; //坐标
    @Column(name = "place_name")
    public String placeName;
    @Column(name = "area_code")
    public String areaCode;
    @Column(name = "group_id")
    public int groupId;
    @Column(name = "childseat_switch")
    public boolean childSeatSwitch;
    @Column(name = "is_daily")
    public boolean isDaily;
    @Column(name = "is_single")
    public boolean isSingle;
    @Column(name = "is_city_code")
    public boolean isCityCode;//能查询航班的 1
    @Column(name = "is_hot")
    public boolean isHot;//是否热门
    @Column(name = "hot_weight")
    public int hotWeight;//热门程度
    @Column(name = "daily_tip")
    public String dailyTip;//包车注意提示


    public boolean isSelected = false;//是否被选择
    public boolean isFirst = false;//是否第一个首字母出现

    public int stayDay = 0;//呆几天

    public int dataType = -1;// 数据类型 1.历史搜索记录 2.热门城市 3.全部城市

//    @Override
//    public void parser(JSONObject jsonObj) throws JSONException {
//        cityId = jsonObj.optInt("cityId");
//        areaCode = jsonObj.optString("areaCode");
//        name = jsonObj.optString("name");
//        placeName = jsonObj.optString("placeName");
//        firstLetter = jsonObj.optString("cityInitial");
//        enName = jsonObj.optString("enName");
//        location = jsonObj.optString("location");
//        stayDay = jsonObj.optInt("stayDay");
//    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof CityBean) {
//            CityBean bean = (CityBean) obj;
//            if (cityId == ((CityBean) obj).cityId) return true;
//        }
//        return false;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CityBean cityBean = (CityBean) o;

        if (cityId != cityBean.cityId) return false;
        if (groupId != cityBean.groupId) return false;
        if (childSeatSwitch != cityBean.childSeatSwitch) return false;
        if (isDaily != cityBean.isDaily) return false;
        if (isSingle != cityBean.isSingle) return false;
        if (isCityCode != cityBean.isCityCode) return false;
        if (isHot != cityBean.isHot) return false;
        if (hotWeight != cityBean.hotWeight) return false;
        if (isSelected != cityBean.isSelected) return false;
        if (isFirst != cityBean.isFirst) return false;
        if (stayDay != cityBean.stayDay) return false;
        if (dataType != cityBean.dataType) return false;
        if (!name.equals(cityBean.name)) return false;
        if (firstLetter != null ? !firstLetter.equals(cityBean.firstLetter) : cityBean.firstLetter != null)
            return false;
        if (!enName.equals(cityBean.enName)) return false;
        if (!location.equals(cityBean.location)) return false;
        if (!placeName.equals(cityBean.placeName)) return false;
        return !(dailyTip != null ? !dailyTip.equals(cityBean.dailyTip) : cityBean.dailyTip != null);

    }

    @Override
    public int hashCode() {
        int result = cityId;
        result = 31 * result + name.hashCode();
        result = 31 * result + enName.hashCode();
        result = 31 * result + placeName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "{" + "cityId=" + cityId + ",name=" + name + ",groupId=" + groupId + ",isDaily=" + isDaily + ",isSingle=" + isSingle + "}";
    }
}

