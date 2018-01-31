package com.hugboga.custom.data.bean.city;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by HONGBO on 2017/11/28 10:34.
 */

public class DestinationGoodsVo implements Parcelable, Serializable {

    public String goodsNo; //商品编号
    public int goodsVersion; //商品版本号
    public String goodsName; //商品名称
    public String goodsImageUrl; //商品图片Url
    public String perPrice; //商品人均价格
    public int guideCount; //商品导游数量
    public int userFavorCount; //用户收藏数量
    public int dayCount; //行程天数
    public int depCityId; //出发城市ID
    public String depCityName; //出发城市名称
    public int arrCityId; //到达城市ID
    public String arrCityName; //到达城市名称
    public String skuDetailUrl; //sku详情地址
    public String shareUrl; //分享地址
    public String placeList; //商品途径点
    public String guideHeadImageUrl; //司导头像
    public String guideId; //司导ID

    public DestinationGoodsVo(Parcel in) {
        if (in == null) {
            return;
        }
        goodsNo = in.readString();
        goodsVersion = in.readInt();
        goodsName = in.readString();
        goodsImageUrl = in.readString();
        perPrice = in.readString();
        guideCount = in.readInt();
        userFavorCount = in.readInt();
        dayCount = in.readInt();
        depCityId = in.readInt();
        depCityName = in.readString();
        arrCityId = in.readInt();
        arrCityName = in.readString();
        skuDetailUrl = in.readString();
        shareUrl = in.readString();
        placeList = in.readString();
        guideHeadImageUrl = in.readString();
        guideId = in.readString();
    }

    public static final Creator<DestinationGoodsVo> CREATOR = new Creator<DestinationGoodsVo>() {
        @Override
        public DestinationGoodsVo createFromParcel(Parcel in) {
            return new DestinationGoodsVo(in);
        }

        @Override
        public DestinationGoodsVo[] newArray(int size) {
            return new DestinationGoodsVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(goodsNo);
        parcel.writeInt(goodsVersion);
        parcel.writeString(goodsName);
        parcel.writeString(goodsImageUrl);
        parcel.writeString(perPrice);
        parcel.writeInt(guideCount);
        parcel.writeInt(userFavorCount);
        parcel.writeInt(dayCount);
        parcel.writeInt(depCityId);
        parcel.writeString(depCityName);
        parcel.writeInt(arrCityId);
        parcel.writeString(arrCityName);
        parcel.writeString(skuDetailUrl);
        parcel.writeString(shareUrl);
        parcel.writeString(placeList);
        parcel.writeString(guideHeadImageUrl);
        parcel.writeString(guideId);
    }
}
