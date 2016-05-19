package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created  on 16/5/19.
 */
public class CarAdditionalServicePrice implements Parcelable {
    public String checkInPrice;
    public String pickupSignPrice;
    public List<CarChildSeatPrice> childSeatPrice;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.checkInPrice);
        dest.writeString(this.pickupSignPrice);
        dest.writeTypedList(this.childSeatPrice);
    }

    public CarAdditionalServicePrice() {
    }

    protected CarAdditionalServicePrice(Parcel in) {
        this.checkInPrice = in.readString();
        this.pickupSignPrice = in.readString();
        this.childSeatPrice = in.createTypedArrayList(CarChildSeatPrice.CREATOR);
    }

    public static final Creator<CarAdditionalServicePrice> CREATOR = new Creator<CarAdditionalServicePrice>() {
        @Override
        public CarAdditionalServicePrice createFromParcel(Parcel source) {
            return new CarAdditionalServicePrice(source);
        }

        @Override
        public CarAdditionalServicePrice[] newArray(int size) {
            return new CarAdditionalServicePrice[size];
        }
    };
}
