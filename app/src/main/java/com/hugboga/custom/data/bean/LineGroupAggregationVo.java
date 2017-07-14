package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqiang on 17/7/12.
 */

public class LineGroupAggregationVo implements Serializable {
    public  int lineGroupId; //线路圈id
    public  String lineGroupName; //线路圈名称
    public List<HotCity> lineGroupCities; //线路圈城市列表
    public List<HotCountry> lineGroupCountries; //线路圈国家列表

    /*
    * 热门城市
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

    /**
     * 国家详情
     */
    public static class HotCountry implements Serializable{
        public  int countryId; //国家id
        public String countryName; //国家名称 汉语
        public String countryNameEn; //国家名称 英语
        public String countryPicture; //国旗
    }
}
