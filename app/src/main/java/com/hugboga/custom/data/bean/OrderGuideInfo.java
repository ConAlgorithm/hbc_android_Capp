package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZHZEPHI on 2015/7/20.
 */
public class OrderGuideInfo implements IBaseBean ,Parcelable {

    public String guideAvatar;//头像
    public String guideName;//车导名
    public String guideID;//车导ID
    public String guideTel;//车导电话
    public double guideStarLevel;//车导级别
    public String guideCar;//车描述
    public String carNumber;//车牌
    public int storeStatus;//导游是否被收藏 0没有 1已收藏

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.guideAvatar);
        dest.writeString(this.guideName);
        dest.writeString(this.guideID);
        dest.writeString(this.guideTel);
        dest.writeDouble(this.guideStarLevel);
        dest.writeString(this.guideCar);
        dest.writeString(this.carNumber);
        dest.writeInt(this.storeStatus);
    }

    public OrderGuideInfo() {
    }

    protected OrderGuideInfo(Parcel in) {
        this.guideAvatar = in.readString();
        this.guideName = in.readString();
        this.guideID = in.readString();
        this.guideTel = in.readString();
        this.guideStarLevel = in.readDouble();
        this.guideCar = in.readString();
        this.carNumber = in.readString();
        this.storeStatus = in.readInt();
    }

    public boolean isCollected() {
        return storeStatus == 1;
    }

    public static final Creator<OrderGuideInfo> CREATOR = new Creator<OrderGuideInfo>() {
        @Override
        public OrderGuideInfo createFromParcel(Parcel source) {
            return new OrderGuideInfo(source);
        }

        @Override
        public OrderGuideInfo[] newArray(int size) {
            return new OrderGuideInfo[size];
        }
    };
}
