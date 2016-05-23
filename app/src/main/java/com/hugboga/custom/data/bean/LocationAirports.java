package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created  on 16/5/23.
 */
public class LocationAirports implements Parcelable{

    public String airportCode;
    public String airportLocation;
    public String airportName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.airportCode);
        dest.writeString(this.airportLocation);
        dest.writeString(this.airportName);
    }

    public LocationAirports() {
    }

    protected LocationAirports(Parcel in) {
        this.airportCode = in.readString();
        this.airportLocation = in.readString();
        this.airportName = in.readString();
    }

    public static final Creator<LocationAirports> CREATOR = new Creator<LocationAirports>() {
        @Override
        public LocationAirports createFromParcel(Parcel source) {
            return new LocationAirports(source);
        }

        @Override
        public LocationAirports[] newArray(int size) {
            return new LocationAirports[size];
        }
    };
}
