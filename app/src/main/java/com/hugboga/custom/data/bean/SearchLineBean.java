package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqiang on 17/8/24.
 */

public class SearchLineBean implements Serializable {
    public int count;
    public List<GoodsPublishStatusVo>  goods;

    public static class GoodsPublishStatusVo{
        public String no;//商品编号
        public String name;//商品名称
        public String pics;//商品图片
        public String depCityName;//出发城市名
        public String depPlaceName;//出发国家名
        public double prePrice;//人均价
        public String keyword;//关键字
        public String goodsDetailUrl;//跳转
        public List<String> poiNames;//poiName
        public List<String> tagNames;//属于描述内容，属于“韩国”游玩线路
    }
}
