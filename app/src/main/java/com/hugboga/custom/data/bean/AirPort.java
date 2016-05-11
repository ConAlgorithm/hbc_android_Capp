package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by admin on 2015/7/21.
 */
@Table(name = "airport")
public class AirPort implements IBaseBean ,Parcelable{
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.airportId);
        dest.writeString(this.airportName);
        dest.writeString(this.areaCode);
        dest.writeString(this.cityFirstLetter);
        dest.writeInt(this.cityId);
        dest.writeString(this.cityName);
        dest.writeString(this.location);
        dest.writeString(this.placeName);
        dest.writeString(this.airportCode);
        dest.writeByte(childSeatSwitch ? (byte) 1 : (byte) 0);
        dest.writeByte(bannerSwitch ? (byte) 1 : (byte) 0);
        dest.writeByte(visaSwitch ? (byte) 1 : (byte) 0);
        dest.writeByte(isHot ? (byte) 1 : (byte) 0);
        dest.writeInt(this.hotWeight);
        dest.writeByte(isFirst ? (byte) 1 : (byte) 0);
    }

    public AirPort() {
    }

    protected AirPort(Parcel in) {
        this.airportId = in.readInt();
        this.airportName = in.readString();
        this.areaCode = in.readString();
        this.cityFirstLetter = in.readString();
        this.cityId = in.readInt();
        this.cityName = in.readString();
        this.location = in.readString();
        this.placeName = in.readString();
        this.airportCode = in.readString();
        this.childSeatSwitch = in.readByte() != 0;
        this.bannerSwitch = in.readByte() != 0;
        this.visaSwitch = in.readByte() != 0;
        this.isHot = in.readByte() != 0;
        this.hotWeight = in.readInt();
        this.isFirst = in.readByte() != 0;
    }

    public static final Creator<AirPort> CREATOR = new Creator<AirPort>() {
        @Override
        public AirPort createFromParcel(Parcel source) {
            return new AirPort(source);
        }

        @Override
        public AirPort[] newArray(int size) {
            return new AirPort[size];
        }
    };
}
