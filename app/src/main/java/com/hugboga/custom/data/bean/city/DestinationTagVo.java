package com.hugboga.custom.data.bean.city;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 二级子标签
 * Created by HONGBO on 2017/11/28 10:31.
 */

public class DestinationTagVo implements Parcelable {

    public String tagId;  //标签ID
    public String tagName; //标签名称
    public int tagLevel; //签级别 1：一级 2：二级
    public List<String> goodsDepCityIdSet; //标签关联的商品出发城市集合

    protected DestinationTagVo(Parcel in) {
        tagId = in.readString();
        tagName = in.readString();
        tagLevel = in.readInt();
        goodsDepCityIdSet = in.createStringArrayList();
    }

    public static final Creator<DestinationTagVo> CREATOR = new Creator<DestinationTagVo>() {
        @Override
        public DestinationTagVo createFromParcel(Parcel in) {
            return new DestinationTagVo(in);
        }

        @Override
        public DestinationTagVo[] newArray(int size) {
            return new DestinationTagVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tagId);
        parcel.writeString(tagName);
        parcel.writeInt(tagLevel);
        parcel.writeStringList(goodsDepCityIdSet);
    }
}
