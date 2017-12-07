package com.hugboga.custom.data.bean.city;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 目的地玩法下单配置
 * Created by HONGBO on 2017/11/28 10:37.
 */

public class ServiceConfigVo implements Parcelable{
    public String title; //名称
    public String desc; //说明
    public String imageUrl; //图片地址
    public List<String> serviceLabelList; //服务标签列表
    public int depCityId; //出发城市Id 线路圈、国家为0
    public int serviceType; //1：接送机 3：包车 4：次租

    protected ServiceConfigVo(Parcel in) {
        title = in.readString();
        desc = in.readString();
        imageUrl = in.readString();
        serviceLabelList = in.createStringArrayList();
        depCityId = in.readInt();
        serviceType = in.readInt();
    }

    public static final Creator<ServiceConfigVo> CREATOR = new Creator<ServiceConfigVo>() {
        @Override
        public ServiceConfigVo createFromParcel(Parcel in) {
            return new ServiceConfigVo(in);
        }

        @Override
        public ServiceConfigVo[] newArray(int size) {
            return new ServiceConfigVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(desc);
        parcel.writeString(imageUrl);
        parcel.writeStringList(serviceLabelList);
        parcel.writeInt(depCityId);
        parcel.writeInt(serviceType);
    }
}
