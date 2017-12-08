package com.hugboga.custom.data.bean.city;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HONGBO on 2017/11/27 21:01.
 */

public class BeginnerDirectionVo implements Parcelable{
    public String title;   //标题
    public String subTitle;    //副标题
    public String backgroundImageUrl;  //背景图URL
    public String beginnerDirectionUrl; //说明URL

    protected BeginnerDirectionVo(Parcel in) {
        title = in.readString();
        subTitle = in.readString();
        backgroundImageUrl = in.readString();
        beginnerDirectionUrl = in.readString();
    }

    public static final Creator<BeginnerDirectionVo> CREATOR = new Creator<BeginnerDirectionVo>() {
        @Override
        public BeginnerDirectionVo createFromParcel(Parcel in) {
            return new BeginnerDirectionVo(in);
        }

        @Override
        public BeginnerDirectionVo[] newArray(int size) {
            return new BeginnerDirectionVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(subTitle);
        parcel.writeString(backgroundImageUrl);
        parcel.writeString(beginnerDirectionUrl);
    }
}
