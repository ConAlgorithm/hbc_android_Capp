package com.hugboga.custom.data.bean.city;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HONGBO on 2017/11/27 21:02.
 */

public class DayCountVo implements Parcelable {
    public String type;   //类型
    public String title;   //名称

    protected DayCountVo(Parcel in) {
        type = in.readString();
        title = in.readString();
    }

    public static final Creator<DayCountVo> CREATOR = new Creator<DayCountVo>() {
        @Override
        public DayCountVo createFromParcel(Parcel in) {
            return new DayCountVo(in);
        }

        @Override
        public DayCountVo[] newArray(int size) {
            return new DayCountVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(title);
    }
}
