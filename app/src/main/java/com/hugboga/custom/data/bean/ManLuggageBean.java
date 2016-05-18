package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created  on 16/5/18.
 */
public class ManLuggageBean implements Parcelable{
    public int mans;
    public int luggages;
    public int childs;
    public int childSeats;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mans);
        dest.writeInt(this.luggages);
        dest.writeInt(this.childs);
        dest.writeInt(this.childSeats);
    }

    public ManLuggageBean() {
    }

    protected ManLuggageBean(Parcel in) {
        this.mans = in.readInt();
        this.luggages = in.readInt();
        this.childs = in.readInt();
        this.childSeats = in.readInt();
    }

    public static final Creator<ManLuggageBean> CREATOR = new Creator<ManLuggageBean>() {
        @Override
        public ManLuggageBean createFromParcel(Parcel source) {
            return new ManLuggageBean(source);
        }

        @Override
        public ManLuggageBean[] newArray(int size) {
            return new ManLuggageBean[size];
        }
    };
}
