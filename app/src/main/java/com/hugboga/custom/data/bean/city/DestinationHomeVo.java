package com.hugboga.custom.data.bean.city;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * 目的地首页
 * Created by HONGBO on 2017/11/27 20:58.
 */

public class DestinationHomeVo implements Parcelable {
    public int destinationId;   //目的地ID
    public String destinationName;  //目的地名称
    public int destinationType;    //目的地类型 101：线路圈 201：国家 202：城市
    public String destinationImageUrl; //目的地图片URL
    public int destinationGoodsCount;  //目的地关联玩法数量
    public int destinationAssociateGuideCount;  //目的地关联司导数量
    public int destinationServiceGuideCount;   //目的地服务司导数量
    public String recommendationGoodsInfo; //推荐线路标题
    public BeginnerDirectionVo beginnerDirection;  //新手入门
    public List<DayCountVo> dayCountList;  //游玩天数列表
    public List<DestinationTagGroupVo> destinationTagGroupList;  //目的地标签列表
    public List<DestinationCityVo> depCityList;    //出发城市列表
    public List<DestinationGoodsVo> destinationGoodsList;  //目的地玩法列表
    public List<ServiceConfigVo> serviceConfigList;    //自定义包车配置

    protected DestinationHomeVo(Parcel in) {
        destinationId = in.readInt();
        destinationName = in.readString();
        destinationType = in.readInt();
        destinationImageUrl = in.readString();
        destinationGoodsCount = in.readInt();
        destinationAssociateGuideCount = in.readInt();
        destinationServiceGuideCount = in.readInt();
        recommendationGoodsInfo = in.readString();
        beginnerDirection = in.readParcelable(BeginnerDirectionVo.class.getClassLoader());
        dayCountList = in.createTypedArrayList(DayCountVo.CREATOR);
        destinationTagGroupList = in.createTypedArrayList(DestinationTagGroupVo.CREATOR);
        depCityList = in.createTypedArrayList(DestinationCityVo.CREATOR);
        destinationGoodsList = in.createTypedArrayList(DestinationGoodsVo.CREATOR);
        serviceConfigList = in.createTypedArrayList(ServiceConfigVo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(destinationId);
        dest.writeString(destinationName);
        dest.writeInt(destinationType);
        dest.writeString(destinationImageUrl);
        dest.writeInt(destinationGoodsCount);
        dest.writeInt(destinationAssociateGuideCount);
        dest.writeInt(destinationServiceGuideCount);
        dest.writeString(recommendationGoodsInfo);
        dest.writeParcelable(beginnerDirection, flags);
        dest.writeTypedList(dayCountList);
        dest.writeTypedList(destinationTagGroupList);
        dest.writeTypedList(depCityList);
        dest.writeTypedList(destinationGoodsList);
        dest.writeTypedList(serviceConfigList);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DestinationHomeVo> CREATOR = new Creator<DestinationHomeVo>() {
        @Override
        public DestinationHomeVo createFromParcel(Parcel in) {
            return new DestinationHomeVo(in);
        }

        @Override
        public DestinationHomeVo[] newArray(int size) {
            return new DestinationHomeVo[size];
        }
    };
}
