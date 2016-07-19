package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created  on 16/4/12.
 */
public class LocationCity  implements Parcelable{
    public String cityId;
    public String cityName;
    public String countryId;
    public String countryName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeString(this.cityName);
        dest.writeString(this.countryId);
        dest.writeString(this.countryName);
    }

    public LocationCity() {
    }

    protected LocationCity(Parcel in) {
        this.cityId = in.readString();
        this.cityName = in.readString();
        this.countryId = in.readString();
        this.countryName = in.readString();
    }

    public static final Creator<LocationCity> CREATOR = new Creator<LocationCity>() {
        @Override
        public LocationCity createFromParcel(Parcel source) {
            return new LocationCity(source);
        }

        @Override
        public LocationCity[] newArray(int size) {
            return new LocationCity[size];
        }
    };
}
