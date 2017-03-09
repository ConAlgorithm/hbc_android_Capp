package com.hugboga.custom.data.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SPW on 2017/3/7.
 */
public class HomeBeanV2 implements Serializable {


    public HomeHeaderInfo headAggVo; //首页头部信息
    public HotExplorationAggregation hotExplorationAggVo; //热门探索
    public DestinationAggregation destinationAggVo; //目的地
    public TravelStoryAggregation storyAggVo; // 旅行故事

    /**
     * 首页头部信息
     */
    public static class HomeHeaderInfo implements Serializable{
        public HeadVideo dynamicPic;                        // gif
        public HeadVideo headVideo;                         // 顶部视频
        public  int countryNum;                 //国家数
        public String countryDesc; //国家描述
        public int cityNum; //城市数
        public String cityDesc;//城市描述
        public int guideNum; //司导数
        public String guideDesc; //司导描述
    }

    /*
    * 顶部视频、GIF
    * */
    public static class HeadVideo implements Serializable {
        public String videoName;    // 头部视频名称
        public String videoUrl;     // 视频地址
        public int videoVersion;    // 视频版本
    }

    /**
     * 当季热门探索结构
     */
    public static class HotExplorationAggregation implements Serializable{
        public int listCount; //当季热门探索总数量
        public List<HotExploration> hotExplorations; //当季热门探索列表
    }

    /**
     * 热门探索
     */
    public static class HotExploration implements Serializable{
        public int explorationType;//        热门探索类型（1，目的地城市；2，主题）
        public String explorationId;//城市或主题主键
        public String explorationName ;// 城市或主题名称
        public String explorationPic;//城市或主题图片
        public String  subtitle;//  副标题
        public String guideNo;//       	司导编号
        public String guideName;//        	司导名称
        public String guidePic;//        	司导头像
        public String guideRecommendedReason;//司导推荐语
        public List<SkuItemBean> explorationGoods;// 城市或主题所包含的商品列表
    }



    /**
     * 目的地结构
     */
    public static class DestinationAggregation{
        public List<HotCity> hotCities; //热门目的地
        public List<LineGroupAgg> lineGroupAggs;//线路圈聚合信息
        public int listCount;//线路圈聚合信息总数量
    }


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
     * 线路圈
     */
    public static class LineGroupAgg{
        public  int lineGroupId; //线路圈id
        public  String lineGroupName; //线路圈名称
        public List<HotCity> lineGroupCities; //线路圈城市列表
        public List<HotCountry> lineGroupCountries; //线路圈国家列表

    }

    /**
     * 国家详情
     */
    public static class HotCountry implements Serializable{
        public static int countryId; //国家id
        public String countryName; //国家名称 汉语
        public String countryNameEn; //国家名称 英语
        public String countryPicture; //国旗
    }

    /**
     * 司导故事结构
     */
    public static class TravelStoryAggregation implements Serializable{
        public List<TravelStory> travelStories; //司导故事列表
        public int listCount; //司导故事数量
    }

    /*
    * 司导故事
    * */
    public static class TravelStory implements Serializable {
        public int storyLableType;              // 标签类型（1，司导故事；2，旅客故事）
        public String storyLableName;           // 标签名称
        public String storyPicture;             // 封面图
        public String storyName;                // 标题
        public String storyUrl;                 // 页面链接
        public int cityId;                      //城市id
        public String cityName;                 //城市名称
        public String guideName;                //司导昵称
        public String guidePic;                 //司导头像
    }


}
