package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/4/14.
 */

public class CityListBean implements Serializable {

    public ServiceVo airportServiceVo;  // 机场服务
    public ServiceVo dailyServiceVo;    // 包车服务
    public ServiceVo singleServiceVo;   // 次租服务
    public CityContent cityContent;     // 城市信息
    public CityGuides cityGuides;       // 司导信息
    public List<SkuItemBean> hotLines;  // 热门线路列表

    public class CityContent implements Serializable {
        public String cityDesc;         // 城市描述
        public int cityId;              // 城市Id
        public String cityName;         // 城市名称
        public String cityNameEn;       // 城市英文名称
        public String cityPicture;      // 城市图片
        public String cityHeadPicture;  // 城市头图
    }

    public class CityGuides implements Serializable {
        public int guideAmount;                 // 城市导游数量
        public ArrayList<String> guideAvatars;  // 司导头像列表
    }

    public class ServiceVo implements Serializable {
        public String bookNote;  // 预定信息
        public int serviceON;    // 1.有服务 0.无服务

        public boolean isCanService() {
            return serviceON == 1;
        }
    }

    public boolean isCanService() {
        if (airportServiceVo != null && airportServiceVo.isCanService()) {
            return true;
        }
        if (dailyServiceVo != null && dailyServiceVo.isCanService()) {
            return true;
        }
        if (singleServiceVo != null && singleServiceVo.isCanService()) {
            return true;
        }
        return false;
    }
}
