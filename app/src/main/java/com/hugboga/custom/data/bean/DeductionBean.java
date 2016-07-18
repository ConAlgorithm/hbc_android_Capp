package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;



public class DeductionBean implements Parcelable{

    public String deduction;
    public String leftAmount;
    public String priceToPay;

//    deduction	是	double	抵扣金额
//    leftAmount	是	double	剩余旅游基金
//    priceToPay	是	double	还需支付金额


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deduction);
        dest.writeString(this.leftAmount);
        dest.writeString(this.priceToPay);
    }

    public DeductionBean() {
    }

    protected DeductionBean(Parcel in) {
        this.deduction = in.readString();
        this.leftAmount = in.readString();
        this.priceToPay = in.readString();
    }

    public static final Creator<DeductionBean> CREATOR = new Creator<DeductionBean>() {
        @Override
        public DeductionBean createFromParcel(Parcel source) {
            return new DeductionBean(source);
        }

        @Override
        public DeductionBean[] newArray(int size) {
            return new DeductionBean[size];
        }
    };
}
