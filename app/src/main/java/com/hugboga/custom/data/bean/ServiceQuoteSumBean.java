package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dyt on 16/4/16.
 */
public class ServiceQuoteSumBean implements Parcelable {
    public int avgSpend;
    public List<DayQuoteBean> dayQuotes;
    public int numOfPerson;
    public int totalPrice;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.avgSpend);
        dest.writeList(this.dayQuotes);
        dest.writeInt(this.numOfPerson);
        dest.writeInt(this.totalPrice);
    }

    public ServiceQuoteSumBean() {
    }

    protected ServiceQuoteSumBean(Parcel in) {
        this.avgSpend = in.readInt();
        this.dayQuotes = new ArrayList<DayQuoteBean>();
        in.readList(this.dayQuotes, DayQuoteBean.class.getClassLoader());
        this.numOfPerson = in.readInt();
        this.totalPrice = in.readInt();
    }

    public static final Creator<ServiceQuoteSumBean> CREATOR = new Creator<ServiceQuoteSumBean>() {
        @Override
        public ServiceQuoteSumBean createFromParcel(Parcel source) {
            return new ServiceQuoteSumBean(source);
        }

        @Override
        public ServiceQuoteSumBean[] newArray(int size) {
            return new ServiceQuoteSumBean[size];
        }
    };
}
