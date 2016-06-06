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
    public String guideCarType;//车描述
    public String CarNumber;//车牌

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
        dest.writeString(this.guideCarType);
        dest.writeString(this.CarNumber);
    }

    public OrderGuideInfo() {
    }

    protected OrderGuideInfo(Parcel in) {
        this.guideAvatar = in.readString();
        this.guideName = in.readString();
        this.guideID = in.readString();
        this.guideTel = in.readString();
        this.guideStarLevel = in.readDouble();
        this.guideCarType = in.readString();
        this.CarNumber = in.readString();
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
