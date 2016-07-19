package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created  on 16/4/16.
 */
public class SelectCarBean implements Parcelable{

    public int avgSpend;
    public int capOfLuggage;
    public int capOfPerson;
    public String carDesc;
    public int carType;
    public int localPrice;
    public int match;
    public String models;
    public int numOfPerson;
    public int overChargePerHour;
    public String payDeadline;
    public int price;
    public String pricemark;
    public int seatCategory;
    public int servicePrice;
    public int totalDays;
    public String urgentCutdownTip;
    public int urgentFlag;
    public int vehiclePrice;
    public ServiceQuoteSumBean serviceQuoteSum;
    public ServiceQuoteSumBean vehicleQuoteSum;
    public String serviceCityNote;
    public String expectedCompTime;
    public int originalPrice;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.avgSpend);
        dest.writeInt(this.capOfLuggage);
        dest.writeInt(this.capOfPerson);
        dest.writeString(this.carDesc);
        dest.writeInt(this.carType);
        dest.writeInt(this.localPrice);
        dest.writeInt(this.match);
        dest.writeString(this.models);
        dest.writeInt(this.numOfPerson);
        dest.writeInt(this.overChargePerHour);
        dest.writeString(this.payDeadline);
        dest.writeInt(this.price);
        dest.writeString(this.pricemark);
        dest.writeInt(this.seatCategory);
        dest.writeInt(this.servicePrice);
        dest.writeInt(this.totalDays);
        dest.writeString(this.urgentCutdownTip);
        dest.writeInt(this.urgentFlag);
        dest.writeInt(this.vehiclePrice);
        dest.writeParcelable(this.serviceQuoteSum, flags);
        dest.writeParcelable(this.vehicleQuoteSum, flags);
        dest.writeString(this.serviceCityNote);
        dest.writeString(this.expectedCompTime);
        dest.writeInt(this.originalPrice);
    }

    public SelectCarBean() {
    }

    protected SelectCarBean(Parcel in) {
        this.avgSpend = in.readInt();
        this.capOfLuggage = in.readInt();
        this.capOfPerson = in.readInt();
        this.carDesc = in.readString();
        this.carType = in.readInt();
        this.localPrice = in.readInt();
        this.match = in.readInt();
        this.models = in.readString();
        this.numOfPerson = in.readInt();
        this.overChargePerHour = in.readInt();
        this.payDeadline = in.readString();
        this.price = in.readInt();
        this.pricemark = in.readString();
        this.seatCategory = in.readInt();
        this.servicePrice = in.readInt();
        this.totalDays = in.readInt();
        this.urgentCutdownTip = in.readString();
        this.urgentFlag = in.readInt();
        this.vehiclePrice = in.readInt();
        this.serviceQuoteSum = in.readParcelable(ServiceQuoteSumBean.class.getClassLoader());
        this.vehicleQuoteSum = in.readParcelable(ServiceQuoteSumBean.class.getClassLoader());
        this.serviceCityNote = in.readString();
        this.expectedCompTime = in.readString();
        this.originalPrice = in.readInt();
    }

    public static final Creator<SelectCarBean> CREATOR = new Creator<SelectCarBean>() {
        @Override
        public SelectCarBean createFromParcel(Parcel source) {
            return new SelectCarBean(source);
        }

        @Override
        public SelectCarBean[] newArray(int size) {
            return new SelectCarBean[size];
        }
    };
}
