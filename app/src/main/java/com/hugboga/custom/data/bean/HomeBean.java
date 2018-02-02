package com.hugboga.custom.data.bean;

import android.text.TextUtils;

import com.hugboga.custom.action.data.ActionBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 16/10/19.
 */
public class HomeBean implements Serializable {

    public ArrayList<BannerBean> bannerInfolList;                    // banner轮播列表
    public ArrayList<HotAlbumBean> hotAlbumList;                     // 热门专辑信息
    public ArrayList<TransferBean> transferList;                     // 接送机信息列表
    public ArrayList<CharteredBean> charteredList;                   // 包车信息列表
    public ArrayList<ExcitedActivityBean> excitingActivityList;      // 精彩活动列表

    public static class BannerBean implements Serializable {
        public int bannerType;                          // 1.活动 2.商品 3.广告
        public int bannerSettingId;                     // 商品ID 或者 活动ID
        public String bannerName;                       // banner名称
        public String bannerPicture;                    // bannner图片
        public String bannerAddress;                    // 活动或者广告URL
        public String bannerDesc;                       // 活动简介
        public int requestType;                         // 请求类型（1，h5；2，native）
        public String guideAvatar;                      // 司导头像
        public String guideName;                        // 司导姓名
        public int goodsFavoriteNum;                    // 商品收藏数
        public String routeCityDesc;                    // 途径城市
        public String goodsCountryName;                 // 商品所在国家
        public int goodsServiceDay;                     // 商品天数
        public int needLogin;                           // 是否需要登录 1：需要登录，0：不需要登录
        public ActionBean pushScheme;
        public String starCityName;
        public String aliasName;
        public String guideId;
        public String sequence; //本地序列
        public String showName;

        public String getGuideName() {
            if (!TextUtils.isEmpty(guideName)) {
                return guideName;
            } else if (!TextUtils.isEmpty(starCityName)) {
                return starCityName + "司导";
            }
            return "";
        }

        public String getGoodsName() {
            if (!TextUtils.isEmpty(showName)) {
                return showName;
            } else if (!TextUtils.isEmpty(aliasName)) {
                return aliasName;
            } else if (!TextUtils.isEmpty(bannerName)) {
                return bannerName;
            }
            return "";
        }
    }

    public static class HotAlbumBean implements Serializable {
        public String albumId;                          // 专辑ID
        public String albumName;                        // 专辑名称
        public String albumLinkUrl;                     // 专辑跳转URL
        public int albumHotLevel;                       // 专辑热度
        public ArrayList<AlbumBean> albumRelItemList;   // 专辑详细信息
    }

    public static class AlbumBean implements Serializable {
        public String goodsNo;                          // 商品NO
        public String goodsName;                        // 商品名称
        public String goodsPic;                         // 商品图片
        public int perPrice;                            // 商品人均价
        public String goodsDetailUrl;                   // 商品详情页URL
        public String guideId;                          // 司导ID（专辑为2，司导时使用）
        public String guideName;                        // 司导姓名（专辑为2，司导时使用）
        public String guideAvatar;                      // 司导头像（专辑为2，司导时使用）
        public List<String> guideAvatars;               // 司导头像数组
        public String guideHomeDesc;                    // 司导个人简介（专辑为2，司导时使用）
        public int goodsFavoriteNum;                    // 商品收藏数
        public int goodsServiceDayNum;                  // 服务天数
        public int guidesNum;                           // 可服务司导数
        public String routeCityDesc;                    // 途径城市
        public String goodsCountryName;
        public String starCityName;
        public String aliasName;                        // 别名

        public String getGuideName() {
            if (!TextUtils.isEmpty(guideName)) {
                return guideName;
            } else if (!TextUtils.isEmpty(starCityName)) {
                return starCityName + "司导";
            }
            return "";
        }

        public String getGoodsName() {
            if (!TextUtils.isEmpty(aliasName)) {
                return aliasName;
            } else if (!TextUtils.isEmpty(goodsName)) {
                return goodsName;
            }
            return "";
        }
    }

    public static class TransferBean implements Serializable {
        public String airportName;                      // 机场名字
        public String airportCode;                      // 机场三节码
        public String airportPicture;                   // 机场图片
        public int airportPrice;                        // 接送机起价
        public int airportGuideNum;                     // 可服务司导数
        public String airportUserNum;                   // 服务用户数
        public String airportCityId;
        public String airportCityName;
    }

    public static class CharteredBean implements Serializable {
        public int starCityId;                          // 出发城市ID
        public String starCityName;                     // 出发城市名字
        public String starCityPicture;                  // 出发城市图片
        public int charteredPrice;                      // 包车起价
        public int charteredGuideNum;                   // 可服务司导数
        public String charteredUserNum;                 // 服务用户数
    }

    public static class ExcitedActivityBean implements Serializable {
        public int startSettingId;                      // 活动ID
        public String activityName;                     // 活动名称
        public String picture;                          // 图片
        public String urlAddress;                       // 活动URL
        public int requestType;                         // 请求类型（1，h5；2，native）
        public int needLogin;                           // 是否需要登录 1：需要登录，0：不需要登录
        public ActionBean pushScheme;
    }
}
