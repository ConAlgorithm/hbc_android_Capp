package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 车型
 * Created by admin on 2015/7/22.
 */
public class CarBean implements Parcelable,IBaseBean {

    public int id;
    public int carType;//类型
    public int carSeat;//座位
    public String desc;//描述
    public String models;//代表车型
    public int originalPrice;//原价
    public int checkInPrice;//促销价
    public String pricemark;//价格戳
    public String urgentCutdownTip;//提示
    public int urgentFlag;//是否急单，1是，0非

    public int imgRes;//图片资源

    public int capOfLuggage;//行李数
    public int capOfPerson;//人数
    public int price; // 钱

    public int localPrice;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.carType);
        dest.writeInt(this.carSeat);
        dest.writeString(this.desc);
        dest.writeString(this.models);
        dest.writeInt(this.originalPrice);
        dest.writeInt(this.checkInPrice);
        dest.writeString(this.pricemark);
        dest.writeString(this.urgentCutdownTip);
        dest.writeInt(this.urgentFlag);
        dest.writeInt(this.imgRes);
        dest.writeInt(this.capOfLuggage);
        dest.writeInt(this.capOfPerson);
        dest.writeInt(this.price);
        dest.writeInt(this.localPrice);
    }

    public CarBean() {
    }

    protected CarBean(Parcel in) {
        this.id = in.readInt();
        this.carType = in.readInt();
        this.carSeat = in.readInt();
        this.desc = in.readString();
        this.models = in.readString();
        this.originalPrice = in.readInt();
        this.checkInPrice = in.readInt();
        this.pricemark = in.readString();
        this.urgentCutdownTip = in.readString();
        this.urgentFlag = in.readInt();
        this.imgRes = in.readInt();
        this.capOfLuggage = in.readInt();
        this.capOfPerson = in.readInt();
        this.price = in.readInt();
        this.localPrice = in.readInt();
    }

    public static final Creator<CarBean> CREATOR = new Creator<CarBean>() {
        @Override
        public CarBean createFromParcel(Parcel source) {
            return new CarBean(source);
        }

        @Override
        public CarBean[] newArray(int size) {
            return new CarBean[size];
        }
    };
}
