package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created  on 16/4/16.
 */
public class CarInfoBean implements Parcelable{

    public List<SelectCarBean> cars;
    public int enableLocal;
    public int guideFloatSwitch;
//    public int guideFloats: [ ],
    public int intownKms;
    public int outtownKms;
    public int overChargePerKm;
    public int serviceHours;
    public int stayCharge;
    public boolean supportChildseat;
    public int timeNotReachFlag;
    public int halfDay;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(cars);
        dest.writeInt(this.enableLocal);
        dest.writeInt(this.guideFloatSwitch);
        dest.writeInt(this.intownKms);
        dest.writeInt(this.outtownKms);
        dest.writeInt(this.overChargePerKm);
        dest.writeInt(this.serviceHours);
        dest.writeInt(this.stayCharge);
        dest.writeByte(supportChildseat ? (byte) 1 : (byte) 0);
        dest.writeInt(this.timeNotReachFlag);
        dest.writeInt(this.halfDay);
    }

    public CarInfoBean() {
    }

    protected CarInfoBean(Parcel in) {
        this.cars = in.createTypedArrayList(SelectCarBean.CREATOR);
        this.enableLocal = in.readInt();
        this.guideFloatSwitch = in.readInt();
        this.intownKms = in.readInt();
        this.outtownKms = in.readInt();
        this.overChargePerKm = in.readInt();
        this.serviceHours = in.readInt();
        this.stayCharge = in.readInt();
        this.supportChildseat = in.readByte() != 0;
        this.timeNotReachFlag = in.readInt();
        this.halfDay = in.readInt();
    }

    public static final Creator<CarInfoBean> CREATOR = new Creator<CarInfoBean>() {
        @Override
        public CarInfoBean createFromParcel(Parcel source) {
            return new CarInfoBean(source);
        }

        @Override
        public CarInfoBean[] newArray(int size) {
            return new CarInfoBean[size];
        }
    };
}
