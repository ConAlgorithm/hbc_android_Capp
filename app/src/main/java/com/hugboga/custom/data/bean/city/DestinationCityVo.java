package com.hugboga.custom.data.bean.city;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HONGBO on 2017/11/28 16:49.
 */

public class DestinationCityVo implements Parcelable{
    public String cityId; //城市ID
    public String cityName; //城市名称

    protected DestinationCityVo(Parcel in) {
        cityId = in.readString();
        cityName = in.readString();
    }

    public static final Creator<DestinationCityVo> CREATOR = new Creator<DestinationCityVo>() {
        @Override
        public DestinationCityVo createFromParcel(Parcel in) {
            return new DestinationCityVo(in);
        }

        @Override
        public DestinationCityVo[] newArray(int size) {
            return new DestinationCityVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cityId);
        parcel.writeString(cityName);
    }
}
