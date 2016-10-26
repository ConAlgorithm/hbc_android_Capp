package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;
import com.hugboga.custom.action.data.ActionBean;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/10/19.
 */
public class HomeBean implements Serializable {

    public HeadVideo dynamicPic;                        // gif
    public HeadVideo headVideo;                         // 顶部视频
    public ArrayList<SkuItemBean> fixGoods;             // 固定线路（超省心）
    public ArrayList<SkuItemBean> recommendGoods;       // 推荐线路（超自由）
    private ArrayList<HotCity> hotCities;               // 热门目的地
    public ArrayList<TravelStory> travelStories;        // 旅行故事
    public ArrayList<ActivePage> activities;            // 推荐线路（超自由）


    private ArrayList<ArrayList<HotCity>> hotCityList;  // 对数据做拆分,6个一条

    public ArrayList<ArrayList<HotCity>> getHotCityList() {
        if (hotCityList == null) {
            hotCityList = new ArrayList<ArrayList<HotCity>>();
            if (hotCities != null && hotCities.size() > 0) {
                ArrayList<HotCity> itemList = new ArrayList<HotCity>(6);
                final int size = hotCities.size();
                for (int i = 0; i < size; i++) {
                    HotCity hotCity = hotCities.get(i);
                    if (hotCity == null) {
                        continue;
                    }
                    itemList.add(hotCity);
                    final int itemListSize = itemList.size();
                    if (itemListSize == 6) {
                        hotCityList.add(itemList);
                        itemList = new ArrayList<HotCity>(6);
                    } else if (i == size -1 && itemListSize > 0) {
                        hotCityList.add(itemList);
                    }
                }
            }
        }
        return hotCityList;
    }

    /*
    * 顶部视频、GIF
    * */
    public static class HeadVideo implements Serializable {
        public String videoName;    // 头部视频名称
        public String videoUrl;     // 视频地址
        public int videoVersion;    // 视频版本
    }

    /*
    * 热门目的地
    * */
    public static class HotCity implements Serializable {
        public int continentId;                 // 大洲ID
        public String continentName;            // 大洲名称
        public int countryId;                   // 国家ID
        public String countryName;              // 国家名称
        public int cityId;                      // 城市ID
        public String cityName;                 // 城市名称
        public String cityNameEn;               // 城市英文名
        public int cityGuideAmount;             // 城市可服务司导数
        public String cityDesc;                 // 城市描述
        public String cityPicture;              // 城市图
        public String cityHeadPicture;          // 城市头图
    }

    /*
    * 旅行故事
    * */
    public static class TravelStory implements Serializable {
        public int storyLableType;              // 标签类型（1，司导故事；2，旅客故事）
        public String storyLableName;           // 标签名称
        public String storyPicture;             // 封面图
        public String storyName;                // 标题
        public String storyUrl;                 // 页面链接
    }

    /*
    * 活动
    * */
    public static class ActivePage implements Serializable {
        public int startSettingId;              // 活动ID
        public String activityName;             // 活动名称
        public String picture;                  // 图片
        public String urlAddress;               // 活动URL
        public int requestType;                 // 请求类型（1，h5；2，native）
        public int needLogin;                   // 是否需要登录 1：需要登录，0：不需要登录L
        @SerializedName("pushScheme")
        public ActionBean actionBean;
    }
}
