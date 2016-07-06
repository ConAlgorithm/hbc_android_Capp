package com.hugboga.custom.data.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ChooseDateBean implements Parcelable{

    public String start_date;
    public String end_date;
    public String showStartDateStr;
    public String showEndDateStr;
    public String showHalfDateStr;
    public String halfDateStr;
    public Date startDate;
    public Date endDate;
    public Date halfDate;
    public int dayNums;
    public boolean isToday;
    public int type;//1,单天,2 多天


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.start_date);
        dest.writeString(this.end_date);
        dest.writeString(this.showStartDateStr);
        dest.writeString(this.showEndDateStr);
        dest.writeString(this.showHalfDateStr);
        dest.writeString(this.halfDateStr);
        dest.writeLong(this.startDate != null ? this.startDate.getTime() : -1);
        dest.writeLong(this.endDate != null ? this.endDate.getTime() : -1);
        dest.writeLong(this.halfDate != null ? this.halfDate.getTime() : -1);
        dest.writeInt(this.dayNums);
        dest.writeByte(this.isToday ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
    }

    public ChooseDateBean() {
    }

    protected ChooseDateBean(Parcel in) {
        this.start_date = in.readString();
        this.end_date = in.readString();
        this.showStartDateStr = in.readString();
        this.showEndDateStr = in.readString();
        this.showHalfDateStr = in.readString();
        this.halfDateStr = in.readString();
        long tmpStartDate = in.readLong();
        this.startDate = tmpStartDate == -1 ? null : new Date(tmpStartDate);
        long tmpEndDate = in.readLong();
        this.endDate = tmpEndDate == -1 ? null : new Date(tmpEndDate);
        long tmpHalfDate = in.readLong();
        this.halfDate = tmpHalfDate == -1 ? null : new Date(tmpHalfDate);
        this.dayNums = in.readInt();
        this.isToday = in.readByte() != 0;
        this.type = in.readInt();
    }

    public static final Creator<ChooseDateBean> CREATOR = new Creator<ChooseDateBean>() {
        @Override
        public ChooseDateBean createFromParcel(Parcel source) {
            return new ChooseDateBean(source);
        }

        @Override
        public ChooseDateBean[] newArray(int size) {
            return new ChooseDateBean[size];
        }
    };
}
