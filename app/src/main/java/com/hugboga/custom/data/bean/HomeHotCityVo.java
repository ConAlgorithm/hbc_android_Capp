package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by zhangqiang on 17/7/12.
 */

public class HomeHotCityVo extends HomeBeanV2.HotCity implements Serializable {
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
