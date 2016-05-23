package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created  on 16/5/23.
 */
public class LocationData implements Parcelable{

    public LocationCity city;
    public List<LocationAirports> airports;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.city, flags);
        dest.writeTypedList(this.airports);
    }

    public LocationData() {
    }

    protected LocationData(Parcel in) {
        this.city = in.readParcelable(LocationCity.class.getClassLoader());
        this.airports = in.createTypedArrayList(LocationAirports.CREATOR);
    }

    public static final Creator<LocationData> CREATOR = new Creator<LocationData>() {
        @Override
        public LocationData createFromParcel(Parcel source) {
            return new LocationData(source);
        }

        @Override
        public LocationData[] newArray(int size) {
            return new LocationData[size];
        }
    };
}
