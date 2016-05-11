package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/11/25.
 */
public class AssessmentBean implements IBaseBean ,Parcelable{

    public double sceneryNarrate;
    public double serviceAttitude;
    public double routeFamiliar;
    public String content;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.sceneryNarrate);
        dest.writeDouble(this.serviceAttitude);
        dest.writeDouble(this.routeFamiliar);
        dest.writeString(this.content);
    }

    public AssessmentBean() {
    }

    protected AssessmentBean(Parcel in) {
        this.sceneryNarrate = in.readDouble();
        this.serviceAttitude = in.readDouble();
        this.routeFamiliar = in.readDouble();
        this.content = in.readString();
    }

    public static final Creator<AssessmentBean> CREATOR = new Creator<AssessmentBean>() {
        @Override
        public AssessmentBean createFromParcel(Parcel source) {
            return new AssessmentBean(source);
        }

        @Override
        public AssessmentBean[] newArray(int size) {
            return new AssessmentBean[size];
        }
    };
}
