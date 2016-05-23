package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created  on 16/5/16.
 */
public class SaveStartEndCity  implements Parcelable{

    public String startCityName;//开始城市
    public int startCityId;

    public String endCityName;//结束城市
    public int endCityId;

    public String airNo;//航班号

    public int id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startCityName);
        dest.writeInt(this.startCityId);
        dest.writeString(this.endCityName);
        dest.writeInt(this.endCityId);
        dest.writeString(this.airNo);
        dest.writeInt(this.id);
    }

    public SaveStartEndCity() {
    }

    protected SaveStartEndCity(Parcel in) {
        this.startCityName = in.readString();
        this.startCityId = in.readInt();
        this.endCityName = in.readString();
        this.endCityId = in.readInt();
        this.airNo = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<SaveStartEndCity> CREATOR = new Creator<SaveStartEndCity>() {
        @Override
        public SaveStartEndCity createFromParcel(Parcel source) {
            return new SaveStartEndCity(source);
        }

        @Override
        public SaveStartEndCity[] newArray(int size) {
            return new SaveStartEndCity[size];
        }
    };
}
