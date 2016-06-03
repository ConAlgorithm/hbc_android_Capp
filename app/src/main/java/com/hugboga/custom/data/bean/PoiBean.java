package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/7/21.
 */
public class PoiBean implements IBaseBean ,Parcelable{

    public int id;
    public String key;
    public String placeName;
    public String placeDetail;
    public double lng;
    public double lat;
    public String location;

    public boolean isFirst = false;
    public boolean isHistory = false;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.key);
        dest.writeString(this.placeName);
        dest.writeString(this.placeDetail);
        dest.writeDouble(this.lng);
        dest.writeDouble(this.lat);
        dest.writeString(this.location);
        dest.writeByte(this.isFirst ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isHistory ? (byte) 1 : (byte) 0);
    }

    public PoiBean() {
    }

    protected PoiBean(Parcel in) {
        this.id = in.readInt();
        this.key = in.readString();
        this.placeName = in.readString();
        this.placeDetail = in.readString();
        this.lng = in.readDouble();
        this.lat = in.readDouble();
        this.location = in.readString();
        this.isFirst = in.readByte() != 0;
        this.isHistory = in.readByte() != 0;
    }

    public static final Creator<PoiBean> CREATOR = new Creator<PoiBean>() {
        @Override
        public PoiBean createFromParcel(Parcel source) {
            return new PoiBean(source);
        }

        @Override
        public PoiBean[] newArray(int size) {
            return new PoiBean[size];
        }
    };
}
