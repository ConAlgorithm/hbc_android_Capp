package com.hugboga.custom.data.bean.city;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 一级标签
 * Created by HONGBO on 2017/12/1 12:00.
 */

public class DestinationTagGroupVo implements Parcelable{

    public String tagId;  //标签ID
    public String tagName; //标签名称
    public String fullTagId; //请求ID
    public int tagLevel; //签级别 1：一级 2：二级
    public List<String> goodsDepCityIdSet; //标签关联的商品出发城市集合
    public List<DestinationTagVo> subTagList; //子标签类别

    protected DestinationTagGroupVo(Parcel in) {
        tagId = in.readString();
        tagName = in.readString();
        fullTagId = in.readString();
        tagLevel = in.readInt();
        goodsDepCityIdSet = in.createStringArrayList();
        subTagList = in.createTypedArrayList(DestinationTagVo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tagId);
        dest.writeString(tagName);
        dest.writeString(fullTagId);
        dest.writeInt(tagLevel);
        dest.writeStringList(goodsDepCityIdSet);
        dest.writeTypedList(subTagList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DestinationTagGroupVo> CREATOR = new Creator<DestinationTagGroupVo>() {
        @Override
        public DestinationTagGroupVo createFromParcel(Parcel in) {
            return new DestinationTagGroupVo(in);
        }

        @Override
        public DestinationTagGroupVo[] newArray(int size) {
            return new DestinationTagGroupVo[size];
        }
    };
}
