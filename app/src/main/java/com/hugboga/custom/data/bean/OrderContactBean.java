package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by qingcha on 16/6/6.
 */
public class OrderContactBean implements IBaseBean ,Parcelable {

    public String name;
    public String areaCode;
    public String mobile;

    protected OrderContactBean(Parcel in) {
        this.name = in.readString();
        this.areaCode = in.readString();
        this.mobile = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(areaCode);
        dest.writeString(areaCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderContactBean> CREATOR = new Creator<OrderContactBean>() {
        @Override
        public OrderContactBean createFromParcel(Parcel in) {
            return new OrderContactBean(in);
        }

        @Override
        public OrderContactBean[] newArray(int size) {
            return new OrderContactBean[size];
        }
    };
}
