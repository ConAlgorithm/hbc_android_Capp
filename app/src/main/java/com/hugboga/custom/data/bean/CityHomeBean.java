package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */

public class CityHomeBean implements Serializable {
    public int goodsCount;

    public CityContent cityContent;     //城市首页头
    public CityGuides cityGuides;     //司导数量，及几个没用的头像
    public CountryContent countryContent;//
    public  LineGroupContent lineGroupContent;
    public CityService cityService; //有无三种服务
    @SerializedName("goodses")
    public List<GoodsSec> goodsSecList;    //商品详情
    @SerializedName("goodsThemes")
    public  List<GoodsThemes> goodsThemesList;//商品主题

    public class CountryContent implements Serializable{
        public int countryId;
        public String countryName;
        public String countryNameEn;
    }

    public class LineGroupContent implements Serializable{
        public int lineGroupId;
        public String lineGroupName;
    }



    public  class CityContent implements Serializable{
        public String cityDesc;//城市描述
        public int cityId;//城市Id
        public String cityName;//城市名称
        public String cityNameEn;//城市英文名称
        public String cityPicture;//城市图片
        public String cityHeadPicture;//城市头图

    }


    public  class CityGuides implements Serializable{
        public int guideAmount;//城市导游数量
        public ArrayList<String> guideAvatars;//司导头像列表

    }

    public class GoodsThemes implements Serializable{
        public int themesId ;//主题ID
        public String themeName;//主题名称
    }


    public  class  CityService implements Serializable{
        private int hasAirporService;
        private int hasDailyservice;
        private int hasSingleService;

        public boolean hasSingleService() {
            return hasSingleService == 1;
        }

        public boolean hasDailyservice() {
            return hasDailyservice == 1;
        }

        public boolean hasAirporService() {
            return hasSingleService == 1;
        }
    }

}
