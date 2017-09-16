package com.hugboga.custom.data.bean;

import com.hugboga.custom.action.data.ActionBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangqiang on 17/8/4.
 */

public class HomeAggregationVo4 implements Serializable {

    public ArrayList<FilterGuideBean> qualityGuides; //旅行故事
    public ArrayList<ActivityPageSettingVo> bannerActivityList;//首页活动页,轮播图使用
    public ArrayList<HomeAlbumInfoVo> hotAlbumList; //热门专辑
    public ArrayList<HomeAlbumInfoVo> pastAlbumList;//往期专辑
    public ArrayList<HomeCommentInfoVo> commentList;//游客说
    public ArrayList<HomeCityContentVo2> cityRecommendedList;//城市推荐
    public ArrayList<ActivityPageSettingVo> excitingActivityList;//精彩活动列表
    public ArrayList<HomeHotDestination> hotCities;//热门目的地



    public static class ActivityPageSettingVo implements Serializable {
        public int startSettingId = 0;
        public String activityName = "";
        public String picture = "";
        public String urlAddress = "";
        public int requestType = 0;//（1，h5；2，native）
        public int needLogin = 0;
        public ActionBean pushScheme = null;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }
}
