package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * SKU 城市列表
 * Created by admin on 2016/3/3.
 */
public class SkuCityBean implements Serializable {

    public String cityId;//城市Id
    public String cityName;//城市名称
    public String cityNameEn;//城市英文名称
    public int cityGuideAmount;//城市导游数量
    public String cityPicture;//城市图片
    public String cityHeadPicture;//城市头图
    public String goodsCount;//城市商品数量
    public String cityDesc;//城市描述
    public ArrayList<String> guideAvatars;//司导头像列表
    @SerializedName("goodses")
    public ArrayList<SkuItemBean> goodsList;//城市商品列表
}
