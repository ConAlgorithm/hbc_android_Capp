package com.hugboga.custom.data.bean;

import com.hugboga.custom.action.data.ActionBean;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/10/19.
 */
public class HomeBean implements Serializable {

    public ArrayList<AlbumBannerBean> bannerInfolList;               // banner轮播列表
    public ArrayList<AlbumBean> hotAlbumList;                        // 热门专辑信息
    public ArrayList<TransferBean> transferList;                     // 接送机信息列表
    public ArrayList<CharteredBean> charteredList;                   // 包车信息列表
    public ArrayList<ExcitedActivityBean> excitingActivityList;      // 精彩活动列表

    public class AlbumBannerBean implements Serializable {
        public String albumId;                          // 专辑ID
        public String albumName;                        // 专辑名称
        public String albumLinkUrl;                     // 专辑跳转URL
        public int albumHotLevel;                       // 专辑热度
        public String albumImageUrl;                    // TODO 图片
        public ArrayList<AlbumBean> albumRelItems;      // 专辑详细信息
    }

    public class AlbumBean implements Serializable {
        public String goodsNo;                          // 商品NO
        public String goodsName;                        // 商品名称
        public String goodsPic;                         // 商品图片
        public int perPrice;                            // 商品人均价
        public String goodsDetailUrl;                   // 商品详情页URL
        public String guideId;                          // 司导ID（专辑为2，司导时使用）
        public String guideName;                        // 司导姓名（专辑为2，司导时使用）
        public String guideAvatar;                      // 司导头像（专辑为2，司导时使用）
        public String guideHomeDesc;                    // 司导个人简介（专辑为2，司导时使用）
        public int isCollected;                         // TODO 1:收藏 0:未收藏
    }

    public class TransferBean implements Serializable {
        public String airportName;                      // 机场名字
        public String airportCode;                      // 机场三节码
        public String airportPicture;                   // 机场图片
        public int airportPrice;                        // 接送机起价
        public int airportGuideNum;                     // 可服务司导数
        public int airportUserNum;                      // 服务用户数
    }

    public class CharteredBean implements Serializable {
        public int starCityId;                          // 出发城市ID
        public String starCityName;                     // 出发城市名字
        public String starCityPicture;                  // 出发城市图片
        public int charteredPrice;                      // 包车起价
        public int charteredGuideNum;                   // 可服务司导数
        public int charteredUserNum;                    // 服务用户数
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
