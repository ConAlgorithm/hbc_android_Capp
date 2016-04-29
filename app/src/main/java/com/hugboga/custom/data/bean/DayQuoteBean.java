package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created  on 16/4/16.
 */
public class DayQuoteBean implements Parcelable{
    public int busySeason;
    public String day;
    public int guideServicePrice;
    public int stayPrice;

    public int totalPrice;

    public int emptyDrivePrice;
    public int longDisPrice;
    public int vehiclePrice;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.busySeason);
        dest.writeString(this.day);
        dest.writeInt(this.guideServicePrice);
        dest.writeInt(this.stayPrice);
        dest.writeInt(this.totalPrice);
        dest.writeInt(this.emptyDrivePrice);
        dest.writeInt(this.longDisPrice);
        dest.writeInt(this.vehiclePrice);
    }

    public DayQuoteBean() {
    }

    protected DayQuoteBean(Parcel in) {
        this.busySeason = in.readInt();
        this.day = in.readString();
        this.guideServicePrice = in.readInt();
        this.stayPrice = in.readInt();
        this.totalPrice = in.readInt();
        this.emptyDrivePrice = in.readInt();
        this.longDisPrice = in.readInt();
        this.vehiclePrice = in.readInt();
    }

    public static final Creator<DayQuoteBean> CREATOR = new Creator<DayQuoteBean>() {
        @Override
        public DayQuoteBean createFromParcel(Parcel source) {
            return new DayQuoteBean(source);
        }

        @Override
        public DayQuoteBean[] newArray(int size) {
            return new DayQuoteBean[size];
        }
    };
}
