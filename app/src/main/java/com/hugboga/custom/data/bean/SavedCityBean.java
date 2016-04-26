package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by dyt on 16/4/22.
 */
public class SavedCityBean implements Parcelable{
    public CityBean startCity;
    public int isHalf;
    public int mansNum;
    public int childNum;
    public int childSeat;
    public int baggages;
    public String halfStartDate;


    public String startDate;
    public String endDate;
    public List<CityBean> passCityList;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.startCity, flags);
        dest.writeInt(this.isHalf);
        dest.writeInt(this.mansNum);
        dest.writeInt(this.childNum);
        dest.writeInt(this.childSeat);
        dest.writeInt(this.baggages);
        dest.writeString(this.halfStartDate);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeTypedList(passCityList);
    }

    public SavedCityBean() {
    }

    protected SavedCityBean(Parcel in) {
        this.startCity = in.readParcelable(CityBean.class.getClassLoader());
        this.isHalf = in.readInt();
        this.mansNum = in.readInt();
        this.childNum = in.readInt();
        this.childSeat = in.readInt();
        this.baggages = in.readInt();
        this.halfStartDate = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.passCityList = in.createTypedArrayList(CityBean.CREATOR);
    }

    public static final Creator<SavedCityBean> CREATOR = new Creator<SavedCityBean>() {
        @Override
        public SavedCityBean createFromParcel(Parcel source) {
            return new SavedCityBean(source);
        }

        @Override
        public SavedCityBean[] newArray(int size) {
            return new SavedCityBean[size];
        }
    };
}
