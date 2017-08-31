package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 17/8/28.
 */
public class RecommendedGoodsBean implements Serializable {

    public ArrayList<RecommendedGoodsItemBean> listData;
    public int listCount;
    public String cityHeadPicture;

    public class RecommendedGoodsItemBean implements Serializable {
        public String goodsNo;          // 商品编号
        public String name;             // 商品名称
        public String depCityId;        // 出发城市ID
        public String depCityName;      // 出发城市名称
        public Integer depPlaceId;      // 出发国家ID
        public String depPlaceName;     // 出发国家名称
        public Integer daysCount;       // 行程天数
        public String pics;             // 图片地址
        public String perPrice;         // 出发城市ID
    }
}
