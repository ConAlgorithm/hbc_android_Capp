package com.hugboga.custom.data.bean;


import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by admin on 2015/7/28.
 */

@Table(name = "city")
public class CityBean implements IBaseBean ,Parcelable,Cloneable {


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

    @Column(name="neighbourTip")
    public String neighbourTip;

    @Column(name = "has_airport")
    public boolean hasAirport;//0没有开通的机场 1开通 有开通的机场

    public boolean isSelected = false;//是否被选择
    public boolean isFirst = false;//是否第一个首字母出现

    public int stayDay = 0;//呆几天

    public int dataType = -1;// 数据类型 1.历史搜索记录 2.热门城市 3.全部城市

    public String keyWord = "";

    public boolean isNationality = false;

    public int cityType = 1;// 1 市内 2 周边 3,市外

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CityBean) {
            CityBean bean = (CityBean) obj;
            if (cityId == ((CityBean) obj).cityId) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "{" + "cityId=" + cityId + ",name=" + name + ",groupId=" + groupId+",hasAirport="+hasAirport+" isCityCode="+isCityCode + ",isDaily=" + isDaily + ",isSingle=" + isSingle + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cityId);
        dest.writeString(this.name);
        dest.writeString(this.firstLetter);
        dest.writeString(this.enName);
        dest.writeString(this.location);
        dest.writeString(this.placeName);
        dest.writeString(this.areaCode);
        dest.writeInt(this.groupId);
        dest.writeByte(childSeatSwitch ? (byte) 1 : (byte) 0);
        dest.writeByte(isDaily ? (byte) 1 : (byte) 0);
        dest.writeByte(isSingle ? (byte) 1 : (byte) 0);
        dest.writeByte(isCityCode ? (byte) 1 : (byte) 0);
        dest.writeByte(isHot ? (byte) 1 : (byte) 0);
        dest.writeInt(this.hotWeight);
        dest.writeString(this.dailyTip);
        dest.writeString(this.neighbourTip);
        dest.writeByte(hasAirport ? (byte) 1 : (byte) 0);
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(isFirst ? (byte) 1 : (byte) 0);
        dest.writeInt(this.stayDay);
        dest.writeInt(this.dataType);
        dest.writeString(this.keyWord);
        dest.writeByte(isNationality ? (byte) 1 : (byte) 0);
        dest.writeInt(cityType);
    }

    public CityBean() {
    }

    protected CityBean(Parcel in) {
        this.cityId = in.readInt();
        this.name = in.readString();
        this.firstLetter = in.readString();
        this.enName = in.readString();
        this.location = in.readString();
        this.placeName = in.readString();
        this.areaCode = in.readString();
        this.groupId = in.readInt();
        this.childSeatSwitch = in.readByte() != 0;
        this.isDaily = in.readByte() != 0;
        this.isSingle = in.readByte() != 0;
        this.isCityCode = in.readByte() != 0;
        this.isHot = in.readByte() != 0;
        this.hotWeight = in.readInt();
        this.dailyTip = in.readString();
        this.neighbourTip = in.readString();
        this.hasAirport = in.readByte() != 0;
        this.isSelected = in.readByte() != 0;
        this.isFirst = in.readByte() != 0;
        this.stayDay = in.readInt();
        this.dataType = in.readInt();
        this.keyWord = in.readString();
        this.isNationality = in.readByte() != 0;
        this.cityType = in.readInt();
    }

    public static final Creator<CityBean> CREATOR = new Creator<CityBean>() {
        @Override
        public CityBean createFromParcel(Parcel source) {
            return new CityBean(source);
        }

        @Override
        public CityBean[] newArray(int size) {
            return new CityBean[size];
        }
    };

    @Override
    public Object clone() {
        CityBean cityBean = null;
        try {
            cityBean= (CityBean) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cityBean;
    }
}

