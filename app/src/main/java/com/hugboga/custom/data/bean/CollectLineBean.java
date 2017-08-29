package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqiang on 17/8/28.
 */

public class CollectLineBean implements Serializable {
    public int count;//
    public List<CollectLineItemBean> goodsList;

    public class CollectLineItemBean implements Serializable{
        public String no;//		商品编号
        public String name;//		商品名称
        public String pics;//		图片
        public String depCityName;//		出发城市名
        public String depPlaceName;//		出发国家名
        public int publishStatus;//		是否上线 1上线 -1下线
        public double prePrice;//		人均
        public String goodsDetailUrl;//商品详情URL
    }

}
