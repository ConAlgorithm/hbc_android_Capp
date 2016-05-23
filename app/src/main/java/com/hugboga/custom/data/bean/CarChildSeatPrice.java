package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created  on 16/5/19.
 */
public class CarChildSeatPrice implements Parcelable {
    public int index;
    public String price;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeString(this.price);
    }

    public CarChildSeatPrice() {
    }

    protected CarChildSeatPrice(Parcel in) {
        this.index = in.readInt();
        this.price = in.readString();
    }

    public static final Creator<CarChildSeatPrice> CREATOR = new Creator<CarChildSeatPrice>() {
        @Override
        public CarChildSeatPrice createFromParcel(Parcel source) {
            return new CarChildSeatPrice(source);
        }

        @Override
        public CarChildSeatPrice[] newArray(int size) {
            return new CarChildSeatPrice[size];
        }
    };
}
