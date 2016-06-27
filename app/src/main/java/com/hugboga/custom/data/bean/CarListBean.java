package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 车型列表
 * Created by admin on 2016/3/20.
 */
public class CarListBean implements Parcelable,IBaseBean {
    public int orderType;//订单类型
    public int goodsType;//商品类型
    public double distance;//预估路程（单位：公里）
    public int interval;//预估时间（单位：分钟）
    public ArrayList<CarBean> carList;

    public int timeNotReachFlag;
    public boolean supportChildseat;
    public int guideFloatSwitch;
    public boolean supportBanner;

    public String estTime;
    public String enableLocal;

    public boolean showHotal;//是否显示酒店


    public CarAdditionalServicePrice additionalServicePrice;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.orderType);
        dest.writeInt(this.goodsType);
        dest.writeDouble(this.distance);
        dest.writeInt(this.interval);
        dest.writeTypedList(this.carList);
        dest.writeInt(this.timeNotReachFlag);
        dest.writeByte(this.supportChildseat ? (byte) 1 : (byte) 0);
        dest.writeInt(this.guideFloatSwitch);
        dest.writeByte(this.supportBanner ? (byte) 1 : (byte) 0);
        dest.writeString(this.estTime);
        dest.writeString(this.enableLocal);
        dest.writeByte(this.showHotal ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.additionalServicePrice, flags);
    }

    public CarListBean() {
    }

    protected CarListBean(Parcel in) {
        this.orderType = in.readInt();
        this.goodsType = in.readInt();
        this.distance = in.readDouble();
        this.interval = in.readInt();
        this.carList = in.createTypedArrayList(CarBean.CREATOR);
        this.timeNotReachFlag = in.readInt();
        this.supportChildseat = in.readByte() != 0;
        this.guideFloatSwitch = in.readInt();
        this.supportBanner = in.readByte() != 0;
        this.estTime = in.readString();
        this.enableLocal = in.readString();
        this.showHotal = in.readByte() != 0;
        this.additionalServicePrice = in.readParcelable(CarAdditionalServicePrice.class.getClassLoader());
    }

    public static final Creator<CarListBean> CREATOR = new Creator<CarListBean>() {
        @Override
        public CarListBean createFromParcel(Parcel source) {
            return new CarListBean(source);
        }

        @Override
        public CarListBean[] newArray(int size) {
            return new CarListBean[size];
        }
    };
}
